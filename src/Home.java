import java.util.ArrayList;
import java.util.List;

public class Home {
    private String name;
    private List<Device> devices = new ArrayList<>();

    public Home(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Device> getDevices() { return devices; }

    public void addDevice(Device device) { devices.add(device); }
    public void removeDevice(Device device) { devices.remove(device); }

    public double calculateTotalKwh() {
        double total = 0;
        for (Device d : devices) {
            total += (d.getWatts() * d.getHours() * 30) / 1000.0;
        }
        return total;
    }

    public double calculateMonthlyBill(double ratePerUnit) {
        return calculateTotalKwh() * ratePerUnit;
    }
}
