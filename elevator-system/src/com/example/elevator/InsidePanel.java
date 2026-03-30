package com.example.elevator;

public class InsidePanel {
    private final Elevator elevator;

    public InsidePanel(Elevator elevator) {
        this.elevator = elevator;
    }

    public void pressFloor(int floor) {
        System.out.println("[Elevator " + elevator.getId() + "] Floor " + floor + " button pressed inside");
        elevator.addDestination(floor);
    }

    public void pressOpen() {
        System.out.println("[Elevator " + elevator.getId() + "] OPEN button pressed");
        elevator.openDoor();
    }

    public void pressClose() {
        System.out.println("[Elevator " + elevator.getId() + "] CLOSE button pressed");
        elevator.closeDoor();
    }

    public void pressEmergency() {
        System.out.println("[Elevator " + elevator.getId() + "] EMERGENCY button pressed");
        elevator.triggerEmergency();
    }

    public void pressAlarm() {
        System.out.println("[Elevator " + elevator.getId() + "] ALARM button pressed");
        elevator.triggerAlarm();
    }
}
