package com.example.snakesladders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Board {
    private int size;
    private int totalCells;
    private List<Snake> snakes;
    private List<Ladder> ladders;
    private Map<Integer, Integer> snakeMap;
    private Map<Integer, Integer> ladderMap;

    public  Board(int size, int numberOfSnakes, int numberOfLadders, DifficultyLevel difficulty) {
        this.size = size;
        this.totalCells = size * size;
        this.snakes = new ArrayList<>();
        this.ladders = new ArrayList<>();
        this.snakeMap = new HashMap<>();
        this.ladderMap = new HashMap<>();
        generateSnakesAndLadders(numberOfSnakes, numberOfLadders, difficulty);
    }

    public  Board(int size, List<Snake> snakes, List<Ladder> ladders) {
        this.size = size;
        this.totalCells = size * size;
        this.snakes = snakes;
        this.ladders = ladders;
        this.snakeMap = new HashMap<>();
        this.ladderMap = new HashMap<>();

        for (Snake snake : snakes) {
            snakeMap.put(snake.getHead(), snake.getTail());
        }
        for (Ladder ladder : ladders) {
            ladderMap.put(ladder.getStart(), ladder.getEnd());
        }
    }

    private void generateSnakesAndLadders(int numberOfSnakes, int numberOfLadders, DifficultyLevel difficulty) {
        Random random = new Random();
        Set<Integer> occupiedCells = new HashSet<>();
        occupiedCells.add(1);
        occupiedCells.add(totalCells);

        for (int i = 0; i < numberOfSnakes; i++) {
            int head, tail;
            do {
                if (difficulty == DifficultyLevel.HARD) {
                    head = random.nextInt(totalCells / 2) + (totalCells / 2) + 1;
                    tail = random.nextInt(head - 2) + 2;
                } else {
                    head = random.nextInt(totalCells - 2) + 2;
                    tail = random.nextInt(head - 1) + 1;
                }
                head = Math.min(head, totalCells - 1);
                tail = Math.max(tail, 1);
            } while (occupiedCells.contains(head) || occupiedCells.contains(tail) || head <= tail);

            Snake snake = new Snake(head, tail);
            snakes.add(snake);
            snakeMap.put(head, tail);
            occupiedCells.add(head);
            occupiedCells.add(tail);
        }

        for (int i = 0; i < numberOfLadders; i++) {
            int start, end;
            do {
                if (difficulty == DifficultyLevel.EASY) {
                    start = random.nextInt(totalCells / 2) + 1;
                    end = random.nextInt(totalCells / 2) + (totalCells / 2);
                } else {
                    start = random.nextInt(totalCells - 2) + 2;
                    end = random.nextInt(totalCells - start - 1) + start + 1;
                }
                start = Math.max(start, 2);
                end = Math.min(end, totalCells - 1);
            } while (occupiedCells.contains(start) || occupiedCells.contains(end) || start >= end);

            Ladder ladder = new Ladder(start, end);
            ladders.add(ladder);
            ladderMap.put(start, end);
            occupiedCells.add(start);
            occupiedCells.add(end);
        }
    }

    public int getTotalCells() {
        return totalCells;
    }

    public int getSize() {
        return size;
    }

    public int getFinalPosition(int position) {
        if (snakeMap.containsKey(position)) {
            return snakeMap.get(position);
        }
        if (ladderMap.containsKey(position)) {
            return ladderMap.get(position);
        }
        return position;
    }

    public boolean hasSnakeAt(int position) {
        return snakeMap.containsKey(position);
    }

    public boolean hasLadderAt(int position) {
        return ladderMap.containsKey(position);
    }

    public List<Snake> getSnakes() {
        return snakes;
    }

    public List<Ladder> getLadders() {
        return ladders;
    }
}
