package weatheriq;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * WeatherStation - the "brain" of the project.
 *
 *  - Holds all sensors in an ArrayList<Sensor>   (polymorphism: one list, many sensor types)
 *  - Updates every sensor every 5 seconds
 *  - Uses the Analyzer, RiskCalculator, AlertSystem and RecommendationSystem (composition)
 *
 * Methods are 'synchronized' because two threads use this object:
 * the timer thread (updates data) and the HTTP server thread (reads data).
 */
public class WeatherStation {

    private static final int UPDATE_INTERVAL_SECONDS = 5;

    private final ArrayList<Sensor> sensors = new ArrayList<>();

    private final WeatherAnalyzer analyzer = new WeatherAnalyzer();
    private final RiskCalculator riskCalculator = new RiskCalculator();
    private final AlertSystem alertSystem = new AlertSystem();
    private final RecommendationSystem recommendationSystem = new RecommendationSystem();

    // Latest results (replaced every 5 seconds)
    private WeatherData latestData;
    private int riskScore;
    private String riskLevel = "Unknown";
    private String condition = "Unknown";
    private boolean rainLikely;
    private List<String> alerts = new ArrayList<>();
    private List<String> recommendations = new ArrayList<>();

    private ScheduledExecutorService scheduler;
    private long startTimeMillis;

    /** Adds a sensor to the station. Rejects null and duplicate sensors. */
    public synchronized void addSensor(Sensor sensor) {
        if (sensor == null) {
            throw new IllegalArgumentException("Cannot add a null sensor.");
        }
        for (Sensor existing : sensors) {
            if (existing.getName().equals(sensor.getName())) {
                throw new IllegalArgumentException("Sensor already added: " + sensor.getName());
            }
        }
        sensors.add(sensor);
    }

    /** Starts the station: takes a first reading now, then one every 5 seconds. */
    public synchronized void start() {
        if (scheduler != null) {
            return; // already running
        }
        if (sensors.isEmpty()) {
            throw new IllegalStateException("Add sensors before starting the station.");
        }
        startTimeMillis = System.currentTimeMillis();
        updateReadings(); // first reading immediately, so the API has data straight away

        scheduler = Executors.newSingleThreadScheduledExecutor(task -> {
            Thread thread = new Thread(task, "sensor-updater");
            thread.setDaemon(true); // does not keep the program alive on its own
            return thread;
        });
        scheduler.scheduleAtFixedRate(() -> {
            try {
                updateReadings();
            } catch (Exception e) {
                // Exception handling: one bad update must not stop future updates.
                System.err.println("Update failed: " + e.getMessage());
            }
        }, UPDATE_INTERVAL_SECONDS, UPDATE_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }

    public synchronized void stop() {
        if (scheduler != null) {
            scheduler.shutdownNow();
            scheduler = null;
        }
    }

    /** Reads every sensor and recalculates risk, alerts and recommendations. */
    public synchronized void updateReadings() {
        // Polymorphism: update() is called on a Sensor reference,
        // but each sensor runs its OWN generateReading() version.
        for (Sensor sensor : sensors) {
            sensor.update();
        }

        WeatherData data = new WeatherData(
                readingOf(TemperatureSensor.NAME),
                readingOf(HumiditySensor.NAME),
                readingOf(WindSensor.NAME),
                readingOf(RainfallSensor.NAME),
                readingOf(PressureSensor.NAME),
                readingOf(UVSensor.NAME),
                readingOf(AirQualitySensor.NAME));

        latestData = data;
        riskScore = riskCalculator.calculateRiskScore(data);
        riskLevel = riskCalculator.getRiskLevel(riskScore);
        condition = analyzer.getOverallCondition(data);
        rainLikely = analyzer.isRainLikely(data);
        alerts = alertSystem.generateAlerts(data);
        recommendations = recommendationSystem.generateRecommendations(data);

        System.out.println("[" + data.getTimestamp() + "] Temp=" + data.getTemperature()
                + " Hum=" + data.getHumidity() + " Wind=" + data.getWindSpeed()
                + " Rain=" + data.getRainfall() + " Pres=" + data.getPressure()
                + " UV=" + data.getUvIndex() + " AQI=" + data.getAirQuality()
                + " | Risk=" + riskScore + " (" + riskLevel + ") | Alerts=" + alerts.size());
    }

    /** Finds a sensor by name and returns its current value. */
    private double readingOf(String sensorName) {
        for (Sensor sensor : sensors) {
            if (sensor.getName().equals(sensorName)) {
                return sensor.getCurrentValue();
            }
        }
        throw new IllegalStateException("Sensor not found: " + sensorName);
    }

    // ---------- Getters used by WeatherServer ----------

    public synchronized List<Sensor> getSensors() {
        return Collections.unmodifiableList(new ArrayList<>(sensors)); // safe read-only copy
    }

    public synchronized WeatherData getLatestData() {
        if (latestData == null) {
            throw new IllegalStateException("No readings yet. Start the station first.");
        }
        return latestData;
    }

    public synchronized int getRiskScore()        { return riskScore; }
    public synchronized String getRiskLevel()     { return riskLevel; }
    public synchronized String getCondition()     { return condition; }
    public synchronized boolean isRainLikely()    { return rainLikely; }
    public synchronized List<String> getAlerts()  { return new ArrayList<>(alerts); }
    public synchronized List<String> getRecommendations() { return new ArrayList<>(recommendations); }

    public synchronized long getUptimeSeconds() {
        return (System.currentTimeMillis() - startTimeMillis) / 1000;
    }
}
