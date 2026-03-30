package com.example.elevator;

import java.util.ArrayList;
import java.util.List;

public class ElevatorSystem {
    private final List<Elevator> elevators;
    private final List<Floor> floors;
    private final ElevatorController controller;
    private int totalFloors;

    public ElevatorSystem(int totalFloors) {
        this.totalFloors = totalFloors;
        this.elevators = new ArrayList<>();
        this.controller = new ElevatorController(elevators);
        this.floors = new ArrayList<>();

        for (int i = 0; i < totalFloors; i++) {
            floors.add(new Floor(i, controller));
        }
    }

    public Elevator addElevator(double weightLimitKg) {
        int id = elevators.size() + 1;
        Elevator elevator = new Elevator(id, weightLimitKg, 0, totalFloors - 1);
        elevators.add(elevator);
        System.out.println("[System] Added Elevator " + id + " (limit: " + weightLimitKg + "kg)");
        return elevator;
    }

    public void addFloor() {
        int newFloorNum = totalFloors;
        totalFloors++;
        floors.add(new Floor(newFloorNum, controller));
        for (Elevator e : elevators) {
            e.updateFloorRange(0, totalFloors - 1);
        }
        System.out.println("[System] Added floor " + newFloorNum + " — total floors: " + totalFloors);
    }

    public Floor getFloor(int floorNumber) {
        return floors.get(floorNumber);
    }

    public Elevator getElevator(int index) {
        return elevators.get(index);
    }

    public ElevatorController getController() { return controller; }
    public List<Elevator> getElevators() { return elevators; }
    public List<Floor> getFloors() { return floors; }
    public int getTotalFloors() { return totalFloors; }
}
