package CarParking;

public class BusParkingSpot extends ParkingSpot {

    BusParkingSpot(int id,int levelId){
        super(id,levelId);
    }

    public boolean canFitVehicle(Vehicle vehicle){
        return vehicle instanceof Bus;
    }
}
