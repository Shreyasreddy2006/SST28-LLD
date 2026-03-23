package com.example.pen;

public class ClickMechanism implements OpenCloseBehavior {
    @Override
    public String open() {
        return "Clicking to extend the tip.";
    }

    @Override
    public String close() {
        return "Clicking to retract the tip.";
    }
}
