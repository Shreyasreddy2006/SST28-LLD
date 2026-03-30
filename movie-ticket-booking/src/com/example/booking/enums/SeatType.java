package com.example.booking.enums;

public  enum SeatType {
    REGULAR(200),
    PREMIUM(350),
    VIP(500);

    private final int priceInRupees;

    SeatType(int priceInRupees) {
        this.priceInRupees = priceInRupees;
    }

    public int getPriceInRupees() { return priceInRupees; }
}
