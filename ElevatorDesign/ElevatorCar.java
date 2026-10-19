package ElevatorDesign;

public class ElevatorCar {

    Direction direction;
    int currentFloor;
    InternalDisplay internalDisplay;

    public ElevatorCar(Direction direction, int currentFloor){
        this.direction = direction;
        this.currentFloor = currentFloor;
    }

    public void showDisplay(){
        internalDisplay.display();
    }

    public void setCurrentFloor(int currentFloor){
        this.currentFloor = currentFloor;
    }

    public int getCurrentFloor(){
        return currentFloor;
    }

}
