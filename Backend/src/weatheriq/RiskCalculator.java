package weatheriq;

/**
 * RiskCalculator - gives a weather risk score from 0 (safe) to 100 (very dangerous).
 *
 * Each parameter adds "risk points". The maximum points per parameter are:
 * temperature 15, humidity 10, wind 20, rain 20, pressure 10, UV 10, air quality 15
 * (total = 100).
 */
public class RiskCalculator {

    public int calculateRiskScore(WeatherData data) {
        if (data == null) {
            throw new IllegalArgumentException("WeatherData cannot be null.");
        }
        int score = 0;
        score += temperatureRisk(data.getTemperature());
        score += humidityRisk(data.getHumidity());
        score += windRisk(data.getWindSpeed());
        score += rainRisk(data.getRainfall());
        score += pressureRisk(data.getPressure());
        score += uvRisk(data.getUvIndex());
        score += airQualityRisk(data.getAirQuality());

        // Safety net: always keep the score between 0 and 100.
        return Math.max(0, Math.min(100, score));
    }

    /** Converts a numeric score into a word. */
    public String getRiskLevel(int score) {
        if (score < 25) return "Low";
        if (score < 50) return "Moderate";
        if (score < 75) return "High";
        return "Severe";
    }

    // ---------- Simple rules (one small method per parameter) ----------

    private int temperatureRisk(double t) {
        if (t >= 40) return 15;
        if (t >= 35) return 10;
        if (t >= 30) return 5;
        if (t <= 5)  return 8;
        return 0;
    }

    private int humidityRisk(double h) {
        if (h >= 90) return 10;
        if (h >= 80) return 6;
        if (h >= 70) return 3;
        return 0;
    }

    private int windRisk(double w) {
        if (w >= 60) return 20;
        if (w >= 40) return 14;
        if (w >= 25) return 7;
        return 0;
    }

    private int rainRisk(double r) {
        if (r >= 30) return 20;
        if (r >= 15) return 14;
        if (r >= 5)  return 7;
        if (r > 0)   return 2;
        return 0;
    }

    private int pressureRisk(double p) {
        if (p < 1000) return 10;
        if (p < 1010) return 5;
        return 0;
    }

    private int uvRisk(double uv) {
        if (uv >= 11) return 10;
        if (uv >= 8)  return 7;
        if (uv >= 6)  return 4;
        if (uv >= 3)  return 1;
        return 0;
    }

    private int airQualityRisk(double aqi) {
        if (aqi >= 300) return 15;
        if (aqi >= 200) return 12;
        if (aqi >= 150) return 9;
        if (aqi >= 100) return 5;
        if (aqi >= 50)  return 2;
        return 0;
    }
}
