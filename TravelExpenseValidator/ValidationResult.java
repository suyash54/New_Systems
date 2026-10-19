package TravelExpenseValidator;

public interface ValidationResult {
    boolean isValid();
    String getFailureReason();
    String getResultType();
}
