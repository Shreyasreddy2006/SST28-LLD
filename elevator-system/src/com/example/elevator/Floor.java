package com.example.elevator;

public class Floor {
    private final int floorNumber;
    private final OutsideButton outsideButton;

    public  Floor(int floorNumber, ElevatorController controller) {
        this.floorNumber = floorNumber;
        this.outsideButton = new OutsideButton(floorNumber, controller);
    }

    public int getFloorNumber() { return floorNumber; }
    public OutsideButton getOutsideButton() { return outsideButton; }
}
