package ThreadSafeParkingSpot;

import lombok.*;

import java.time.Instant;


@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Ticket {
    private final String ticketId;
    private final String vehicleNumber;
    private final String spotId;
    private final VehicleType type;
    private final Instant entryTime = Instant.now();

}
