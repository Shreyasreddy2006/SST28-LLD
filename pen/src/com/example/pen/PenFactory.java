package com.example.pen;

public class PenFactory {

    public static Pen create(PenType penType, String color, MechanismType mechanism) {
        WriteBehavior writeBehavior;
        RefillBehavior refillBehavior;

        switch (penType) {
            case BALLPOINT:
                writeBehavior = new BallpointWrite();
                refillBehavior = new CartridgeRefill();
                break;
            case GEL:
                writeBehavior = new GelWrite();
                refillBehavior = new CartridgeRefill();
                break;
            case FOUNTAIN:
                writeBehavior = new FountainWrite();
                refillBehavior = new BottleRefill();
                break;
            default:
                throw new IllegalArgumentException("Unknown pen type: " + penType);
        }

        OpenCloseBehavior openCloseBehavior;
        switch (mechanism) {
            case CAP:
                openCloseBehavior = new CapMechanism();
                break;
            case CLICK:
                openCloseBehavior = new ClickMechanism();
                break;
            default:
                throw new IllegalArgumentException("Unknown mechanism: " + mechanism);
        }

        return new Pen(penType, color, writeBehavior, refillBehavior, openCloseBehavior);
    }
}
