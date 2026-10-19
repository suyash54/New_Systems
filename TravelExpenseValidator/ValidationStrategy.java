package TravelExpenseValidator;

@FunctionalInterface
public interface ValidationStrategy<T extends ValidationResult> {
    void validate(T target,Rule rule);
}
