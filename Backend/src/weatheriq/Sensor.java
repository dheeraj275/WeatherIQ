package weatheriq;

import java.util.Random;

/**
 * Sensor - ABSTRACT base class for every weather sensor.
 *
 * OOP concepts shown here:
 *  - Abstraction   : cannot be instantiated; declares abstract methods that
 *                    every child sensor MUST implement.
 *  - Encapsulation : all fields are private and are accessed through methods.
 *  - Constructor   : validates its inputs before creating the object.
 */
public abstract class Sensor {

    // ---------- Encapsulation: private data ----------
    private final String name;
    private final String unit;
    private final double minValue;
    private final double maxValue;
    private double currentValue;
    private final Random random = new Random();

    /** Constructor used by child classes through super(...). */
    protected Sensor(String name, String unit, double minValue, double maxValue, double startValue) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Sensor name cannot be empty.");
        }
        if (unit == null) {
            throw new IllegalArgumentException("Sensor unit cannot be null.");
        }
        if (minValue >= maxValue) {
            throw new IllegalArgumentException("Minimum value must be less than maximum value for sensor " + name);
        }
        if (startValue < minValue || startValue > maxValue) {
            throw new IllegalArgumentException("Start value " + startValue + " is outside the range "
                    + minValue + " to " + maxValue + " for sensor " + name);
        }
        this.name = name;
        this.unit = unit;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.currentValue = startValue;
    }

    // ---------- Abstract methods: each child gives its own version ----------

    /** Produce the next simulated reading for this sensor. */
    protected abstract double generateReading();

    /** Describe the current reading in words, e.g. "Hot", "Low", "Good". */
    public abstract String getStatus();

    // ---------- Common behaviour shared by all sensors ----------

    /** Takes a new reading. Works for ANY sensor type (polymorphism). */
    public void update() {
        double reading = generateReading();
        // Keep the value inside the sensor's valid range.
        currentValue = Math.max(minValue, Math.min(maxValue, reading));
    }

    /**
     * Helper for children: small random change from the current value.
     * This is a "random walk", so values change smoothly instead of jumping wildly.
     */
    protected double nextValue(double maxStep) {
        double change = (random.nextDouble() * 2 - 1) * maxStep;
        double next = currentValue + change;
        next = Math.max(minValue, Math.min(maxValue, next));
        return Math.round(next * 10.0) / 10.0;   // round to 1 decimal place
    }

    /** Helper for children: returns true with the given probability (0.0 to 1.0). */
    protected boolean chance(double probability) {
        return random.nextDouble() < probability;
    }

    // ---------- Getters (read-only access) ----------
    public String getName()        { return name; }
    public String getUnit()        { return unit; }
    public double getMinValue()    { return minValue; }
    public double getMaxValue()    { return maxValue; }
    public double getCurrentValue(){ return currentValue; }

    /** Method overriding: replaces Object.toString(). */
    @Override
    public String toString() {
        return name + ": " + currentValue + " " + unit + " (" + getStatus() + ")";
    }
}
