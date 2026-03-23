package com.example.pen;

public class CartridgeRefill implements RefillBehavior {
    @Override
    public String refill() {
        return "Swapping out the ink cartridge.";
    }
}
