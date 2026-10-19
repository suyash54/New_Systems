package PizzaDecoratorDesign;

public class DefaultPricingStrategy implements ToppingPricingStrategy {
    private final int perServingCost;
    public DefaultPricingStrategy(int perServingCost) { this.perServingCost = perServingCost; }
    @Override
    public int calculateCost(PizzaSize size, int existingServings, int newServings) {
        return perServingCost * newServings;
    }
}
