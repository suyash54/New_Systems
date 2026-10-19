package CarParking;

public class CarParkingSpot extends ParkingSpot{

    CarParkingSpot(int id,int levelId){
        super(id, levelId);
    }

    public boolean canFitVehicle(Vehicle vehicle){
        return vehicle instanceof Car;
    }
}
