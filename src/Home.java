import java.util.ArrayList;
import java.util.List;

public class Home {
    private final int id;              // DB id (or -1 if not from DB)
    private final String name;
    private final List<Device> devices = new ArrayList<>();

    public Home(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // helper constructor if you want id-less (not used here)
    public Home(String name) {
        this(-1, name);
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public List<Device> getDevices() { return devices; }
    public void setDevices(List<Device> devicesList) {
        devices.clear();
        devices.addAll(devicesList);
    }

    public void addDevice(Device d) { devices.add(d); }

    @Override
    public String toString() {
        return name;
    }
}
