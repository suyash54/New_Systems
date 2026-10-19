package ElevatorDesign;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ElevatorSystem {

    public static void main(String[] args){



        ElevatorCar elevatorCar = new ElevatorCar(Direction.IDLE,-1);
        ElevatorController elevatorController = new ElevatorController(elevatorCar);
        InternalDisplay internalDisplay = new InternalDisplay(elevatorCar.getCurrentFloor(),elevatorCar.direction);
        ExternalDisplay externalDisplay = new ExternalDisplay(elevatorCar.getCurrentFloor(),elevatorCar.direction);
        InternalButtons internalButtons = new InternalButtons(elevatorController);
        ExternalButtons externalButtons = new ExternalButtons(elevatorController);


        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() ->
                internalButtons.pressButton("0", elevatorCar.getCurrentFloor()));

        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() ->
                externalButtons.pressButton("UP",7));

        CompletableFuture<Void> future6 = CompletableFuture.runAsync(() ->
                externalButtons.pressButton("UP",5));

        CompletableFuture<Void> future3 = CompletableFuture.runAsync(() ->
                internalButtons.pressButton("8", elevatorCar.getCurrentFloor()));

        internalButtons.pressButton("9", elevatorCar.getCurrentFloor());

        CompletableFuture<Void> future4 = CompletableFuture.runAsync(() ->
                externalButtons.pressButton("DOWN", 10));

        CompletableFuture<Void> future5 = CompletableFuture.runAsync(() ->
                internalButtons.pressButton("6", elevatorCar.getCurrentFloor()));

        CompletableFuture.allOf(future1, future2, future3).join();
        CompletableFuture.allOf(future4, future5, future6).join();

    }
}
