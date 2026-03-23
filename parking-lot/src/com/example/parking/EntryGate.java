package com.example.parking;

import java.util.Map;
import java.util.HashMap;

public class EntryGate {
    private String gateId;
    private Map<Integer, Integer> distanceToSlot;

    public EntryGate(String gateId) {
         this.gateId = gateId;
         this.distanceToSlot = new HashMap<>();
    }

    public String getGateId() {
        return gateId;
    }

    public void setDistanceToSlot(int slotNumber, int distance) {
        distanceToSlot.put(slotNumber, distance);
    }

    public int getDistanceToSlot(int slotNumber) {
        return distanceToSlot.getOrDefault(slotNumber, Integer.MAX_VALUE);
    }
}
