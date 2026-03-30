package com.example.booking.model;

import com.example.booking.enums.SeatStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Show {
    private final String id;
    private final Movie movie;
    private final Screen screen;
    private final Theatre theatre;
    private final LocalDateTime startTime;

    public Show(String id, Movie movie, Screen screen, Theatre theatre, LocalDateTime startTime) {
        this.id = Objects.requireNonNull(id);
        this.movie = Objects.requireNonNull(movie);
        this.screen = Objects.requireNonNull(screen);
        this.theatre = Objects.requireNonNull(theatre);
        this.startTime = Objects.requireNonNull(startTime);
    }

    public String getId() { return id; }
    public Movie getMovie() { return movie; }
    public Screen getScreen() { return screen; }
    public Theatre getTheatre() { return theatre; }
    public LocalDateTime getStartTime() { return startTime; }

    public List<Seat> getAvailableSeats() {
        return screen.getSeats().stream()
                .filter(s -> s.getStatus() == SeatStatus.AVAILABLE)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return movie.getTitle() + " @ " + theatre.getName() + " " + screen.getName()
                + " [" + startTime + "]";
    }
}
