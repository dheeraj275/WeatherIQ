# WEATHERIQ - Smart Advanced Weather Station (Java Backend)

Simulated weather backend using only core Java 21 (no libraries, no database).

```
WeatherIQ/
|-- run.bat                      <- double-click to compile + run (Windows)
|-- .vscode/settings.json        <- tells VS Code where the source/output folders are
|-- src/weatheriq/
|   |-- Sensor.java              (abstract)
|   |-- TemperatureSensor.java   HumiditySensor.java   WindSensor.java
|   |-- RainfallSensor.java      PressureSensor.java   UVSensor.java   AirQualitySensor.java
|   |-- WeatherData.java         WeatherAnalyzer.java  RiskCalculator.java
|   |-- AlertSystem.java         RecommendationSystem.java
|   |-- WeatherStation.java      WeatherServer.java    Main.java
```

---

## 1. Setup and run on Windows (VS Code)

1. **Install JDK 21** (e.g. Eclipse Temurin 21 from adoptium.net). Tick "Add to PATH" during install.
2. Open **PowerShell** and verify both commands show version 21:
   ```
   java -version
   javac -version
   ```
3. In VS Code, install the **Extension Pack for Java** (Extensions panel, Ctrl+Shift+X).
4. Unzip `WeatherIQ.zip` (e.g. to `C:\Projects\WeatherIQ`), then **File > Open Folder** and choose `WeatherIQ`.
5. Open the terminal (**Ctrl + `**) and run:
   ```
   mkdir out
   javac -d out src\weatheriq\*.java
   java -cp out weatheriq.Main
   ```
   (Or simply run `.\run.bat`, or double-click it.)
6. You should see:
   ```
   === WEATHERIQ - Smart Advanced Weather Station ===
   Server running on port 8080
   ```
   A new reading line prints every 5 seconds. Press **Ctrl + C** to stop.

Alternative: open `Main.java` and click **Run** above `main`.

If Windows Firewall asks for permission, choose **Allow** (private networks).
To use another port: `java -cp out weatheriq.Main 9090`.

## 2. Testing the endpoints

Keep the server running, open a **second** terminal:

```
curl.exe http://localhost:8080/api/health
curl.exe http://localhost:8080/api/weather
Invoke-RestMethod http://localhost:8080/api/weather | ConvertTo-Json -Depth 5
```
(Use `curl.exe`, not `curl` - in PowerShell `curl` is an alias for something else.)

Or just open `http://localhost:8080/api/weather` in a browser. Refresh after 5 seconds - values change.

Error checks:
```
curl.exe -i -X POST http://localhost:8080/api/weather      -> 405 Method Not Allowed (JSON error)
curl.exe -i http://localhost:8080/api/health               -> 200 OK
```

### Sample /api/weather response
```json
{
  "timestamp": "2026-11-24 19:18:58",
  "condition": "Clear and Pleasant",
  "rainLikely": false,
  "readings": {
    "temperature": {"value": 27.5, "unit": "°C", "status": "Warm"},
    "humidity":    {"value": 56.6, "unit": "%", "status": "Comfortable"},
    "wind":        {"value": 11.0, "unit": "km/h", "status": "Calm"},
    "rainfall":    {"value": 0.0, "unit": "mm/h", "status": "No Rain"},
    "pressure":    {"value": 1012.2, "unit": "hPa", "status": "Normal"},
    "uv":          {"value": 4.8, "unit": "index", "status": "Moderate"},
    "airQuality":  {"value": 79.0, "unit": "AQI", "status": "Moderate"}
  },
  "riskScore": 3,
  "riskLevel": "Low",
  "alerts": [],
  "recommendations": ["Conditions are comfortable. Enjoy your day!"]
}
```
### Sample /api/health response
```json
{"status":"UP","service":"WeatherIQ","sensors":7,"uptimeSeconds":3,"time":"2026-11-24 19:19:01"}
```

### Calling it from your dashboard (JavaScript)
```js
const res = await fetch("http://localhost:8080/api/weather");
const data = await res.json();
console.log(data.readings.temperature.value, data.riskScore);
```
CORS headers are already enabled, so a dashboard opened from a file or another port can call the API.
If your dashboard expects different field names, change only `buildWeatherJson()` in `WeatherServer.java`.

---

## 3. Explanation of each class

| Class | Role |
|---|---|
| **Sensor** (abstract) | Parent of all sensors. Holds name, unit, min, max, current value (all private). Has abstract `generateReading()` and `getStatus()`, plus shared `update()`, `nextValue()`, getters and `toString()`. |
| **TemperatureSensor, HumiditySensor, WindSensor, RainfallSensor, PressureSensor, UVSensor, AirQualitySensor** | Children of Sensor. Each sets its own unit/range in the constructor and overrides `generateReading()` and `getStatus()` with its own logic. |
| **WeatherData** | Immutable snapshot of all 7 readings + timestamp. Validates values in its constructor. |
| **WeatherAnalyzer** | Describes overall condition ("Stormy", "Hot and Humid"...) and whether rain is likely. |
| **RiskCalculator** | Adds risk points per parameter (max 100 total) and converts the score to Low / Moderate / High / Severe. |
| **AlertSystem** | Returns warning messages when a reading crosses a danger limit (empty list if none). |
| **RecommendationSystem** | Returns simple advice (hydrate, umbrella, mask, sunscreen...). |
| **WeatherStation** | Owns `ArrayList<Sensor>`, updates all sensors every 5 s with a `ScheduledExecutorService`, and uses the four helper classes above. |
| **WeatherServer** | Wraps Java's `HttpServer`; serves `/api/weather` and `/api/health` as JSON. |
| **Main** | Creates sensors, station and server; handles startup errors; clean shutdown on Ctrl+C. |

### Where each OOP concept appears

| Concept | Where |
|---|---|
| **Encapsulation** | Private fields + getters in `Sensor`, `WeatherData`, `WeatherStation`. `WeatherData` has no setters. |
| **Constructors** | `Sensor(...)` validates input; child constructors call `super(...)`; overloaded constructors `TemperatureSensor()` / `TemperatureSensor(double)` using `this(...)`. |
| **Inheritance** | 7 sensors `extends Sensor`. |
| **Abstraction** | `Sensor` is abstract; callers use `update()` / `getStatus()` without knowing the details. |
| **Method overriding** | `generateReading()`, `getStatus()` in every sensor; `toString()` in `Sensor` and `WeatherData`. |
| **Polymorphism** | `ArrayList<Sensor>` holds all 7 types; `sensor.update()` and `sensor.getStatus()` run the correct child version at runtime. |
| **Composition (has-a)** | `WeatherStation` has a WeatherAnalyzer, RiskCalculator, AlertSystem, RecommendationSystem. |
| **Exception handling / validation** | Constructor checks, `addSensor` duplicate check, HTTP 405/500/503 responses, port validation, BindException message, try/catch in the update loop. |

### Risk score rules (max points)
Temperature 15 | Humidity 10 | Wind 20 | Rainfall 20 | Pressure 10 | UV 10 | Air quality 15 = **100**.
Levels: 0-24 Low, 25-49 Moderate, 50-74 High, 75-100 Severe.

---

## 4. Fifteen likely viva questions

**1. What are the four pillars of OOP, and where did you use them?**
Encapsulation (private fields + getters), Inheritance (7 sensors extend Sensor), Abstraction (abstract Sensor class), Polymorphism (one `ArrayList<Sensor>` holding different sensor types, each behaving differently).

**2. What is an abstract class? Where is it used?**
A class declared with `abstract` that cannot be instantiated and may contain abstract (body-less) methods. `Sensor` is abstract because a generic "sensor" does not exist in real life - only specific ones do. It forces every child to implement `generateReading()` and `getStatus()`.

**3. Why an abstract class and not an interface?**
All sensors share state (name, unit, min, max, current value) and common code (`update()`, `nextValue()`). Interfaces cannot hold instance fields, so an abstract class lets me share both data and behaviour.

**4. What is encapsulation? Give an example.**
Binding data and the methods that work on it together, and hiding the data with `private`. In `Sensor`, `currentValue` is private; it can only change through `update()`, which also clamps it to the valid range, so it can never become invalid.

**5. What is inheritance? Which keyword is used?**
A child class reuses the fields and methods of a parent. `TemperatureSensor extends Sensor` gets `update()`, `getName()` etc. for free and only writes what is different.

**6. Difference between overriding and overloading?**
Overriding: child redefines a parent's method with the same signature (`getStatus()` in each sensor) - decided at runtime. Overloading: same method/constructor name with different parameters (`TemperatureSensor()` and `TemperatureSensor(double)`) - decided at compile time.

**7. Where did you use polymorphism?**
In `WeatherStation`: `for (Sensor s : sensors) s.update();`. The variable type is `Sensor`, but the actual object may be a `WindSensor`, `UVSensor`, etc., and Java calls that object's own `generateReading()`. The same happens with `s.getStatus()` in `WeatherServer`.

**8. Compile-time vs runtime polymorphism?**
Compile-time = method overloading. Runtime = method overriding with a parent reference pointing to a child object (dynamic method dispatch). My sensor list uses runtime polymorphism.

**9. What is a constructor? What do `this(...)` and `super(...)` do?**
A special method with the class name and no return type, called when an object is created to initialise it. `super(...)` calls the parent constructor (`TemperatureSensor` calls `Sensor`'s constructor, which validates the inputs). `this(...)` calls another constructor of the same class (`TemperatureSensor()` calls `this(28.0)`).

**10. Inheritance vs composition (is-a vs has-a)?**
Inheritance is "is-a": a TemperatureSensor IS a Sensor. Composition is "has-a": a WeatherStation HAS a RiskCalculator and an AlertSystem. I use composition there because a station is not a kind of calculator.

**11. Why did you use ArrayList for sensors?**
It is a resizable array, keeps insertion order, and is easy to loop over. Adding an 8th sensor needs only one `addSensor(...)` line - the rest of the code does not change.

**12. How is the risk score calculated?**
Each of the 7 parameters adds points using simple if-rules in `RiskCalculator` (e.g. wind >= 60 km/h adds 20). The maximum points sum to exactly 100, and `Math.min/Math.max` keeps the final score within 0-100. The score is then mapped to Low/Moderate/High/Severe.

**13. How does your HTTP server work? What is JSON and CORS?**
Java's built-in `com.sun.net.httpserver.HttpServer` listens on port 8080; `createContext("/api/weather", handler)` maps a URL to a handler method. The handler builds a JSON string (a text format of key-value pairs that JavaScript can read) and sends it with status 200. GET is used because we only read data. CORS headers (`Access-Control-Allow-Origin`) allow the browser dashboard, running on a different origin, to call this API.

**14. Why is `synchronized` used, and how do updates every 5 seconds work?**
`ScheduledExecutorService` runs `updateReadings()` on a background thread every 5 seconds, while HTTP requests are handled on other threads. `synchronized` makes sure a request never reads half-updated data.

**15. How do you handle exceptions and invalid input?**
Validation throws `IllegalArgumentException` (bad sensor range, humidity > 100, null sensor, duplicate sensor, bad port). The server catches exceptions and returns proper HTTP codes (405 wrong method, 503 no data yet, 500 other errors). `Main` catches `BindException` (port in use) and `NumberFormatException` (bad port argument) and prints friendly messages. Inside the 5-second loop a try/catch stops one failed update from cancelling the schedule. These are unchecked exceptions (RuntimeException); `IOException` from starting the server is a checked exception that must be caught or declared.

---

## 5. Quick ideas if the examiner asks "how would you extend it?"
- Add a new sensor (e.g. `VisibilitySensor extends Sensor`) - only one new class and one `addSensor` line.
- Replace simulated values with real sensor data by changing only `generateReading()`.
- Add a database or history list later without touching the sensor classes.
