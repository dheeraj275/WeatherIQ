package weatheriq;

import java.util.ArrayList;
import java.util.List;

/**
 * AlertSystem - creates warning messages when readings cross dangerous limits.
 * Returns an EMPTY list when everything is normal.
 */
public class AlertSystem {

    public List<String> generateAlerts(WeatherData data) {
        if (data == null) {
            throw new IllegalArgumentException("WeatherData cannot be null.");
        }
        List<String> alerts = new ArrayList<>();

        // Temperature
        if (data.getTemperature() >= 40) {
            alerts.add("WARNING: Extreme heat (" + data.getTemperature() + " C). Risk of heat stroke.");
        } else if (data.getTemperature() >= 35) {
            alerts.add("ALERT: Heat alert (" + data.getTemperature() + " C).");
        }

        // Wind
        if (data.getWindSpeed() >= 60) {
            alerts.add("WARNING: Storm-level winds (" + data.getWindSpeed() + " km/h).");
        } else if (data.getWindSpeed() >= 40) {
            alerts.add("ALERT: Strong winds (" + data.getWindSpeed() + " km/h).");
        }

        // Rainfall
        if (data.getRainfall() >= 30) {
            alerts.add("WARNING: Violent rainfall (" + data.getRainfall() + " mm/h). Flooding possible.");
        } else if (data.getRainfall() >= 15) {
            alerts.add("ALERT: Heavy rainfall (" + data.getRainfall() + " mm/h).");
        }

        // UV
        if (data.getUvIndex() >= 11) {
            alerts.add("WARNING: Extreme UV index (" + data.getUvIndex() + ").");
        } else if (data.getUvIndex() >= 8) {
            alerts.add("ALERT: Very high UV index (" + data.getUvIndex() + ").");
        }

        // Air quality
        if (data.getAirQuality() >= 300) {
            alerts.add("WARNING: Hazardous air quality (AQI " + data.getAirQuality() + ").");
        } else if (data.getAirQuality() >= 150) {
            alerts.add("ALERT: Unhealthy air quality (AQI " + data.getAirQuality() + ").");
        }

        // Pressure
        if (data.getPressure() < 1000) {
            alerts.add("ALERT: Low pressure (" + data.getPressure() + " hPa). A storm may be approaching.");
        }

        // Humidity
        if (data.getHumidity() >= 90) {
            alerts.add("ALERT: Very high humidity (" + data.getHumidity() + "%).");
        }

        return alerts;
    }
}
