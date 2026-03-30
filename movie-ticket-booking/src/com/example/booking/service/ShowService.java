package com.example.booking.service;

import com.example.booking.enums.City;
import com.example.booking.exception.ShowConflictException;
import com.example.booking.model.Movie;
import com.example.booking.model.Screen;
import com.example.booking.model.Show;
import com.example.booking.model.Theatre;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class ShowService {
    private final Map<String, Show> showsById = new ConcurrentHashMap<>();
    private final Map<String, ReentrantLock> screenLocks = new ConcurrentHashMap<>();

    public Show addShow(String showId, Movie movie, Screen screen, Theatre theatre,
                        LocalDateTime startTime) {
        ReentrantLock lock = screenLocks.computeIfAbsent(screen.getId(), k -> new ReentrantLock());
        lock.lock();
        try {
            LocalDateTime endTime = startTime.plusMinutes(movie.getDurationMinutes() + 15);
            for (Show existing : showsById.values()) {
                if (!existing.getScreen().getId().equals(screen.getId())) continue;
                LocalDateTime existStart = existing.getStartTime();
                LocalDateTime existEnd = existStart.plusMinutes(
                        existing.getMovie().getDurationMinutes() + 15);
                if (startTime.isBefore(existEnd) && endTime.isAfter(existStart)) {
                    throw new ShowConflictException(
                            "Show conflicts with existing show: " + existing);
                }
            }
            Show show = new Show(showId, movie, screen, theatre, startTime);
            showsById.put(showId, show);
            return show;
        } finally {
            lock.unlock();
        }
    }

    public Show getShow(String showId) {
        return showsById.get(showId);
    }

    public List<Movie> showMovies(City city) {
        return showsById.values().stream()
                .filter(s -> s.getTheatre().getCity() == city)
                .map(Show::getMovie)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Show> getShowsForMovie(String movieId, City city) {
        return showsById.values().stream()
                .filter(s -> s.getMovie().getId().equals(movieId)
                        && s.getTheatre().getCity() == city)
                .collect(Collectors.toList());
    }

    public List<Show> getAll() {
        return new ArrayList<>(showsById.values());
    }
}
