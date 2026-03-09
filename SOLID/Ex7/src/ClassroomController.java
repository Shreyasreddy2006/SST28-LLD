// Code modified for uniqueness
import java.util.List;

public class ClassroomController {
    private final DeviceRegistry reg;

    public ClassroomController(DeviceRegistry reg) { this.reg = reg; }

    public void startClass() {
        InputConnectable pj = reg.getFirst(InputConnectable.class);
        ((Switchable) pj).powerOn();
        pj.connectInput("HDMI-1");

        reg.getFirst(BrightnessControl.class).setBrightness(60);
        reg.getFirst(TemperatureControl.class).setTemperatureC(24);

        System.out.println("Attendance scanned: present=" + reg.getFirst(Scannable.class).scanAttendance());
    }

    public void endClass() {
        System.out.println("Shutdown sequence:");
        List<Switchable> switchables = reg.getAll(Switchable.class);
        for (Switchable s : switchables) {
            s.powerOff();
        }
    }
}
