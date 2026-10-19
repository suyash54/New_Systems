package TravelExpenseValidator;

public class HotelExpenseValidationStrategy implements ExpenseValidationStrategy{

    @Override
    public void validate(Expense expense,Rule rule){

            // Example rule: field="nights", constraint="7" (max nights allowed)
            if ("nights".equalsIgnoreCase(rule.getField())) {
                String rawNights = expense.get("nights");
                if (rawNights == null) {
                    expense.markInvalid("Hotel expense is missing 'nights' field");
                    return;
                }
                try {
                    int nights = Integer.parseInt(rawNights);
                    int maxNights = Integer.parseInt(rule.getConstraint());
                    if (nights > maxNights) {
                        expense.markInvalid(
                                "Hotel nights " + nights + " exceeds allowed max " + maxNights
                        );
                    }
                } catch (NumberFormatException e) {
                    expense.markInvalid("Hotel nights is not a valid number: " + rawNights);
                }
            }
    }
}
