// Code modified for uniqueness
public class AirConditioner implements Switchable, TemperatureControl {
    @Override public void powerOn() { }
    @Override public void powerOff() { System.out.println("AC OFF"); }
    @Override public void setTemperatureC(int c) { System.out.println("AC set to " + c + "C"); }
}
