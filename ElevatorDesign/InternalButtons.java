package ElevatorDesign;

import java.util.Arrays;
import java.util.List;

public class InternalButtons implements ButtonStrategy{

    ElevatorController elevatorController;
    public static List<String> buttons = Arrays.asList("-1", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10");

    public InternalButtons(ElevatorController elevatorController){
        this.elevatorController = elevatorController;
    }

    public void pressButton(String button ,int currentFloor){
        if(buttons.contains(button)){
            elevatorController.internalRequest(button);
        }
    }
}
