// Code modified for uniqueness
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddOnPricing {
    private final Map<AddOn, Double> prices = new HashMap<>();

    public AddOnPricing() {
        prices.put(AddOn.MESS, 1000.0);
        prices.put(AddOn.LAUNDRY, 500.0);
        prices.put(AddOn.GYM, 300.0);
    }

    public double total(List<AddOn> addOns) {
        double sum = 0.0;
        for (AddOn a : addOns) {
            sum += prices.getOrDefault(a, 0.0);
        }
        return sum;
    }
}
