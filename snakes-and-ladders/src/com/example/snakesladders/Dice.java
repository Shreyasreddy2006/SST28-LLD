package com.example.snakesladders;

import java.util.Random;

public class Dice {
    private int faces;
    private Random random;

    public Dice(int faces) {
        this.faces = faces;
        this.random = new Random();
    }

    public int roll() {
        return random.nextInt(faces) + 1;
    }

    public void setSeed(long seed) {
        this.random = new Random(seed);
    }
}
