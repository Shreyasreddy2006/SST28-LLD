package com.example.pen;

public class BottleRefill implements RefillBehavior {
    @Override
    public String refill() {
        return "Drawing ink from a bottle using the converter.";
    }
}
