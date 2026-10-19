package ElevatorDesign;

import java.util.Comparator;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.ReentrantLock;

public class ElevatorController {
    private ElevatorCar elevatorCar;
    private TreeSet<Integer> upStops, downStops;
    private ReentrantLock lock;

    public ElevatorController(ElevatorCar elevatorCar) {
        this.elevatorCar = elevatorCar;
        upStops = new TreeSet<>();
        downStops = new TreeSet<>(Comparator.reverseOrder());
        lock = new ReentrantLock();
    }

    private void handleElevatorRequest(int targetFloor) {
        if (elevatorCar.currentFloor == targetFloor) {
            System.out.println("You are already at your destination");
            return;
        }

        if (elevatorCar.currentFloor < targetFloor) {
            upStops.add(targetFloor);
        } else {
            downStops.add(targetFloor);
        }
        processRequest();
    }

    public void externalRequest(int currentFloor) {
        handleElevatorRequest(currentFloor);
    }

    public void internalRequest(String button) {
        handleElevatorRequest(Integer.parseInt(button));
    }

    // Rest of your code remains the same
    public void processRequest() {
        if (elevatorCar.direction == Direction.UP) {
            lock.lock();
            try {
                processUpRequest();
                processDownRequest();
            } finally {
                lock.unlock();
            }
        } else if (elevatorCar.direction == Direction.DOWN || elevatorCar.direction == Direction.IDLE) {
            lock.lock();
            try {
                processDownRequest();
                processUpRequest();
            } finally {
                lock.unlock();
            }
        }
    }

    public void processUpRequest() {
        while (!upStops.isEmpty()) {
            Integer floor = upStops.pollFirst();
            System.out.println("You are at floor: " + floor);
            if(floor!=null)
             elevatorCar.setCurrentFloor(floor);
        }
    }

    public void processDownRequest() {
        while (!downStops.isEmpty()) {
            Integer floor = downStops.pollFirst();
            System.out.println("You are at floor: " + floor);
            if(floor != null)
             elevatorCar.setCurrentFloor(floor);
        }
    }
}
