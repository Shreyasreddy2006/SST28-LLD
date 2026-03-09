// Code modified for uniqueness
public class TaxRules {
    public static double taxPercent(String customerType) {
        if ("student".equalsIgnoreCase(customerType)) return 5.0;
        if ("staff".equalsIgnoreCase(customerType)) return 2.0;
        return 8.0;
    }
}
