package com.example.booking.model;

import com.example.booking.enums.SeatStatus;
import com.example.booking.enums.SeatType;

import java.util.Objects;

public class Seat {
    private final String id;
    private final SeatType type;
    private volatile SeatStatus status;

    public Seat(String id, SeatType type) {
        this.id = Objects.requireNonNull(id);
        this.type = Objects.requireNonNull(type);
        this.status = SeatStatus.AVAILABLE;
    }

    public String getId() { return id; }
    public SeatType getType() { return type; }
    public int getPrice() { return type.getPriceInRupees(); }

    public synchronized SeatStatus getStatus() { return status; }

    public synchronized boolean lock() {
        if (status != SeatStatus.AVAILABLE) return false;
        status = SeatStatus.LOCKED;
        return true;
    }

    public synchronized void confirmBooking() {
        if (status != SeatStatus.LOCKED) throw new IllegalStateException("Seat " + id + " not locked");
        status = SeatStatus.BOOKED;
    }

    public synchronized void release() {
        status = SeatStatus.AVAILABLE;
    }

    @Override
    public String toString() {
        return id + "[" + type + "," + status + "]";
    }
}
