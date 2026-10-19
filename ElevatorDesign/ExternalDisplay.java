package ElevatorDesign;

public class ExternalDisplay {
    int floorNumber;
    Direction direction;

    public ExternalDisplay(int floorNumber, Direction direction){
        this.floorNumber = floorNumber;
        this.direction = direction;
    }

    public void updateDisplay(int floorNumber, Direction direction){
        this.floorNumber = floorNumber;
        this.direction = direction;
    }

    public void display(){
        System.out.println("Floor Number: "+floorNumber+" Direction: "+direction);
    }
}
