package com.example.elevator;

import java.util.List;

public class ElevatorController {
    private final List<Elevator> elevators;

    public ElevatorController(List<Elevator> elevators) {
        this.elevators = elevators;
    }

    public Elevator requestElevator(int floor, Direction direction) {
        Elevator best = findBestElevator(floor, direction);
        if (best == null) {
            System.out.println("[Controller] No available elevator for floor " + floor);
            return null;
        }
        best.addDestination(floor);
        System.out.println("[Controller] Assigned Elevator " + best.getId() + " to floor " + floor + " (" + direction + ")");
        return best;
    }

    private Elevator findBestElevator(int floor, Direction direction) {
        Elevator best = null;
        int bestDistance = Integer.MAX_VALUE;

        for (Elevator e : elevators) {
            if (!e.isAvailable()) continue;

            int distance = Math.abs(e.getCurrentFloor() - floor);

            if (e.getState() == ElevatorState.IDLE) {
                if (distance < bestDistance) {
                    bestDistance = distance;
                    best = e;
                }
                continue;
            }

            boolean movingTowards = false;
            if (direction == Direction.UP && e.getState() == ElevatorState.MOVING_UP && e.getCurrentFloor() <= floor) {
                movingTowards = true;
            }
            if (direction == Direction.DOWN && e.getState() == ElevatorState.MOVING_DOWN && e.getCurrentFloor() >= floor) {
                movingTowards = true;
            }

            if (movingTowards && distance < bestDistance) {
                bestDistance = distance;
                best = e;
            }
        }

        if (best == null) {
            for (Elevator e : elevators) {
                if (!e.isAvailable()) continue;
                int distance = Math.abs(e.getCurrentFloor() - floor);
                if (distance < bestDistance) {
                    bestDistance = distance;
                    best = e;
                }
            }
        }

        return best;
    }

    public void stepAll() {
        for (Elevator e : elevators) {
            e.step();
        }
    }

    public void printStatus() {
        System.out.println("--- Elevator Status ---");
        for (Elevator e : elevators) {
            System.out.println("  " + e);
        }
        System.out.println("-----------------------");
    }
}
