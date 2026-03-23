package com.example.pen;

public class Pen {
    private String inkColor;
    private PenType type;
    private boolean opened;
    private WriteBehavior writeBehavior;
    private RefillBehavior refillBehavior;
    private OpenCloseBehavior openCloseBehavior;

    public Pen(PenType type, String inkColor, WriteBehavior writeBehavior,
               RefillBehavior refillBehavior, OpenCloseBehavior openCloseBehavior) {
        this.type = type;
        this.inkColor = inkColor;
        this.writeBehavior = writeBehavior;
        this.refillBehavior = refillBehavior;
        this.openCloseBehavior = openCloseBehavior;
        this.opened = false;
    }

    public String open() {
        String result = openCloseBehavior.open();
        this.opened = true;
        return result;
    }

    public String close() {
        String result = openCloseBehavior.close();
        this.opened = false;
        return result;
    }

    public String write() {
        if (!opened) {
            throw new IllegalStateException("Pen is closed. Call open() before writing.");
        }
        return "[" + inkColor.toUpperCase() + "] " + writeBehavior.write();
    }

    public String refill(String newColor) {
        String result = refillBehavior.refill();
        this.inkColor = newColor;
        return result + " Now loaded with " + newColor + " ink.";
    }

    public boolean isOpened() {
        return opened;
    }

    public String getInkColor() {
        return inkColor;
    }

    public PenType getType() {
        return type;
    }
}
