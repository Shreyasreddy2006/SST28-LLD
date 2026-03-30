package com.example.elevator;

public class App {
    public static void main(String[] args) {
        ElevatorSystem system = new ElevatorSystem(10);

        Elevator e1 = system.addElevator(700);
        Elevator e2 = system.addElevator(500);
        Elevator e3 = system.addElevator(800);

        System.out.println("\n=== Scenario 1: Outside button calls ===");
        system.getFloor(5).getOutsideButton().pressUp();
        system.getFloor(3).getOutsideButton().pressDown();
        system.getController().printStatus();

        System.out.println("\n=== Stepping elevators ===");
        for (int i = 0; i < 6; i++) {
            system.getController().stepAll();
        }
        system.getController().printStatus();

        System.out.println("\n=== Scenario 2: Inside panel — passenger selects floor ===");
        e1.getInsidePanel().pressFloor(8);
        for (int i = 0; i < 4; i++) {
            system.getController().stepAll();
        }
        system.getController().printStatus();

        System.out.println("\n=== Scenario 3: Overweight ===");
        e2.setCurrentWeight(600);
        e2.getInsidePanel().pressFloor(7);
        e2.setCurrentWeight(400);
        e2.closeDoor();
        e2.getInsidePanel().pressFloor(7);
        system.getController().printStatus();

        System.out.println("\n=== Scenario 4: Emergency stop ===");
        e3.addDestination(9);
        system.getController().stepAll();
        system.getController().stepAll();
        e3.getInsidePanel().pressEmergency();
        system.getController().printStatus();

        System.out.println("\n=== Scenario 5: Maintenance mode ===");
        e1.setMaintenance(true);
        system.getFloor(7).getOutsideButton().pressUp();
        system.getController().printStatus();
        e1.setMaintenance(false);

        System.out.println("\n=== Scenario 6: Alarm button ===");
        e2.getInsidePanel().pressAlarm();

        System.out.println("\n=== Scenario 7: Adding a new floor ===");
        system.addFloor();
        system.getFloor(10).getOutsideButton().pressUp();
        system.getController().printStatus();

        System.out.println("\n=== Scenario 8: Open / Close door ===");
        e3.getInsidePanel().pressOpen();
        e3.getInsidePanel().pressClose();

        System.out.println("\n=== Final Status ===");
        system.getController().printStatus();
    }
}
