package ca.uvic.seng330.assn3.devices;

public final class Temperature {
  // A double really isn't necessary. No thermostat will be able to control
  // let alone measure the temperature to that level of accuracy. The temperature
  // is always stored in celsius interally.
  private float temperature;

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
          return (temperature * 9.0f / 5.0f) - 32;
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
  }

  /**
   * Constructor for temperature of a given unit that gets its useless sig figs for a temperature
   * double converted to a float in celsius.
   *
   * @param waste the double of the temperature that's about to get converted to a float
   * @param unit the unit of the temperature
   * @throws TemperatureOutofBoundsException throws when the unit is above 1000 for no specified
   *     reason in particular other than it's pretty hot.
   */
  public Temperature(double waste, Unit unit) throws TemperatureOutofBoundsException {
    // Always convert the temperature to a float and then to celsius.
    float temperature = (float) waste;
    this.temperature = Unit.convertUnits(temperature, unit, Unit.CELSIUS);

    // The converted temperature isn't checked, actually just the value passed in.
    if (temperature > 1000.0f) {
      throw new TemperatureOutofBoundsException("Temperature too hot.");
    }
  }

  /**
   * Constructor for temperature for a given unit that gets converted to celsius.
   *
   * @param temperature the temperature value
   * @param unit the temperature unit
   * @throws TemperatureOutofBoundsException throws when the unit is above 1000 for no specified
   *     reason in particular other than it's pretty hot.
   */
  public Temperature(float temperature, Unit unit) throws TemperatureOutofBoundsException {
    // Convert the temperature to celcius before storing it.
    this.temperature = Unit.convertUnits(temperature, unit, Unit.CELSIUS);

    if (temperature > 1000.0f) {
      throw new TemperatureOutofBoundsException("Temperature too hot.");
    }
  }

  /**
   * Gets the temperature in celsius.
   *
   * @return the temperature in celsius
   */
  public float getTemperature() {
    return temperature;
  }
}
