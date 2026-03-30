package com.example.elevator;

import java.util.TreeSet;

public class Elevator {
    private final int id;
    private final double weightLimitKg;
    private int currentFloor;
    private double currentWeightKg;
    private ElevatorState state;
    private DoorState doorState;
    private final TreeSet<Integer> destinations;
    private final InsidePanel insidePanel;
    private int minFloor;
    private int maxFloor;

    public Elevator(int id, double weightLimitKg, int minFloor, int maxFloor) {
        this.id = id;
        this.weightLimitKg = weightLimitKg;
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
        this.currentFloor = minFloor;
        this.currentWeightKg = 0;
        this.state = ElevatorState.IDLE;
        this.doorState = DoorState.CLOSED;
        this.destinations = new TreeSet<>();
        this.insidePanel = new InsidePanel(this);
    }

    // --- Getters ---

    public int getId() { return id; }
    public int getCurrentFloor() { return currentFloor; }
    public ElevatorState getState() { return state; }
    public DoorState getDoorState() { return doorState; }
    public double getWeightLimitKg() { return weightLimitKg; }
    public double getCurrentWeightKg() { return currentWeightKg; }
    public InsidePanel getInsidePanel() { return insidePanel; }
    public TreeSet<Integer> getDestinations() { return destinations; }

    // --- Weight management ---

    public void setCurrentWeight(double weightKg) {
        this.currentWeightKg = weightKg;
        if (currentWeightKg > weightLimitKg) {
            System.out.println("[Elevator " + id + "] OVERWEIGHT! " + currentWeightKg
                    + "kg exceeds limit of " + weightLimitKg + "kg");
            openDoor();
            triggerAlarm();
            // elevator will not move until weight is reduced
        }
    }

    public boolean isOverweight() {
        return currentWeightKg > weightLimitKg;
    }

    // --- Door controls ---

    public void openDoor() {
        doorState = DoorState.OPEN;
        System.out.println("[Elevator " + id + "] Door OPENED on floor " + currentFloor);
    }

    public void closeDoor() {
        if (isOverweight()) {
            System.out.println("[Elevator " + id + "] Cannot close door — overweight!");
            return;
        }
        doorState = DoorState.CLOSED;
        System.out.println("[Elevator " + id + "] Door CLOSED on floor " + currentFloor);
    }

    // --- Emergency / Alarm ---

    public void triggerEmergency() {
        state = ElevatorState.IDLE;
        openDoor();
        destinations.clear();
        System.out.println("[Elevator " + id + "] *** EMERGENCY STOP *** on floor " + currentFloor);
    }

    public void triggerAlarm() {
        System.out.println("[Elevator " + id + "] *** ALARM RINGING ***");
    }

    // --- Maintenance ---

    public void setMaintenance(boolean on) {
        if (on) {
            state = ElevatorState.MAINTENANCE;
            destinations.clear();
            System.out.println("[Elevator " + id + "] Entered MAINTENANCE mode");
        } else {
            state = ElevatorState.IDLE;
            System.out.println("[Elevator " + id + "] Exited MAINTENANCE mode — now IDLE");
        }
    }

    public boolean isAvailable() {
        return state != ElevatorState.MAINTENANCE && !isOverweight();
    }

    // --- Destination & movement ---

    public void addDestination(int floor) {
        if (state == ElevatorState.MAINTENANCE) {
            System.out.println("[Elevator " + id + "] Under maintenance — request ignored");
            return;
        }
        if (isOverweight()) {
            System.out.println("[Elevator " + id + "] Overweight — request ignored, reduce weight first");
            return;
        }
        if (floor < minFloor || floor > maxFloor) {
            System.out.println("[Elevator " + id + "] Invalid floor " + floor);
            return;
        }
        destinations.add(floor);
        System.out.println("[Elevator " + id + "] Added destination: floor " + floor);
    }

    public void step() {
        if (state == ElevatorState.MAINTENANCE || isOverweight()) return;
        if (destinations.isEmpty()) {
            if (state != ElevatorState.IDLE) {
                state = ElevatorState.IDLE;
                System.out.println("[Elevator " + id + "] Now IDLE at floor " + currentFloor);
            }
            return;
        }

        int nextFloor = determineNextFloor();

        if (nextFloor > currentFloor) {
            state = ElevatorState.MOVING_UP;
            currentFloor++;
            System.out.println("[Elevator " + id + "] Moving UP — now at floor " + currentFloor);
        } else if (nextFloor < currentFloor) {
            state = ElevatorState.MOVING_DOWN;
            currentFloor--;
            System.out.println("[Elevator " + id + "] Moving DOWN — now at floor " + currentFloor);
        }

        if (destinations.contains(currentFloor)) {
            destinations.remove(currentFloor);
            openDoor();
            System.out.println("[Elevator " + id + "] Arrived at floor " + currentFloor);
            closeDoor();
        }
    }

    private int determineNextFloor() {
        if (state == ElevatorState.MOVING_UP || state == ElevatorState.IDLE) {
            Integer higher = destinations.ceiling(currentFloor);
            if (higher != null) return higher;
            return destinations.first();
        } else {
            Integer lower = destinations.floor(currentFloor);
            if (lower != null) return lower;
            return destinations.last();
        }
    }

    public void updateFloorRange(int minFloor, int maxFloor) {
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
    }

    @Override
    public String toString() {
        return "Elevator{id=" + id + ", floor=" + currentFloor + ", state=" + state
                + ", door=" + doorState + ", weight=" + currentWeightKg + "/" + weightLimitKg + "kg"
                + ", destinations=" + destinations + "}";
    }
}
