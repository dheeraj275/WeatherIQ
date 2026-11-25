WEATHERIQ - INTEGRATED JAVA BACKEND + EXISTING DASHBOARD

WHAT CHANGED
- Preserved the existing dashboard design and layout.
- Connected dashboard data to the Java API at http://localhost:9090/api/weather.
- Dashboard fetches fresh readings every 5 seconds.
- Temperature, humidity, wind, rainfall, pressure, UV and AQI now come from Java.
- Added Java-powered risk score, alerts and recommendations to the overview.
- Sensor chart and analytics trend lines build short-term history in the browser session.
- The dashboard does not use the old random temperature fluctuation anymore.
- The original login mockup, station diagnostics and some decorative design elements remain visual mockups; they are not backed by Java.
- No persistent historical data is stored. Analytics history is only collected while this dashboard tab remains open.

RUN ON WINDOWS
1. Open Backend\WeatherIQ as a folder in VS Code.
2. In its terminal run:
   .\run.bat 9090
   The included run.bat accepts the port number as an argument.
3. Keep the Java server terminal open.
4. Open a SECOND terminal in Dashboard and run:
   py -m http.server 5500
   (If py is unavailable, try: python -m http.server 5500)
5. Open this in your browser:
   http://localhost:5500/code.html
6. Keep both terminals running. The dashboard will fetch Java data every 5 seconds.

TEST
- Backend health: http://localhost:9090/api/health
- Backend weather: http://localhost:9090/api/weather
- Dashboard: http://localhost:5500/code.html

TROUBLESHOOTING
- If port 9090 is busy, stop the other Java process or use another port. If you change the backend port, also change WEATHER_API_URL and WEATHER_HEALTH_URL near the bottom of Dashboard\code.html.
- If the dashboard says Backend disconnected, first confirm /api/health opens in the same browser.
- Do not close either terminal while demonstrating the project.

IMPORTANT PROJECT SCOPE
The Java backend uses simulated sensor readings, not physical IoT sensors. The dashboard displays the Java-generated simulated readings. The dashboard's in-session charts are not long-term historical analytics because no database or file storage is used.
