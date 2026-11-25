package weatheriq;

/** Simulates atmospheric pressure in hectopascals (hPa). */
public class PressureSensor extends Sensor {

    public static final String NAME = "pressure";

    public PressureSensor() {
        this(1012.0);
    }

    public PressureSensor(double startValue) {
        super(NAME, "hPa", 980.0, 1040.0, startValue);
    }

    @Override
    protected double generateReading() {
        return nextValue(1.5);
    }

    @Override
    public String getStatus() {
        double p = getCurrentValue();
        if (p < 1000) return "Low";
        if (p <= 1020) return "Normal";
        return "High";
    }
}
