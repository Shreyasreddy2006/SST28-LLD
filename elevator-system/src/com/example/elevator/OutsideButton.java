package com.example.elevator;

public class OutsideButton {
    private final int floor;
    private final ElevatorController controller;

    public OutsideButton(int floor, ElevatorController controller) {
        this.floor = floor;
        this.controller = controller;
    }

    public void pressUp() {
        System.out.println("[Floor " + floor + "] UP button pressed");
        controller.requestElevator(floor, Direction.UP);
    }

    public void pressDown() {
        System.out.println("[Floor " + floor + "] DOWN button pressed");
        controller.requestElevator(floor, Direction.DOWN);
    }
}
