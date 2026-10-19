package ThreadSafeParkingSpot;

import java.util.List;
import java.util.Optional;

public interface ParkingStrategy {

    Optional<ParkingSpot> selectSpot(List<ParkingFloor> floors,VehicleType type);
}
