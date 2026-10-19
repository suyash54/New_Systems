package PizzaDecoratorDesign;
import java.util.Map;

public class CheeseburstTaxModifier implements TaxModifier {
    @Override
    public boolean isApplicable(Map<ToppingType, Integer> toppings) {
        return toppings.getOrDefault(ToppingType.CHEESEBURST, 0) > 0;
    }
    @Override
    public double apply(double rate) { return rate + 0.30 * rate; }
}
