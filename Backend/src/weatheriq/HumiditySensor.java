package weatheriq;

/** Simulates relative humidity in percent. */
public class HumiditySensor extends Sensor {

    public static final String NAME = "humidity";

    public HumiditySensor() {
        this(55.0);
    }

    public HumiditySensor(double startValue) {
        super(NAME, "%", 20.0, 100.0, startValue);
    }

    @Override
    protected double generateReading() {
        return nextValue(4.0);
    }

    @Override
    public String getStatus() {
        double h = getCurrentValue();
        if (h < 30) return "Dry";
        if (h <= 60) return "Comfortable";
        if (h <= 80) return "Humid";
        return "Very Humid";
    }
}
