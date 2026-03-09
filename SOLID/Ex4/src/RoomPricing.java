// Code modified for uniqueness
import java.util.HashMap;
import java.util.Map;

public class RoomPricing {
    private final Map<Integer, Double> prices = new HashMap<>();

    public RoomPricing() {
        prices.put(LegacyRoomTypes.SINGLE, 14000.0);
        prices.put(LegacyRoomTypes.DOUBLE, 15000.0);
        prices.put(LegacyRoomTypes.TRIPLE, 12000.0);
        prices.put(LegacyRoomTypes.DELUXE, 16000.0);
    }

    public double basePrice(int roomType) {
        return prices.getOrDefault(roomType, 16000.0);
    }
}
