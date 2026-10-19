package TravelExpenseValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExpenseEvaluator {


    public List<ValidationResult> evaluate(List<Rule> rules,List<Map<String,String>> rawExpenses) {

        Map<String,String> tripMetaData = null;
        List<Expense> expenseList = new ArrayList<>();


        for(Map<String,String> row: rawExpenses){
            String type = row.get("type");
            if("TRIP".equalsIgnoreCase(type)){
                tripMetaData = row;
            }
            else{
                expenseList.add(new Expense(row));
            }
        }

        if(tripMetaData == null){
            throw new IllegalArgumentException(
                    "No row with type=TRIP found in expenses list"
            );
        }

        Trip trip = new Trip(tripMetaData, expenseList);

        for(Expense expense: expenseList){

            String category = expense.get("category");
            if(category == null)
                continue;

            RuleCategory ruleCategory;

            try{
                ruleCategory = RuleCategory.valueOf(category.toUpperCase());
            } catch (IllegalArgumentException e) {
                continue;
            }

            if(ValidationStrategyFactory.isExpenseCategory(ruleCategory)) {
                ExpenseValidationStrategy strategy = ValidationStrategyFactory.getExpenseValidationStrategy(ruleCategory);


                rules.stream()
                        .filter(r -> r.getCategory().equalsIgnoreCase(category))
                        .forEach(r -> strategy.validate(expense,r));
            }

        }

        for (Rule rule : rules) {
            RuleCategory ruleCategory;
            try {
                ruleCategory = RuleCategory.valueOf(rule.getCategory().toUpperCase());
            } catch (IllegalArgumentException e) {
                continue;
            }

            if (ValidationStrategyFactory.isTripCategory(ruleCategory)) {
                TripValidationStrategy strategy =
                        ValidationStrategyFactory.getTripValidationStrategy(ruleCategory);
                strategy.validate(trip, rule);
            }
        }

        // ── 4. Collect all invalid results ───────────────────────────────────
        List<ValidationResult> invalid = new ArrayList<>();

        expenseList.stream()
                .filter(e -> !e.isValid())
                .forEach(invalid::add);

        if (!trip.isValid()) {
            invalid.add(trip);
        }

        return invalid;
    }
}
