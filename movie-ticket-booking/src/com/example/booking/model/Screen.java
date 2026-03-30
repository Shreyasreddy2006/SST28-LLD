package com.example.booking.model;

import com.example.booking.enums.SeatType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Screen {
    private final String id;
    private final String name;
    private final List<Seat> seats;

    public Screen(String id, String name, int regularCount, int premiumCount, int vipCount) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);

        List<Seat> allSeats = new ArrayList<>();
        char row = 'A';
        row = addSeats(allSeats, row, regularCount, SeatType.REGULAR);
        row = addSeats(allSeats, row, premiumCount, SeatType.PREMIUM);
        addSeats(allSeats, row, vipCount, SeatType.VIP);

        this.seats = Collections.unmodifiableList(allSeats);
    }

    private static char addSeats(List<Seat> seats, char startRow, int count, SeatType type) {
        int perRow = 10;
        int added = 0;
        char row = startRow;
        while (added < count) {
            int col = (added % perRow) + 1;
            seats.add(new Seat(row + "" + col, type));
            added++;
            if (added % perRow == 0) row++;
        }
        if (count > 0 && count % perRow != 0) row++;
        return row;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public List<Seat> getSeats() { return seats; }

    @Override
    public String toString() { return name + " (" + seats.size() + " seats)"; }
}
