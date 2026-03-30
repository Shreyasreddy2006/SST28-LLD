package com.example.booking.model;

import java.util.Objects;

public class Movie {
    private final String id;
    private final String title;
    private final int durationMinutes;

    public Movie(String id, String title, int durationMinutes) {
        this.id = Objects.requireNonNull(id);
        this.title = Objects.requireNonNull(title);
        this.durationMinutes = durationMinutes;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public int getDurationMinutes() { return durationMinutes; }

    @Override
    public String toString() {
        return title + " (" + durationMinutes + " min)";
    }
}
