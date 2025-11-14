public class Device {
    private String name;
    private double watts;
    private double hoursPerDay;

    public Device(String name, double watts, double hoursPerDay) {
        this.name = name;
        this.watts = watts;
        this.hoursPerDay = hoursPerDay;
    }

    public String getName() {
        return name;
    }

    public double getMonthlyBill() {
        double kWh = (watts * hoursPerDay * 30) / 1000.0;
        double rate = 30; // PKR per kWh
        return kWh * rate;
    }
}
