package com.example.parking;

import java.util.List;

public interface SlotAssignmentStrategy {
    ParkingSlot findSlot(List<ParkingSlot> allSlots, VehicleType vehicleType, EntryGate gate);
}
