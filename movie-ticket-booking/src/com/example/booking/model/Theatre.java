package com.example.booking.model;

import com.example.booking.enums.City;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Theatre {
    private final String id;
    private final String name;
    private final City city;
    private final List<Screen> screens;

    public Theatre(String id, String name, City city, List<Screen> screens) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.city = Objects.requireNonNull(city);
        this.screens = Collections.unmodifiableList(new ArrayList<>(screens));
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public City getCity() { return city; }
    public List<Screen> getScreens() { return screens; }

    @Override
    public String toString() { return name + " [" + city + "]"; }
}
