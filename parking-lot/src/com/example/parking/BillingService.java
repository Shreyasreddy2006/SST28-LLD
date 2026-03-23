package com.example.parking;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.Map;
import java.util.HashMap;

public class BillingService {
    private Map<SlotType, Double> hourlyRates;

    public BillingService() {
        hourlyRates = new HashMap<>();
        hourlyRates.put(SlotType.SMALL, 10.0);
        hourlyRates.put(SlotType.MEDIUM, 20.0);
        hourlyRates.put(SlotType.LARGE, 30.0);
    }

    public void setRate(SlotType slotType, double ratePerHour) {
        hourlyRates.put(slotType, ratePerHour);
    }

    public double calculateBill(ParkingTicket ticket, LocalDateTime exitTime) {
        Duration duration = Duration.between(ticket.getEntryTime(), exitTime);
        long totalMinutes = duration.toMinutes();
        long hours = (totalMinutes + 59) / 60;
        if (hours == 0) {
            hours = 1;
        }
        double rate = hourlyRates.get(ticket.getSlotType());
        return hours * rate;
    }
}
