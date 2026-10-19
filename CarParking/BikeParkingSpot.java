package CarParking;

public class BikeParkingSpot extends ParkingSpot{

    BikeParkingSpot(int id,int levelId){
        super(id,levelId);
    }

    public boolean canFitVehicle(Vehicle vehicle){
           return vehicle instanceof Bike;
    }
}
