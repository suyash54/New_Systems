package ThreadSafeParkingSpot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Test harness for the ThreadSafeParkingSpot package.
 *
 * Layout codes:  2 = two-wheeler spot, 4 = four-wheeler spot.
 *
 * Two floors are intentionally shaped so the two strategies pick DIFFERENT
 * spots for the same request:
 *   Floor 0 -> few four-wheeler spots, many two-wheeler spots
 *   Floor 1 -> many four-wheeler spots, few two-wheeler spots
 */
public class Main {

    // ---- tiny assert helper ---------------------------------------------
    private static int pass = 0, fail = 0;

    private static void check(String name, boolean cond) {
        System.out.printf("    [%s] %s%n", cond ? "PASS" : "FAIL", name);
        if (cond) pass++; else fail++;
    }

    /**
     * A ThreadFactory is exactly the "new Thread" hook an ExecutorService uses
     * under the hood. Supplying our own lets us name the threads (handy in a
     * debugger / thread dump) while the pool still manages their lifecycle.
     */
    private static ThreadFactory namedFactory(String prefix) {
        return new ThreadFactory() {
            private final AtomicInteger n = new AtomicInteger();
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, prefix + "-" + n.getAndIncrement());
            }
        };
    }

    public static void main(String[] args) throws Exception {
        System.out.println("==================================================");
        System.out.println("      ThreadSafeParkingSpot  -  Test Harness      ");
        System.out.println("==================================================");

        functionalTest();
        strategyTest();
        raceConditionTest();
        churnTest();

        System.out.println("\n--------------------------------------------------");
        System.out.printf ("  RESULT:  %d passed, %d failed%n", pass, fail);
        System.out.println("--------------------------------------------------");
        if (fail > 0) System.exit(1);
    }

    // ----------------------------------------------------------------------
    //  Lot construction
    // ----------------------------------------------------------------------
    private static List<ParkingFloor> buildFloors() {
        int[][] floor0 = {
                {2, 2},
                {2, 4}          // four-wheeler capacity = 1, two-wheeler = 3
        };
        int[][] floor1 = {
                {4, 4, 4},
                {4, 4, 2}       // four-wheeler capacity = 5, two-wheeler = 1
        };
        List<ParkingFloor> floors = new ArrayList<>();
        floors.add(new ParkingFloor(0, floor0));
        floors.add(new ParkingFloor(1, floor1));
        return floors;
    }

    private static int capacity(List<ParkingFloor> floors, VehicleType t) {
        int sum = 0;
        for (ParkingFloor f : floors) sum += f.freeCount(t);
        return sum;
    }

    private static int freeTotal(List<ParkingFloor> floors, VehicleType t) {
        return capacity(floors, t); // freeCount reflects live free set
    }

    private static int occupiedTotal(List<ParkingFloor> floors, VehicleType t) {
        int c = 0;
        for (ParkingFloor f : floors) {
            for (ParkingSpot[] row : f.getGrid())
                for (ParkingSpot s : row)
                    if (s.getType() == t && s.isOccupied()) c++;
        }
        return c;
    }

    // ----------------------------------------------------------------------
    //  1. Functional test - park, read back, remove, re-park
    // ----------------------------------------------------------------------
    private static void functionalTest() {
        System.out.println("\n[1] Functional test (single-threaded)");
        List<ParkingFloor> floors = buildFloors();
        ParkingLot lot = new ParkingLot(floors);
        ParkingStrategy strat = new LowestIndexStrategy();

        int fourCap = capacity(floors, VehicleType.FOUR_WHEELER);

        String a = lot.park(new Vehicle("KA-01", VehicleType.FOUR_WHEELER), "T1", strat);
        String b = lot.park(new Vehicle("KA-02", VehicleType.FOUR_WHEELER), "T2", strat);
        System.out.println("    parked KA-01 -> " + a);
        System.out.println("    parked KA-02 -> " + b);

        check("first park returned a spot", !a.isEmpty());
        check("second park got a DIFFERENT spot", !b.isEmpty() && !a.equals(b));
        check("occupied count == 2", occupiedTotal(floors, VehicleType.FOUR_WHEELER) == 2);
        check("free count decreased by 2", freeTotal(floors, VehicleType.FOUR_WHEELER) == fourCap - 2);

        boolean removed = lot.removeVehicle(a);
        check("removeVehicle succeeded", removed);
        check("double remove is rejected", !lot.removeVehicle(a));
        check("free count restored by 1", freeTotal(floors, VehicleType.FOUR_WHEELER) == fourCap - 1);

        String c = lot.park(new Vehicle("KA-03", VehicleType.FOUR_WHEELER), "T3", strat);
        check("freed spot is reused", c.equals(a));
        System.out.println("    re-parked KA-03 -> " + c + "  (reused freed spot " + a + ")");
    }

    // ----------------------------------------------------------------------
    //  2. Strategy test - the two strategies pick different floors
    // ----------------------------------------------------------------------
    private static void strategyTest() {
        System.out.println("\n[2] Strategy test");

        // LowestIndexStrategy scans floor 0 first -> should land on floor 0
        List<ParkingFloor> f1 = buildFloors();
        ParkingLot lot1 = new ParkingLot(f1);
        String lowest = lot1.park(new Vehicle("LI-1", VehicleType.FOUR_WHEELER), "L1",
                new LowestIndexStrategy());
        System.out.println("    LowestIndexStrategy  -> " + lowest);
        check("LowestIndex picks floor 0", lowest.startsWith("0-"));

        // MaxFreeFloorStrategy picks the floor with the most free spots -> floor 1 (5 > 1)
        List<ParkingFloor> f2 = buildFloors();
        ParkingLot lot2 = new ParkingLot(f2);
        String maxFree = lot2.park(new Vehicle("MF-1", VehicleType.FOUR_WHEELER), "M1",
                new MaxFreeFloorStrategy());
        System.out.println("    MaxFreeFloorStrategy -> " + maxFree);
        check("MaxFreeFloor picks floor 1", maxFree.startsWith("1-"));

        check("the two strategies chose different floors", !lowest.equals(maxFree));
    }

    // ----------------------------------------------------------------------
    //  3. Race condition test - many threads storm limited capacity
    //     Proves: exactly `capacity` succeed, and NO spot is double-booked.
    // ----------------------------------------------------------------------
    private static void raceConditionTest() throws Exception {
        System.out.println("\n[3] Race condition test (contended allocation)");

        final int THREADS = 200;
        List<ParkingFloor> floors = buildFloors();
        ParkingLot lot = new ParkingLot(floors);
        ParkingStrategy strat = new LowestIndexStrategy();
        int cap = capacity(floors, VehicleType.FOUR_WHEELER);

        // Pool with our own ThreadFactory (the `new Thread` hook). Size >= THREADS
        // so every task can sit on the start gate at once (else it would deadlock).
        ExecutorService pool = Executors.newFixedThreadPool(THREADS, namedFactory("parker"));
        CountDownLatch startGate = new CountDownLatch(1);       // fire all at once
        List<Future<String>> futures = new ArrayList<>();

        for (int i = 0; i < THREADS; i++) {
            final int id = i;
            futures.add(pool.submit(() -> {
                startGate.await();                              // block until GO
                return lot.park(
                        new Vehicle("V" + id, VehicleType.FOUR_WHEELER), "T" + id, strat);
            }));
        }

        startGate.countDown();                                  // GO

        // Results arrive via Future -> aggregate single-threaded, no atomics needed.
        Set<String> assigned = new HashSet<>();
        int successes = 0;
        boolean duplicate = false;
        for (Future<String> f : futures) {
            String spot = f.get();                              // blocks until that task ends
            if (!spot.isEmpty()) {
                successes++;
                if (!assigned.add(spot)) duplicate = true;      // same spot twice = bug
            }
        }
        pool.shutdown();

        System.out.printf("    %d threads stormed %d four-wheeler spots%n", THREADS, cap);
        System.out.printf("    successful parks = %d, unique spots = %d%n",
                successes, assigned.size());

        check("successes == capacity (no over-allocation)", successes == cap);
        check("no spot was double-booked", !duplicate);
        check("unique spots == successes", assigned.size() == successes);
        check("occupied count == capacity", occupiedTotal(floors, VehicleType.FOUR_WHEELER) == cap);
        check("free count == 0", freeTotal(floors, VehicleType.FOUR_WHEELER) == 0);
    }

    // ----------------------------------------------------------------------
    //  4. Churn test - concurrent park + remove loop.
    //     Detects transient double-occupancy and verifies the lot returns
    //     to a fully-consistent state afterwards.
    // ----------------------------------------------------------------------
    private static void churnTest() throws Exception {
        System.out.println("\n[4] Churn test (concurrent park + remove)");

        final int WORKERS = 16;
        final int ITERS = 5_000;
        List<ParkingFloor> floors = buildFloors();
        ParkingLot lot = new ParkingLot(floors);
        ParkingStrategy strat = new LowestIndexStrategy();
        int fourCap = capacity(floors, VehicleType.FOUR_WHEELER);

        // Detection state IS shared across the running tasks, so these stay concurrent.
        Set<String> live = ConcurrentHashMap.newKeySet();        // currently-held spots
        AtomicBoolean doubleOccupancy = new AtomicBoolean(false);
        AtomicInteger totalParks = new AtomicInteger();

        ExecutorService pool = Executors.newFixedThreadPool(WORKERS, namedFactory("churn"));
        List<Future<?>> futures = new ArrayList<>();

        for (int w = 0; w < WORKERS; w++) {
            final int id = w;
            futures.add(pool.submit(() -> {
                for (int i = 0; i < ITERS; i++) {
                    String spot = lot.park(
                            new Vehicle("V" + id + "_" + i, VehicleType.FOUR_WHEELER),
                            "T" + id + "_" + i, strat);
                    if (spot.isEmpty()) continue;                 // lot momentarily full
                    totalParks.incrementAndGet();
                    // if the same spot is live in two threads at once -> bug
                    if (!live.add(spot)) doubleOccupancy.set(true);
                    // Un-mark BEFORE freeing: once removeVehicle unlocks, another
                    // thread can legitimately re-park this spot, and if our
                    // live.remove hasn't run yet it would look like a double-book.
                    // We still exclusively hold the spot here, so this is safe.
                    live.remove(spot);
                    lot.removeVehicle(spot);
                }
            }));
        }

        for (Future<?> f : futures) f.get();                      // wait for all + surface errors
        pool.shutdown();

        System.out.printf("    %d workers x %d iterations, %d successful parks%n",
                WORKERS, ITERS, totalParks.get());

        check("no transient double-occupancy", !doubleOccupancy.get());
        check("all spots released (live set empty)", live.isEmpty());
        check("occupied count == 0 at end", occupiedTotal(floors, VehicleType.FOUR_WHEELER) == 0);
        check("free count restored to capacity", freeTotal(floors, VehicleType.FOUR_WHEELER) == fourCap);
    }
}
