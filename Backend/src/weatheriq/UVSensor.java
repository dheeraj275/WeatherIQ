package weatheriq;

/** Simulates the UV index (0 = no UV, 11+ = extreme). */
public class UVSensor extends Sensor {

    public static final String NAME = "uv";

    public UVSensor() {
        this(5.0);
    }

    public UVSensor(double startValue) {
        super(NAME, "index", 0.0, 12.0, startValue);
    }

    @Override
    protected double generateReading() {
        return nextValue(0.6);
    }

    @Override
    public String getStatus() {
        double uv = getCurrentValue();
        if (uv < 3) return "Low";
        if (uv < 6) return "Moderate";
        if (uv < 8) return "High";
        if (uv < 11) return "Very High";
        return "Extreme";
    }
}
