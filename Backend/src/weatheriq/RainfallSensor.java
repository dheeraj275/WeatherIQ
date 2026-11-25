package weatheriq;

/** Simulates rainfall intensity in mm per hour. Often 0 (no rain). */
public class RainfallSensor extends Sensor {

    public static final String NAME = "rainfall";

    public RainfallSensor() {
        this(0.0);
    }

    public RainfallSensor(double startValue) {
        super(NAME, "mm/h", 0.0, 60.0, startValue);
    }

    @Override
    protected double generateReading() {
        // 40% of the time it is dry; otherwise rain changes a little.
        if (chance(0.4)) {
            return 0.0;
        }
        return nextValue(6.0);
    }

    @Override
    public String getStatus() {
        double r = getCurrentValue();
        if (r == 0) return "No Rain";
        if (r < 2.5) return "Light Rain";
        if (r < 10) return "Moderate Rain";
        if (r < 30) return "Heavy Rain";
        return "Violent Rain";
    }
}
