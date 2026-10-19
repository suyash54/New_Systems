package TravelExpenseValidator;

public class AirFlowExpenseValidationStrategy implements ExpenseValidationStrategy{

    @Override
    public void validate(Expense expense,Rule rule){

        if ("amount".equalsIgnoreCase(rule.getField())) {
            String rawAmount = expense.get("amount");
            if (rawAmount == null) {
                expense.markInvalid("Airfare expense is missing 'amount' field");
                return;
            }
            try {
                double amount = Double.parseDouble(rawAmount);
                double maxAllowed = Double.parseDouble(rule.getConstraint());
                if (amount > maxAllowed) {
                    expense.markInvalid(
                            "Airfare amount " + amount + " exceeds allowed max " + maxAllowed
                    );
                }
            } catch (NumberFormatException e) {
                expense.markInvalid("Airfare amount is not a valid number: " + rawAmount);
            }
        }
    }
}
