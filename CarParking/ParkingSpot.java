package CarParking;

public abstract class ParkingSpot {

    public int id;
    public int levelId;
    public boolean isOccupied = false;
    Vehicle vehicle;

    ParkingSpot(int id, int levelId ){

        this.id = id;
        this.levelId = levelId;

    }

    public void parkVehicle(Vehicle vehicle){
        this.isOccupied = true;
        this.vehicle = vehicle;
        System.out.println("The Vehicle "+vehicle.getLicenceNumber()+" is parked in spot " +id);
    }

    public abstract boolean canFitVehicle(Vehicle vehicle);


    public void freeSpot(){
        this.isOccupied=false;
        System.out.println("Spot with id: " + id + "is free");
    }

    public Vehicle getVehicle(){
        return this.vehicle;
    }

}
