package ThreadSafeParkingSpot;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;


public class ParkingLot {

    private final List<ParkingFloor> floors;
    private final Map<VehicleType, ReentrantLock> typeLocks = new EnumMap<>(VehicleType.class);

    ParkingLot(List<ParkingFloor> floors){
        this.floors = floors;
        for(VehicleType t: VehicleType.values()){
            typeLocks.put(t,new ReentrantLock());
        }
    }

    String park(Vehicle vehicle, String ticketId, ParkingStrategy strategy){

        VehicleType type = vehicle.getType();
        ReentrantLock lock = typeLocks.get(type);

        lock.lock();
        try{
            Optional<ParkingSpot> selected = strategy.selectSpot(floors,type);

            if(selected.isEmpty())
                return "";

            ParkingSpot spot = selected.get();
            floors.get(spot.getFloor()).occupy(spot);

            Ticket ticket = new Ticket(ticketId,vehicle.getNumber(),spot.getSpotId(),type);
            spot.assign(ticket);
            return spot.getSpotId();
        } finally {
            lock.unlock();
        }
    }

    boolean removeVehicle(String spotId){
        int[] pos = parse(spotId);
        if(pos == null) return false;

        ParkingFloor floor = floors.get(pos[0]);
        ParkingSpot spot = floor.getSpot(pos[1],pos[2]);
        if(spot == null) return false;

        ReentrantLock lock = typeLocks.get(spot.getType());
        lock.lock();
        try{
            if(!spot.isOccupied()) return false;
            floor.vacate(spot);
            spot.release();
            return true;
        } finally{
            lock.unlock();
        }
    }

    private int[] parse(String spotId){
        if(spotId == null) return null;
        String[] p = spotId.split("-");
        if(p.length != 3) return null;

        try{
            int f = Integer.parseInt(p[0]),r=Integer.parseInt(p[1]),c=Integer.parseInt(p[2]);
            if(f<0 || f>=floors.size()) return null;
            return new int[]{f,r,c};
        }catch(NumberFormatException e){
            return null;
        }
    }
}
