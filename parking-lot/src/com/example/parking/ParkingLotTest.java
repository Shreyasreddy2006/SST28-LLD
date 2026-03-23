package com.example.parking;

import java.time.LocalDateTime;
import java.util.Map;

public class ParkingLotTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        testBikeGetsSmallSlot();
        testCarGetsMediumSlot();
        testBusGetsLargeSlot();
        testBikeGoesToMediumWhenSmallIsFull();
        testNearestSlotIsPickedFirst();
        testBillingIsBasedOnSlotTypeNotVehicle();
        testStatusShowsCorrectAvailability();
        testExitFreesUpSlot();
        testNoSlotAvailableThrowsError();

        System.out.println("\n========== RESULTS ==========");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("Total:  " + (passed + failed));

        if (failed > 0) {
            System.exit(1);
        }
    }

    private static ParkingLot buildLot() {
        BillingService billing = new BillingService();
        billing.setRate(SlotType.SMALL, 10.0);
        billing.setRate(SlotType.MEDIUM, 20.0);
        billing.setRate(SlotType.LARGE, 30.0);

        NearestSlotStrategy strategy = new NearestSlotStrategy();
        ParkingLot lot = new ParkingLot(strategy, billing);

        lot.addSlot(new ParkingSlot(1, SlotType.SMALL));
        lot.addSlot(new ParkingSlot(2, SlotType.SMALL));
        lot.addSlot(new ParkingSlot(3, SlotType.MEDIUM));
        lot.addSlot(new ParkingSlot(4, SlotType.MEDIUM));
        lot.addSlot(new ParkingSlot(5, SlotType.LARGE));
        lot.addSlot(new ParkingSlot(6, SlotType.LARGE));

        EntryGate gateA = new EntryGate("A");
        gateA.setDistanceToSlot(1, 1);
        gateA.setDistanceToSlot(2, 2);
        gateA.setDistanceToSlot(3, 3);
        gateA.setDistanceToSlot(4, 4);
        gateA.setDistanceToSlot(5, 5);
        gateA.setDistanceToSlot(6, 6);

        EntryGate gateB = new EntryGate("B");
        gateB.setDistanceToSlot(1, 6);
        gateB.setDistanceToSlot(2, 5);
        gateB.setDistanceToSlot(3, 4);
        gateB.setDistanceToSlot(4, 3);
        gateB.setDistanceToSlot(5, 2);
        gateB.setDistanceToSlot(6, 1);

        lot.addGate(gateA);
        lot.addGate(gateB);

        return lot;
    }

    private static void testBikeGetsSmallSlot() {
        ParkingLot lot = buildLot();
        Vehicle bike = new Vehicle("KA-01-1234", VehicleType.TWO_WHEELER);
        ParkingTicket ticket = lot.park(bike, LocalDateTime.now(), "A");

        check("Bike gets SMALL slot", ticket.getSlotType() == SlotType.SMALL);
    }

    private static void testCarGetsMediumSlot() {
        ParkingLot lot = buildLot();
        Vehicle car = new Vehicle("KA-02-5678", VehicleType.CAR);
        ParkingTicket ticket = lot.park(car, LocalDateTime.now(), "A");

        check("Car gets MEDIUM slot", ticket.getSlotType() == SlotType.MEDIUM);
    }

    private static void testBusGetsLargeSlot() {
        ParkingLot lot = buildLot();
        Vehicle bus = new Vehicle("KA-03-9999", VehicleType.BUS);
        ParkingTicket ticket = lot.park(bus, LocalDateTime.now(), "A");

        check("Bus gets LARGE slot", ticket.getSlotType() == SlotType.LARGE);
    }

    private static void testBikeGoesToMediumWhenSmallIsFull() {
        ParkingLot lot = buildLot();
        Vehicle bike1 = new Vehicle("KA-01-0001", VehicleType.TWO_WHEELER);
        Vehicle bike2 = new Vehicle("KA-01-0002", VehicleType.TWO_WHEELER);
        Vehicle bike3 = new Vehicle("KA-01-0003", VehicleType.TWO_WHEELER);

        lot.park(bike1, LocalDateTime.now(), "A");
        lot.park(bike2, LocalDateTime.now(), "A");
        ParkingTicket ticket3 = lot.park(bike3, LocalDateTime.now(), "A");

        check("Bike overflows to MEDIUM when SMALL is full", ticket3.getSlotType() == SlotType.MEDIUM);
    }

    private static void testNearestSlotIsPickedFirst() {
        ParkingLot lot = buildLot();
        Vehicle bike = new Vehicle("KA-01-1111", VehicleType.TWO_WHEELER);

        ParkingTicket ticketFromA = lot.park(bike, LocalDateTime.now(), "A");
        check("From gate A, nearest small slot is #1", ticketFromA.getSlotNumber() == 1);

        ParkingLot lot2 = buildLot();
        Vehicle car = new Vehicle("KA-02-2222", VehicleType.CAR);
        ParkingTicket ticketFromB = lot2.park(car, LocalDateTime.now(), "B");
        check("From gate B, nearest compatible slot for car is #6 (large, distance 1)", ticketFromB.getSlotNumber() == 6);
    }

    private static void testBillingIsBasedOnSlotTypeNotVehicle() {
        ParkingLot lot = buildLot();

        Vehicle bike1 = new Vehicle("KA-01-0001", VehicleType.TWO_WHEELER);
        Vehicle bike2 = new Vehicle("KA-01-0002", VehicleType.TWO_WHEELER);
        Vehicle bike3 = new Vehicle("KA-01-0003", VehicleType.TWO_WHEELER);

        LocalDateTime entry = LocalDateTime.of(2026, 3, 23, 10, 0);
        LocalDateTime exit = LocalDateTime.of(2026, 3, 23, 12, 0);

        lot.park(bike1, entry, "A");
        lot.park(bike2, entry, "A");
        ParkingTicket overflowTicket = lot.park(bike3, entry, "A");

        double bill = lot.exit(overflowTicket.getTicketId(), exit);

        check("Bike in MEDIUM slot billed at MEDIUM rate (2hrs * 20 = 40)", bill == 40.0);
    }

    private static void testStatusShowsCorrectAvailability() {
        ParkingLot lot = buildLot();

        Vehicle car = new Vehicle("KA-02-1111", VehicleType.CAR);
        lot.park(car, LocalDateTime.now(), "A");

        Map<SlotType, int[]> availability = lot.status();

        check("SMALL: 2 total, 2 available", availability.get(SlotType.SMALL)[0] == 2 && availability.get(SlotType.SMALL)[1] == 2);
        check("MEDIUM: 2 total, 1 available", availability.get(SlotType.MEDIUM)[0] == 2 && availability.get(SlotType.MEDIUM)[1] == 1);
        check("LARGE: 2 total, 2 available", availability.get(SlotType.LARGE)[0] == 2 && availability.get(SlotType.LARGE)[1] == 2);
    }

    private static void testExitFreesUpSlot() {
        ParkingLot lot = buildLot();
        Vehicle car = new Vehicle("KA-02-3333", VehicleType.CAR);

        LocalDateTime entry = LocalDateTime.of(2026, 3, 23, 10, 0);
        ParkingTicket ticket = lot.park(car, entry, "A");

        Map<SlotType, int[]> before = lot.status();
        check("Before exit: 1 medium available", before.get(SlotType.MEDIUM)[1] == 1);

        lot.exit(ticket.getTicketId(), LocalDateTime.of(2026, 3, 23, 13, 0));

        Map<SlotType, int[]> after = lot.status();
        check("After exit: 2 medium available", after.get(SlotType.MEDIUM)[1] == 2);
    }

    private static void testNoSlotAvailableThrowsError() {
        ParkingLot lot = buildLot();

        lot.park(new Vehicle("BUS-1", VehicleType.BUS), LocalDateTime.now(), "A");
        lot.park(new Vehicle("BUS-2", VehicleType.BUS), LocalDateTime.now(), "A");

        try {
            lot.park(new Vehicle("BUS-3", VehicleType.BUS), LocalDateTime.now(), "A");
            check("Should throw when no large slots left", false);
        } catch (RuntimeException e) {
            check("Throws error when no large slots left", true);
        }
    }

    private static void check(String testName, boolean condition) {
        if (condition) {
            System.out.println("PASS: " + testName);
            passed++;
        } else {
            System.out.println("FAIL: " + testName);
            failed++;
        }
    }
}
