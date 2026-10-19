package PizzaDecoratorDesign;
import java.util.Map;

public class MushroomTaxModifier implements TaxModifier {
    @Override
    public boolean isApplicable(Map<ToppingType, Integer> toppings) {
        return toppings.getOrDefault(ToppingType.MUSHROOM, 0) > 0;
    }
    @Override
    public double apply(double rate) { return rate - 0.10 * rate; }
}
