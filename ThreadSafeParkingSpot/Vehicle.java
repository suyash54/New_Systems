package ThreadSafeParkingSpot;

public class Vehicle {

    private final String number;
    private final VehicleType type;


    public Vehicle(String number,VehicleType type){
        this.number = number;
        this.type = type;
    }

    String getNumber(){ return this.number;}
    VehicleType getType(){ return this.type; }
}
