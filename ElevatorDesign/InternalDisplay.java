package ElevatorDesign;

public class InternalDisplay {

    int floorNumber;
    Direction direction;

    public InternalDisplay(int floorNumber, Direction direction){
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
