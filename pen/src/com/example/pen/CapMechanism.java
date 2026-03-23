package com.example.pen;

public class CapMechanism implements OpenCloseBehavior {
    @Override
    public String open() {
         return "Removing the cap.";
    }

    @Override
    public String close() {
         return "Putting the cap back on.";
    }
}
