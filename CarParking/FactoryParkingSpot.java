package CarParking;

public class FactoryParkingSpot {


    public static ParkingSpot createSpot(int spotId, VehicleType vehicleType,int levelId) throws Exception{

        return switch (vehicleType) {
            case BIKE -> new BikeParkingSpot(spotId,levelId);
            case BUS -> new BusParkingSpot(spotId,levelId);
            case CAR -> new CarParkingSpot(spotId,levelId);
            default -> throw new Exception("unkown vehicle Type");
        };
    }
}
