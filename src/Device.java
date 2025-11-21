public class Device {
    private final int id;
    private final int homeId;
    private final String name;
    private final double watts;
    private final double hoursPerDay;

    public Device(int id, int homeId, String name, double watts, double hoursPerDay) {
        this.id = id;
        this.homeId = homeId;
        this.name = name;
        this.watts = watts;
        this.hoursPerDay = hoursPerDay;
    }

    public Device(int homeId, String name, double watts, double hoursPerDay) {
        this(-1, homeId, name, watts, hoursPerDay);
    }

    public int getId() { return id; }
    public int getHomeId() { return homeId; }
    public String getName() { return name; }
    public double getWatts() { return watts; }
    public double getHoursPerDay() { return hoursPerDay; }

    public double monthlyKwh() {
        return (watts * hoursPerDay * 30.0) / 1000.0;
    }

    public double monthlyCost(double ratePerUnit) {
        return monthlyKwh() * ratePerUnit;
    }

    @Override
    public String toString() {
        return name + " (" + watts + "W, " + hoursPerDay + "h/day)";
    }
}
