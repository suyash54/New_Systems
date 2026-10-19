
package PizzaDecoratorDesign;

public class CornPricingStrategy implements ToppingPricingStrategy {
    @Override
    public int calculateCost(PizzaSize size, int existingServings, int newServings) {
        int total = 0;
        for (int i = 0; i < newServings; i++) {
            total += costForServingIndex(size, existingServings + i);
        }
        return total;
    }

    private int costForServingIndex(PizzaSize size, int servingIndex) {
        if (size == PizzaSize.LARGE) return 20;
        if (size == PizzaSize.MEDIUM) return servingIndex == 0 ? 50 : 40;
        return ToppingCatalog.baseCost(ToppingType.CORN);
    }
}
