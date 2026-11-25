package weatheriq;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * WeatherData - one snapshot of all seven weather readings.
 *
 * Encapsulation: fields are private and final (cannot change after creation).
 * The constructor validates every value so a WeatherData object is always valid.
 */
public class WeatherData {

    private final double temperature;   // degrees C
    private final double humidity;      // %
    private final double windSpeed;     // km/h
    private final double rainfall;      // mm/h
    private final double pressure;      // hPa
    private final double uvIndex;       // index
    private final double airQuality;    // AQI
    private final String timestamp;

    public WeatherData(double temperature, double humidity, double windSpeed, double rainfall,
                       double pressure, double uvIndex, double airQuality) {
        // Input validation
        check("Temperature", temperature, -90, 60);
        check("Humidity", humidity, 0, 100);
        check("Wind speed", windSpeed, 0, 400);
        check("Rainfall", rainfall, 0, 500);
        check("Pressure", pressure, 800, 1100);
        check("UV index", uvIndex, 0, 20);
        check("Air quality", airQuality, 0, 500);

        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.rainfall = rainfall;
        this.pressure = pressure;
        this.uvIndex = uvIndex;
        this.airQuality = airQuality;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private static void check(String label, double value, double min, double max) {
        if (Double.isNaN(value) || value < min || value > max) {
            throw new IllegalArgumentException(label + " value " + value
                    + " is invalid (allowed range " + min + " to " + max + ").");
        }
    }

    // Getters only - no setters, so the data cannot be changed.
    public double getTemperature() { return temperature; }
    public double getHumidity()    { return humidity; }
    public double getWindSpeed()   { return windSpeed; }
    public double getRainfall()    { return rainfall; }
    public double getPressure()    { return pressure; }
    public double getUvIndex()     { return uvIndex; }
    public double getAirQuality()  { return airQuality; }
    public String getTimestamp()   { return timestamp; }

    @Override
    public String toString() {
        return "WeatherData[temp=" + temperature + ", humidity=" + humidity + ", wind=" + windSpeed
                + ", rain=" + rainfall + ", pressure=" + pressure + ", uv=" + uvIndex
                + ", aqi=" + airQuality + ", time=" + timestamp + "]";
    }
}
