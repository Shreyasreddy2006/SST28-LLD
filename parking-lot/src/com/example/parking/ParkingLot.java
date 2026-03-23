package com.example.parking;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParkingLot {
    private List<ParkingSlot> slots;
    private Map<String, EntryGate> gates;
    private Map<String, ParkingTicket> activeTickets;
    private SlotAssignmentStrategy assignmentStrategy;
    private BillingService billingService;
    private int ticketCounter;

    public ParkingLot(SlotAssignmentStrategy strategy, BillingService billingService) {
        this.slots = new ArrayList<>();
        this.gates = new HashMap<>();
        this.activeTickets = new HashMap<>();
        this.assignmentStrategy = strategy;
        this.billingService = billingService;
        this.ticketCounter = 0;
    }

    public void addSlot(ParkingSlot slot) {
        slots.add(slot);
    }

    public void addGate(EntryGate gate) {
        gates.put(gate.getGateId(), gate);
    }

    public ParkingTicket park(Vehicle vehicle, LocalDateTime entryTime, String entryGateId) {
        EntryGate gate = gates.get(entryGateId);
        if (gate == null) {
            throw new RuntimeException("No such gate: " + entryGateId);
        }

        ParkingSlot slot = assignmentStrategy.findSlot(slots, vehicle.getType(), gate);
        if (slot == null) {
            throw new RuntimeException("No available slot for " + vehicle);
        }

        slot.occupy();
        ticketCounter++;
        String ticketId = "TKT-" + ticketCounter;

        ParkingTicket ticket = new ParkingTicket(ticketId, vehicle, slot.getSlotNumber(), slot.getType(), entryTime);
        activeTickets.put(ticketId, ticket);

        return ticket;
    }

    public Map<SlotType, int[]> status() {
        Map<SlotType, int[]> availability = new HashMap<>();
        for (SlotType type : SlotType.values()) {
            availability.put(type, new int[]{0, 0});
        }

        for (ParkingSlot slot : slots) {
            int[] counts = availability.get(slot.getType());
            counts[0]++;
            if (!slot.isOccupied()) {
                counts[1]++;
            }
        }

        return availability;
    }

    public double exit(String ticketId, LocalDateTime exitTime) {
        ParkingTicket ticket = activeTickets.get(ticketId);
        if (ticket == null) {
            throw new RuntimeException("Invalid ticket: " + ticketId);
        }

        double bill = billingService.calculateBill(ticket, exitTime);

        for (ParkingSlot slot : slots) {
            if (slot.getSlotNumber() == ticket.getSlotNumber()) {
                slot.free();
                break;
            }
        }

        activeTickets.remove(ticketId);
        return bill;
    }
}
