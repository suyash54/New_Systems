package TravelExpenseValidator;

import java.util.Arrays;
import java.util.List;

public class DestinationTripStrategy implements TripValidationStrategy{

    @Override
    public void validate(Trip trip, Rule rule) {
        // Example rule: field="destination", constraint="NYC,LON,SFO" (allowed destinations)
        if ("destination".equalsIgnoreCase(rule.getField())) {
            String destination = trip.get("destination");
            if (destination == null) {
                trip.markInvalid("Trip is missing 'destination' field");
                return;
            }
            List<String> allowed = Arrays.asList(rule.getConstraint().split(","));
            if (!allowed.contains(destination)) {
                trip.markInvalid(
                        "Destination '" + destination + "' is not in allowed list: " + allowed
                );
            }
        }
    }
}
