package weatheriq;

import java.util.ArrayList;
import java.util.List;

/**
 * RecommendationSystem - gives simple advice to people based on current weather.
 */
public class RecommendationSystem {

    public List<String> generateRecommendations(WeatherData data) {
        if (data == null) {
            throw new IllegalArgumentException("WeatherData cannot be null.");
        }
        List<String> tips = new ArrayList<>();

        if (data.getTemperature() >= 35) {
            tips.add("Stay hydrated and avoid outdoor activity between 12 PM and 4 PM.");
        } else if (data.getTemperature() <= 15) {
            tips.add("It is cold - wear warm clothing.");
        }

        if (data.getUvIndex() >= 6) {
            tips.add("Apply sunscreen (SPF 30+) and wear sunglasses outdoors.");
        }

        if (data.getRainfall() >= 30) {
            tips.add("Avoid travel through low-lying or flooded areas.");
        } else if (data.getRainfall() > 0) {
            tips.add("Carry an umbrella or raincoat.");
        }

        if (data.getWindSpeed() >= 40) {
            tips.add("Secure loose outdoor objects and avoid riding two-wheelers.");
        }

        if (data.getAirQuality() >= 150) {
            tips.add("Wear an N95 mask outdoors and keep windows closed.");
        } else if (data.getAirQuality() >= 100) {
            tips.add("People with asthma or breathing problems should limit outdoor exercise.");
        }

        if (data.getHumidity() >= 80) {
            tips.add("Humidity is high - keep indoor spaces ventilated.");
        } else if (data.getHumidity() <= 30) {
            tips.add("Air is dry - drink extra water.");
        }

        if (data.getPressure() < 1000) {
            tips.add("Pressure is low - weather may worsen, check the forecast.");
        }

        if (tips.isEmpty()) {
            tips.add("Conditions are comfortable. Enjoy your day!");
        }
        return tips;
    }
}
