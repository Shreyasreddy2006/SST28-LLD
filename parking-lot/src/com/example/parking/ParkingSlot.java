package com.example.parking;

public class ParkingSlot {
    private int slotNumber;
    private SlotType type;
    private boolean occupied;

    public ParkingSlot(int slotNumber, SlotType type) {
        this.slotNumber = slotNumber;
        this.type = type;
        this.occupied = false;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public SlotType getType() {
        return type;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void occupy() {
        this.occupied = true;
    }

    public void free() {
        this.occupied = false;
    }

    @Override
    public String toString() {
        return "Slot #" + slotNumber + " (" + type + ")";
    }
}
