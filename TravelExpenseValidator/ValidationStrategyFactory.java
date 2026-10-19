package TravelExpenseValidator;

import java.util.EnumMap;
import java.util.Map;

public class ValidationStrategyFactory {

    private static final Map<RuleCategory, ExpenseValidationStrategy> expenseStrategies =
            new EnumMap<>(RuleCategory.class);

    private static final Map<RuleCategory, TripValidationStrategy> tripStrategies =
            new EnumMap<>(RuleCategory.class);


    static{
        expenseStrategies.put(RuleCategory.AIRFARE, new AirFlowExpenseValidationStrategy());
        expenseStrategies.put(RuleCategory.HOTEL, new HotelExpenseValidationStrategy());
        tripStrategies.put(RuleCategory.TRIP_TOTAL, new TotalTripValidationStrategy());
        tripStrategies.put(RuleCategory.TRIP_DESTINATION, new DestinationTripStrategy());
    }

    public static ExpenseValidationStrategy getExpenseValidationStrategy(RuleCategory category){
        ExpenseValidationStrategy expenseValidationStrategy = expenseStrategies.get(category);
        if(expenseValidationStrategy == null){
            throw new IllegalArgumentException(
                    "No expense strategy registered for category: " + category
            );
        }

        return expenseValidationStrategy;
    }

    public static TripValidationStrategy getTripValidationStrategy(RuleCategory category){
        TripValidationStrategy tripValidationStrategy = tripStrategies.get(category);
        if(tripValidationStrategy == null){
            throw new IllegalArgumentException(
                    "No trip strategy registered for category: " + category
            );
        }

        return tripValidationStrategy;
    }


    public static boolean isExpenseCategory(RuleCategory category){
        return expenseStrategies.containsKey(category);
    }

    public static boolean isTripCategory(RuleCategory category){
        return tripStrategies.containsKey(category);
    }
}
