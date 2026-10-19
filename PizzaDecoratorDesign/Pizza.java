package PizzaDecoratorDesign;

import java.util.Map;

// getBaseCost()        -> the classic decorator responsibility (structural accumulation)
// getToppingServings() -> the escape hatch: flattened view so cross-cutting
//                          rules/pricing can see the WHOLE pizza, not just
//                          "wrapped" (the layers beneath this one).
public interface Pizza {
    double getBaseCost();
    Map<ToppingType, Integer> getToppingServings();
    PizzaSize getSize();
    double getBaseTaxRate();
}
