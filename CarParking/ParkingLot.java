package CarParking;

import java.util.ArrayList;
import java.util.List;

public class ParkingLot {

    private List<ParkingLevel> parkingLevel;
    int levelsCapacity;

    ParkingLot(int capacity, List<ParkingLevel> parkingLevel){
        this.levelsCapacity = capacity;
        if(parkingLevel.isEmpty()){
            this.parkingLevel = new ArrayList<>();
        }
        else{
            this.parkingLevel = parkingLevel;
        }
    }

    public ParkingLevel addParkingLevel(int levelId, int spotsCapacity) throws Exception {
        if (this.levelsCapacity > 0) {
            ParkingLevel newParkingLevel = new ParkingLevel(levelId, spotsCapacity);
            parkingLevel.add(newParkingLevel);
            this.levelsCapacity--;
            return newParkingLevel;
        } else {
            throw new Exception("Capacity reached its limit");
        }
    }

    public void addParkingSpot(ParkingLevel parkingLevel, VehicleType type, int id) throws Exception {
         parkingLevel.addParkingSpot(id,type);
    }

    public void ParkVehicle(Vehicle vehicle) throws Exception {

       for(ParkingLevel parkingLevel: parkingLevel){
           ParkingSpot parkingSpot = parkingLevel.isParkingSpotAvailable(vehicle);
           if(parkingSpot != null){
               parkingSpot.parkVehicle(vehicle);
               return;
           }
        }
        throw new Exception("No Parking Spot Available");
    }

    public void freeSpot(Vehicle vehicle){
        for(ParkingLevel parkingLevel: parkingLevel){
            for(ParkingSpot parkingSpot: parkingLevel.getParkingSpot()){
                if(parkingSpot.isOccupied && parkingSpot.getVehicle().getLicenceNumber() == vehicle.getLicenceNumber()){
                    parkingSpot.freeSpot();
                    return;
                }
            }
        }
    }

}
