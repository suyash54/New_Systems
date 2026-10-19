package ThreadSafeParkingSpot;

import java.util.List;
import java.util.Optional;

public class MaxFreeFloorStrategy implements ParkingStrategy{

    public Optional<ParkingSpot> selectSpot(List<ParkingFloor> floors, VehicleType type){
        ParkingFloor best = null;
        int bestCount = 0;

        for(ParkingFloor f: floors){
            int c = f.freeCount(type);
            if(c>bestCount){
                bestCount = c;
                best = f;
            }
        }

        return best == null ? Optional.empty(): best.peekLowest(type);
    }
}
