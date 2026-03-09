// Code modified for uniqueness
public class LightsPanel implements Switchable, BrightnessControl {
    @Override public void powerOn() { }
    @Override public void powerOff() { System.out.println("Lights OFF"); }
    @Override public void setBrightness(int pct) { System.out.println("Lights set to " + pct + "%"); }
}
