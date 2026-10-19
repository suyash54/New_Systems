package CarParking;

import java.util.ArrayList;

public class CarParking {

    public static void main(String args[]) {
        try {
            ParkingLot parkingLot = new ParkingLot(2, new ArrayList<>());
            ParkingLevel parkingLevel1 = parkingLot.addParkingLevel(1, 3);
            ParkingLevel parkingLevel2 = parkingLot.addParkingLevel(2, 3);
            ParkingLevel parkingLevel3 = parkingLot.addParkingLevel(2, 3);

            parkingLot.addParkingSpot(parkingLevel1, VehicleType.CAR, 1);
            parkingLot.addParkingSpot(parkingLevel1, VehicleType.CAR, 2);
            parkingLot.addParkingSpot(parkingLevel1, VehicleType.BUS, 3);
            parkingLot.addParkingSpot(parkingLevel2, VehicleType.CAR, 1);
            parkingLot.addParkingSpot(parkingLevel2, VehicleType.CAR, 2);
            parkingLot.addParkingSpot(parkingLevel2, VehicleType.BIKE, 3);

            Car car1 = new Car(112131);
            Car car2 = new Car(112132);
            Bus bus1 = new Bus(112133);
            Bike bike1 = new Bike(112134);
            Bike bike2 = new Bike(112135);
            Bus bus2 = new Bus(112136);
            parkingLot.ParkVehicle(car1);
            parkingLot.ParkVehicle(car2);
            parkingLot.ParkVehicle(bus1);
            parkingLot.freeSpot(car1);
            parkingLot.freeSpot(bus1);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}