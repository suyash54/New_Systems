package PizzaDecoratorDesign;

import java.util.Map;

public class CheeseburstMaxServingsRule implements ToppingValidationRule {
    private static final Map<PizzaSize, Integer> MAX_SERVINGS = Map.of(
        PizzaSize.SMALL, 1,
        PizzaSize.MEDIUM, 2
    );

    @Override
    public void validate(Map<ToppingType, Integer> existing, PizzaSize size,
                          ToppingType topping, int servings) {
        if (topping != ToppingType.CHEESEBURST) return;
        int max = MAX_SERVINGS.getOrDefault(size, Integer.MAX_VALUE);
        int totalAfterAdd = existing.getOrDefault(ToppingType.CHEESEBURST, 0) + servings;
        if (totalAfterAdd > max) {
            throw new InvalidToppingException(
                "Cheeseburst servings exceed max (" + max + ") allowed for size " + size);
        }
    }
}
