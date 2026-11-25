package weatheriq;

import java.net.BindException;

/**
 * Main - program entry point. Creates the sensors, the station and the server.
 *
 * Optional: pass a port number as an argument, e.g.  java -cp out weatheriq.Main 9090
 * (default is 8080).
 */
public class Main {

    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) {
        System.out.println("=== WEATHERIQ - Smart Advanced Weather Station ===");

        try {
            int port = readPort(args);

            // 1. Create the station and add all 7 sensors.
            //    The list type is Sensor, but each object is a different child class.
            WeatherStation station = new WeatherStation();
            station.addSensor(new TemperatureSensor());
            station.addSensor(new HumiditySensor());
            station.addSensor(new WindSensor());
            station.addSensor(new RainfallSensor());
            station.addSensor(new PressureSensor());
            station.addSensor(new UVSensor());
            station.addSensor(new AirQualitySensor());

            // 2. Start sensor updates (every 5 seconds).
            station.start();

            // 3. Start the HTTP server.
            WeatherServer server = new WeatherServer(station, port);
            server.start();

            // 4. Stop everything cleanly when you press Ctrl+C.
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                server.stop();
                station.stop();
                System.out.println("WeatherIQ stopped. Goodbye!");
            }));

            System.out.println("Server running on port " + port);
            System.out.println("  Weather : http://localhost:" + port + "/api/weather");
            System.out.println("  Health  : http://localhost:" + port + "/api/health");
            System.out.println("Press Ctrl+C to stop.");

        } catch (BindException e) {
            System.err.println("ERROR: The port is already in use. Close the other program or pass a different port.");
        } catch (NumberFormatException e) {
            System.err.println("ERROR: Port must be a number, for example: java -cp out weatheriq.Main 9090");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.err.println("ERROR: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("ERROR: Could not start WeatherIQ - " + e.getMessage());
        }
    }

    /** Reads the optional port argument; falls back to 8080. */
    private static int readPort(String[] args) {
        if (args.length == 0) {
            return DEFAULT_PORT;
        }
        return Integer.parseInt(args[0].trim());
    }
}
