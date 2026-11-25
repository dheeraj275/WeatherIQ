package weatheriq;

/**
 * WeatherAnalyzer - looks at a WeatherData snapshot and describes the overall weather.
 */
public class WeatherAnalyzer {

    /** Returns a one-phrase summary such as "Stormy", "Rainy" or "Hot and Humid". */
    public String getOverallCondition(WeatherData data) {
        if (data == null) {
            throw new IllegalArgumentException("WeatherData cannot be null.");
        }
        if (data.getWindSpeed() >= 60 || data.getRainfall() >= 30) {
            return "Stormy";
        }
        if (data.getRainfall() >= 10) {
            return "Rainy";
        }
        if (data.getRainfall() > 0) {
            return "Light Rain";
        }
        if (data.getTemperature() >= 35 && data.getHumidity() >= 60) {
            return "Hot and Humid";
        }
        if (data.getTemperature() >= 35) {
            return "Hot";
        }
        if (data.getAirQuality() > 150) {
            return "Polluted";
        }
        if (data.getHumidity() >= 85) {
            return "Humid and Cloudy";
        }
        if (data.getUvIndex() >= 6) {
            return "Sunny";
        }
        return "Clear and Pleasant";
    }

    /** Simple rule: low pressure + high humidity means rain is likely soon. */
    public boolean isRainLikely(WeatherData data) {
        if (data == null) {
            throw new IllegalArgumentException("WeatherData cannot be null.");
        }
        return data.getPressure() < 1005 && data.getHumidity() >= 80;
    }
}
