package weatheriq;

/** Simulates the Air Quality Index (AQI). Always a whole number. */
public class AirQualitySensor extends Sensor {

    public static final String NAME = "airQuality";

    public AirQualitySensor() {
        this(80.0);
    }

    public AirQualitySensor(double startValue) {
        super(NAME, "AQI", 0.0, 400.0, startValue);
    }

    @Override
    protected double generateReading() {
        // AQI is shown as a whole number, so round it.
        return Math.round(nextValue(20.0));
    }

    @Override
    public String getStatus() {
        double aqi = getCurrentValue();
        if (aqi <= 50) return "Good";
        if (aqi <= 100) return "Moderate";
        if (aqi <= 150) return "Unhealthy for Sensitive Groups";
        if (aqi <= 200) return "Unhealthy";
        if (aqi <= 300) return "Very Unhealthy";
        return "Hazardous";
    }
}
