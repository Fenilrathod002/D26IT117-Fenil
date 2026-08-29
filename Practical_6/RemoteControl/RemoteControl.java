package RemoteControl;
interface Switchable {
    void on();
    void off();
    default void toggle() {
        System.out.println("Toggle operation");
    }
}
class Fan implements Switchable {
    boolean isOn = false;
    @Override
    public void on() {
        isOn = true;
        System.out.println("Fan is ON.");
    }
    @Override
    public void off() {
        isOn = false;
        System.out.println("Fan is OFF.");
    }
    @Override
    public void toggle() {
        if (isOn) {
            off();
        } else {
            on();
        }
    }
}
class Light implements Switchable {
    boolean isOn = false;
    @Override
    public void on() {
        isOn = true;
        System.out.println("Light is ON.");
    }
    @Override
    public void off() {
        isOn = false;
        System.out.println("Light is OFF.");
    }
    @Override
    public void toggle() {
        if (isOn) {
            off();
        } else {
            on();
        }
    }
}
@FunctionalInterface
interface SwitchPermission {
    boolean maySwitchOn(Switchable device, int hour);
}
public class RemoteControl {
    public static void main(String[] args) {
        // Array of Switchable devices
        Switchable[] devices = {
            new Fan(),
            new Light()
        };
        // Toggle each device
        for (Switchable device : devices) {
            device.toggle();
        }
        // Anonymous class
        SwitchPermission permission1 = new SwitchPermission() {
            @Override
            public boolean maySwitchOn(Switchable device, int hour) {
                return hour >= 6 && hour <= 24;
            }
        };
        System.out.println(
            "Anonymous class: " +
            permission1.maySwitchOn(devices[1], 18)
        );
        // Lambda expression
        SwitchPermission permission2 =
            (device, hour) -> hour >= 6 && hour <= 22;
        System.out.println(
            "Lambda: " +
            permission2.maySwitchOn(devices[0], 6)
        );
    }
}