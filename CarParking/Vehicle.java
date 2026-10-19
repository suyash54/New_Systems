package CarParking;

public abstract class Vehicle {

    int licencePlate;

    Vehicle(int licencePlate){
        this.licencePlate = licencePlate;
    }

    public int getLicenceNumber(){
        return licencePlate;
    }
}
