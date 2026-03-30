package com.example.booking.service;

import com.example.booking.enums.City;
import com.example.booking.model.Theatre;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TheatreService {
    private final Map<String, Theatre> theatresById = new ConcurrentHashMap<>();

    public void addTheatre(Theatre theatre) {
        theatresById.put(theatre.getId(), theatre);
    }

    public Theatre getTheatre(String theatreId) {
        return theatresById.get(theatreId);
    }

    public List<Theatre> showTheatres(City city) {
        return theatresById.values().stream()
                .filter(t -> t.getCity() == city)
                .collect(Collectors.toList());
    }

    public List<Theatre> getAll() {
        return new ArrayList<>(theatresById.values());
    }
}
