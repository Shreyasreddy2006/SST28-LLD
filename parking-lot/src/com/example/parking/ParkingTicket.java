package com.example.parking;

import java.time.LocalDateTime;

public class ParkingTicket {
    private String ticketId;
    private Vehicle vehicle;
    private int slotNumber;
    private SlotType slotType;
    private LocalDateTime entryTime;

    public ParkingTicket(String ticketId, Vehicle vehicle, int slotNumber, SlotType slotType, LocalDateTime entryTime) {
        this.ticketId = ticketId;
        this.vehicle = vehicle;
        this.slotNumber = slotNumber;
        this.slotType = slotType;
        this.entryTime = entryTime;
    }

    public String getTicketId() {
        return ticketId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public SlotType getSlotType() {
        return slotType;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    @Override
    public String toString() {
        return "Ticket[" + ticketId + "] " + vehicle + " -> Slot #" + slotNumber + " (" + slotType + ") at " + entryTime;
    }
}
