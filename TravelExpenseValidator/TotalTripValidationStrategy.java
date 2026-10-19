package TravelExpenseValidator;

public class TotalTripValidationStrategy implements TripValidationStrategy{


    @Override
    public void validate(Trip trip,Rule rule){
      if("totalAmount".equalsIgnoreCase(rule.getField())){
          double total = trip.getExpenses().stream()
                  .mapToDouble(exp ->{
                      String amt = exp.get("amount");
                      if(amt == null) return 0.0;
                      try{ return Double.parseDouble(amt);} catch (NumberFormatException e) {
                          return 0.0;
                      }
                  }).sum();

          try{
              double maxAllowed = Double.parseDouble(rule.getConstraint());
              if (total > maxAllowed) {
                  trip.markInvalid(
                          "Total trip spend " + total + " exceeds allowed max " + maxAllowed
                  );
              }
          } catch (NumberFormatException e) {
              trip.markInvalid("Trip constraint is not a valid number: " + rule.getConstraint());
          }
          }
      }
}
