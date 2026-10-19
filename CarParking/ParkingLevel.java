package CarParking;

import java.util.ArrayList;
import java.util.List;

public class ParkingLevel {
    List<ParkingSpot> parkingSpots;
    int levelId;
    int spotsCapacity;
    private FactoryParkingSpot factoryParkingSpot;

    ParkingLevel(int levelId, int spotsCapacity){
        this.levelId = levelId;
        this.spotsCapacity = spotsCapacity;
        this.parkingSpots = new ArrayList<>();
    }

    public ParkingSpot isParkingSpotAvailable(Vehicle vehicle){
        for(ParkingSpot parkingSpot: this.parkingSpots){
            if(!(parkingSpot.isOccupied) && parkingSpot.canFitVehicle(vehicle))
                return parkingSpot;
        }
        return null;
    }

    public void addParkingSpot(int spotId, VehicleType vehicleType) throws Exception {

        if(this.spotsCapacity > 0){
          ParkingSpot parkingSpot =  FactoryParkingSpot.createSpot(spotId,vehicleType,levelId);
          parkingSpots.add(parkingSpot);
          this.spotsCapacity--;
        }
        else{
            throw new Exception("Capacity reached its limit");
        }
    }

    public List<ParkingSpot> getParkingSpot(){
        return this.parkingSpots;

    }
}
