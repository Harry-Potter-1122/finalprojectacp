public class Device {
    private String name;
    private double watts;
    private double hours;

    public Device(String name, double watts, double hours) {
        this.name = name;
        this.watts = watts;
        this.hours = hours;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getWatts() { return watts; }
    public void setWatts(double watts) { this.watts = watts; }

    public double getHours() { return hours; }
    public void setHours(double hours) { this.hours = hours; }

    public double monthlyKwh() {
        return (watts * hours * 30) / 1000.0;
    }
}
