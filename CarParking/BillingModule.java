package CarParking;

public class BillingModule {

    public static void calculateBill(Vehicle vehicle, ParkingSpot parkingSpot){
        System.out.println("The bill for the vehicle with licence plate number "+vehicle.getLicenceNumber()+" is 100");
    }
}
