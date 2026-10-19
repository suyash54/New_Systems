package PizzaDecoratorDesign;
import java.util.Map;

public class CheeseburstMushroomExclusivityRule implements ToppingValidationRule {
    @Override
    public void validate(Map<ToppingType, Integer> existing, PizzaSize size,
                          ToppingType topping, int servings) {
        if (topping == ToppingType.CHEESEBURST && existing.getOrDefault(ToppingType.MUSHROOM, 0) > 0) {
            throw new InvalidToppingException("Cannot add cheeseburst: mushroom already present");
        }
        if (topping == ToppingType.MUSHROOM && existing.getOrDefault(ToppingType.CHEESEBURST, 0) > 0) {
            throw new InvalidToppingException("Cannot add mushroom: cheeseburst already present");
        }
    }
}
