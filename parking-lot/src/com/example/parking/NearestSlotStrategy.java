package com.example.parking;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class NearestSlotStrategy implements SlotAssignmentStrategy {

    @Override
    public ParkingSlot findSlot(List<ParkingSlot> allSlots, VehicleType vehicleType, EntryGate gate) {
        List<SlotType> compatibleTypes = getCompatibleSlotTypes(vehicleType);

        ParkingSlot nearest = null;
        int shortestDistance = Integer.MAX_VALUE;

        for (ParkingSlot slot : allSlots) {
            if (slot.isOccupied()) {
                continue;
            }
            if (!compatibleTypes.contains(slot.getType())) {
                continue;
            }
            int distance = gate.getDistanceToSlot(slot.getSlotNumber());
            if (distance < shortestDistance) {
                shortestDistance = distance;
                nearest = slot;
            }
        }

        return nearest;
    }

    private List<SlotType> getCompatibleSlotTypes(VehicleType vehicleType) {
        switch (vehicleType) {
            case TWO_WHEELER:
                return Arrays.asList(SlotType.SMALL, SlotType.MEDIUM, SlotType.LARGE);
            case CAR:
                return Arrays.asList(SlotType.MEDIUM, SlotType.LARGE);
            case BUS:
                return Arrays.asList(SlotType.LARGE);
            default:
                return new ArrayList<>();
        }
    }
}
