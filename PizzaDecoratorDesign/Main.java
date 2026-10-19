package PizzaDecoratorDesign;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ToppingRuleEngine ruleEngine = new ToppingRuleEngine(List.of(
            new CheeseburstMushroomExclusivityRule(),
            new CheeseburstMaxServingsRule(),
            new PineappleSizeRestrictionRule()
        ));
        ToppingPricingRegistry pricingRegistry = new ToppingPricingRegistry();
        PizzaPriceCalculator calculator = new PizzaPriceCalculator(List.of(
            new CheeseburstTaxModifier(),
            new MushroomTaxModifier()
        ));

        // Classic decorator-style construction, still reads the same as the
        // interviewer's original "stack toppings" ask:
        Pizza pizza = new PlainPizza(PizzaSize.MEDIUM, 200.0, 0.10);
        pizza = new ToppingDecorator(pizza, ToppingType.CORN, 1, pricingRegistry, ruleEngine);
        pizza = new ToppingDecorator(pizza, ToppingType.ONION, 1, pricingRegistry, ruleEngine);
        pizza = new ToppingDecorator(pizza, ToppingType.CORN, 1, pricingRegistry, ruleEngine); // 2nd corn -> tier 2 price
        pizza = new ToppingDecorator(pizza, ToppingType.CHEESEBURST, 1, pricingRegistry, ruleEngine);

        System.out.println(calculator.calculate(pizza));
        // Adding mushroom now would throw InvalidToppingException, even though
        // it's a fresh outer layer added AFTER cheeseburst — the flattened
        // view catches it, which pure recursive decorator could not.
    }
}
