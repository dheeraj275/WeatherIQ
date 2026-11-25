package weatheriq;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * WeatherServer - a tiny web server (Java's built-in HttpServer) that shares
 * the weather station's data as JSON.
 *
 *   GET /api/weather  -> readings, risk score, alerts, recommendations
 *   GET /api/health   -> server status
 */
public class WeatherServer {

    private final WeatherStation station;
    private final int port;
    private HttpServer server;

    public WeatherServer(WeatherStation station, int port) {
        if (station == null) {
            throw new IllegalArgumentException("WeatherStation cannot be null.");
        }
        if (port < 1 || port > 65535) {
            throw new IllegalArgumentException("Port must be between 1 and 65535, but was " + port);
        }
        this.station = station;
        this.port = port;
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/api/weather", this::handleWeather);
        server.createContext("/api/health", this::handleHealth);
        server.start();
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }

    // ---------------- Request handlers ----------------

    private void handleWeather(HttpExchange exchange) throws IOException {
        try {
            if (rejectIfNotGet(exchange)) {
                return;
            }
            sendJson(exchange, 200, buildWeatherJson());
        } catch (IllegalStateException e) {
            sendJson(exchange, 503, errorJson(e.getMessage()));
        } catch (Exception e) {
            sendJson(exchange, 500, errorJson("Internal server error: " + e.getMessage()));
        }
    }

    private void handleHealth(HttpExchange exchange) throws IOException {
        try {
            if (rejectIfNotGet(exchange)) {
                return;
            }
            String json = "{"
                    + "\"status\":\"UP\","
                    + "\"service\":\"WeatherIQ\","
                    + "\"sensors\":" + station.getSensors().size() + ","
                    + "\"uptimeSeconds\":" + station.getUptimeSeconds() + ","
                    + "\"time\":" + quote(now())
                    + "}";
            sendJson(exchange, 200, json);
        } catch (Exception e) {
            sendJson(exchange, 500, errorJson("Internal server error: " + e.getMessage()));
        }
    }

    /**
     * Input validation for HTTP: only GET is allowed.
     * OPTIONS is answered because browsers send it first (CORS pre-check).
     * @return true if a response was already sent (so the caller must stop).
     */
    private boolean rejectIfNotGet(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if ("OPTIONS".equalsIgnoreCase(method)) {
            addCorsHeaders(exchange);
            exchange.sendResponseHeaders(204, -1);
            exchange.close();
            return true;
        }
        if (!"GET".equalsIgnoreCase(method)) {
            sendJson(exchange, 405, errorJson("Method " + method + " not allowed. Use GET."));
            return true;
        }
        return false;
    }

    // ---------------- JSON building (written by hand, no libraries) ----------------

    private String buildWeatherJson() {
        // synchronized: take one consistent snapshot while the timer thread cannot change it
        synchronized (station) {
            WeatherData data = station.getLatestData();
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append("\"timestamp\":").append(quote(data.getTimestamp())).append(",");
            sb.append("\"condition\":").append(quote(station.getCondition())).append(",");
            sb.append("\"rainLikely\":").append(station.isRainLikely()).append(",");

            sb.append("\"readings\":{");
            List<Sensor> sensors = station.getSensors();
            for (int i = 0; i < sensors.size(); i++) {
                Sensor s = sensors.get(i);   // Sensor reference, real object is a child class
                if (i > 0) {
                    sb.append(",");
                }
                sb.append(quote(s.getName())).append(":{")
                  .append("\"value\":").append(s.getCurrentValue()).append(",")
                  .append("\"unit\":").append(quote(s.getUnit())).append(",")
                  .append("\"status\":").append(quote(s.getStatus()))   // polymorphic call
                  .append("}");
            }
            sb.append("},");

            sb.append("\"riskScore\":").append(station.getRiskScore()).append(",");
            sb.append("\"riskLevel\":").append(quote(station.getRiskLevel())).append(",");
            sb.append("\"alerts\":").append(toJsonArray(station.getAlerts())).append(",");
            sb.append("\"recommendations\":").append(toJsonArray(station.getRecommendations()));
            sb.append("}");
            return sb.toString();
        }
    }

    private String toJsonArray(List<String> items) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < items.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(quote(items.get(i)));
        }
        return sb.append("]").toString();
    }

    private String errorJson(String message) {
        return "{\"error\":" + quote(message) + "}";
    }

    /** Wraps text in quotes and escapes characters that would break JSON. */
    private String quote(String text) {
        if (text == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder("\"");
        for (char c : text.toCharArray()) {
            switch (c) {
                case '"'  -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default -> {
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
                }
            }
        }
        return sb.append("\"").toString();
    }

    private String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // ---------------- Sending responses ----------------

    private void addCorsHeaders(HttpExchange exchange) {
        // CORS lets your dashboard (a different origin / file) call this API from the browser.
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
    }

    private void sendJson(HttpExchange exchange, int statusCode, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        addCorsHeaders(exchange);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.getResponseHeaders().set("Cache-Control", "no-store");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream out = exchange.getResponseBody()) {
            out.write(bytes);
        }
    }
}
