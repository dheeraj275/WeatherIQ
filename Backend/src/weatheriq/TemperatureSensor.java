package weatheriq;

/** Simulates air temperature in degrees Celsius. */
public class TemperatureSensor extends Sensor {

    public static final String NAME = "temperature";

    // Constructor overloading: one default, one custom start value.
    public TemperatureSensor() {
        this(28.0);
    }

    public TemperatureSensor(double startValue) {
        super(NAME, "\u00B0C", 10.0, 48.0, startValue);
    }

    @Override
    protected double generateReading() {
        return nextValue(1.5);
    }

    @Override
    public String getStatus() {
        double t = getCurrentValue();
        if (t < 15) return "Cold";
        if (t <= 25) return "Comfortable";
        if (t <= 35) return "Warm";
        if (t < 40) return "Hot";
        return "Extreme Heat";
    }
}
