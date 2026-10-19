package ElevatorDesign;

import java.util.Arrays;
import java.util.List;

public class ExternalButtons implements ButtonStrategy{

    ElevatorController elevatorController;
    public static List<String> buttons = Arrays.asList("UP","DOWN");

    public ExternalButtons(ElevatorController elevatorController){
        this.elevatorController = elevatorController;
    }

    public void pressButton(String button,int currentFloor){
        if(buttons.contains(button)){
            elevatorController.externalRequest(currentFloor);
        }
    }


}
