package PizzaDecoratorDesign;

import java.util.Map;

public class PineappleSizeRestrictionRule implements ToppingValidationRule {
    @Override
    public void validate(Map<ToppingType, Integer> existing, PizzaSize size,
                          ToppingType topping, int servings) {
        if (topping == ToppingType.PINEAPPLE && size == PizzaSize.SMALL) {
            throw new InvalidToppingException("Pineapple is only allowed on medium and large pizzas");
        }
    }
}
