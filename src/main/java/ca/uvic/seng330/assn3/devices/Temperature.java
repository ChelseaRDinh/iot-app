package ca.uvic.seng330.assn3.devices;

public final class Temperature {
  private float temperature;
  private Unit unit;

  public enum Unit {
    CELSIUS,
    FAHRENHEIT;

    /**
     * Converts a temperature from a given unit to another.
     *
     * @param temperature the temperature in "from" units
     * @param from the units that the temperature is currently in
     * @param to the unit to convert to
     * @return the converted temperature (converting to the same unit does nothing)
     */
    public static float convertUnits(float temperature, Unit from, Unit to) {
      if (from == FAHRENHEIT) {
        if (to == CELSIUS) {
          return (temperature - 32) * 5.0f / 9.0f;
        }
      } else if (from == CELSIUS) {
        if (to == FAHRENHEIT) {
          return (temperature * 9.0f / 5.0f) + 32;
        }
      }

      return temperature;
    }
  }

  public class TemperatureOutofBoundsException extends Exception {
    public TemperatureOutofBoundsException(String message) {
      super(message);
    }
  }

  public Temperature() {
    temperature = 0.0f;
    unit = Unit.CELSIUS;
  }

  /**
   * Constructor for temperature for a given unit
   *
   * @param temperature the temperature value
   * @param unit the temperature unit
   * @throws TemperatureOutofBoundsException throws when the unit is above 1000 for no specified
   *     reason in particular other than it's pretty hot.
   */
  public Temperature(float temperature, Unit unit) throws TemperatureOutofBoundsException {
    this.temperature = temperature;
    this.unit = unit;

    if (temperature > 1000.0f) {
      throw new TemperatureOutofBoundsException("Temperature too hot.");
    }
  }

  /**
   * Gets the temperature.
   *
   * @return the temperature in the Unit measurement
   */
  public float getTemperature() {
    return temperature;
  }

  public Unit getUnit() {
    return unit;
  }
}
