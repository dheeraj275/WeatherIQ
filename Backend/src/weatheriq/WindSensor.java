package weatheriq;

/** Simulates wind speed in km/h. */
public class WindSensor extends Sensor {

    public static final String NAME = "wind";

    public WindSensor() {
        this(12.0);
    }

    public WindSensor(double startValue) {
        super(NAME, "km/h", 0.0, 90.0, startValue);
    }

    @Override
    protected double generateReading() {
        return nextValue(6.0);
    }

    @Override
    public String getStatus() {
        double w = getCurrentValue();
        if (w < 12) return "Calm";
        if (w < 30) return "Breezy";
        if (w < 50) return "Windy";
        if (w < 70) return "Strong Wind";
        return "Storm";
    }
}
