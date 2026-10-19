package ThreadSafeParkingSpot;

import java.util.List;
import java.util.Optional;

public class LowestIndexStrategy implements  ParkingStrategy{

    public Optional<ParkingSpot> selectSpot(List<ParkingFloor> floors,
                                            VehicleType type){

        for(ParkingFloor f: floors){
            Optional<ParkingSpot> s = f.peekLowest(type);
            if(s.isPresent()) return  s;
        }

        return Optional.empty();
    }
}
