// Code modified for uniqueness
import java.util.List;
import java.util.Map;

public class BillCalculator {
    private final Map<String, MenuItem> menu;

    public BillCalculator(Map<String, MenuItem> menu) {
        this.menu = menu;
    }

    public double subtotal(List<OrderLine> lines) {
        double sum = 0.0;
        for (OrderLine l : lines) {
            MenuItem item = menu.get(l.itemId);
            sum += item.price * l.qty;
        }
        return sum;
    }

    public double lineTotal(OrderLine l) {
        return menu.get(l.itemId).price * l.qty;
    }

    public String itemName(OrderLine l) {
        return menu.get(l.itemId).name;
    }
}
