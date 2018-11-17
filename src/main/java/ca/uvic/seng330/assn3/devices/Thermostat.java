package ca.uvic.seng330.assn3.devices;

import ca.uvic.seng330.assn3.devices.Temperature.TemperatureOutofBoundsException;
import ca.uvic.seng330.assn3.devices.Temperature.Unit;
import org.json.JSONObject;

public final class Thermostat extends Device {
  private Temperature desiredTemperature;
  private boolean isOn;

  /**
   * Constructor for thermostat that takes a hub.
   *
   * @param h the hub that this device is under
   */
  public Thermostat(Hub h) {
    super(h);

    desiredTemperature = new Temperature();
    isOn = false;
  }

  /**
   * Sets the temperature of the thermostat.
   *
   * @param temp the temperature to set the thermostat to
   */
  public void setTemp(Temperature temp) {
    desiredTemperature = temp;

    alertHub(
        "Thermostat temperature set to "
            + new Float(desiredTemperature.getTemperature()).toString()
            + " celsius.");
  }

  /**
   * Gets the temperature in celsius.
   *
   * @return the temperature in celsius as a float
   */
  public float getTemp() {
    return desiredTemperature.getTemperature();
  }

  public boolean getUnit() {
    return desiredTemperature.getUnit() == Unit.CELSIUS;
  }

  public boolean getCondition() {
    return isOn;
  }

  public void toggle() {
    isOn = !isOn;
  }

  /**
   * Notify the thermostat of some message sent to it.
   *
   * @param jsonMessage the message as a JSONObject that's been sent
   */
  @Override
  public void notify(JSONObject jsonMessage) {
    String message = jsonMessage.getString("payload");

    // If this was targeted then carry out the action if the action is applicable to this device.
    if (message.equals("setTemp")) {
      Float temperature = jsonMessage.getFloat("data");

      trySetTempWithReponse(jsonMessage, temperature, desiredTemperature.getUnit());
    } else if (message.equals("getTemp")) {
      alertHub(jsonMessage.getString("node_id"), "getTemp", new Float(getTemp()).toString());
    } else if (message.equals("setUnit")) {
      Boolean isCelsius = jsonMessage.getBoolean("data");
      Unit currentUnit = desiredTemperature.getUnit();
      float currentTemp = desiredTemperature.getTemperature();

      // Set the current temp to the other unit, converting its current value.
      if (isCelsius && currentUnit != Unit.CELSIUS) {
        float temperature = Unit.convertUnits(currentTemp, currentUnit, Unit.CELSIUS);

        trySetTempWithReponse(jsonMessage, temperature, Unit.CELSIUS);
        alertHub(jsonMessage.getString("node_id"), "getTemp", new Float(getTemp()).toString());
      } else if (!isCelsius && currentUnit != Unit.FAHRENHEIT) {
        float temperature = Unit.convertUnits(currentTemp, currentUnit, Unit.FAHRENHEIT);

        trySetTempWithReponse(jsonMessage, temperature, Unit.FAHRENHEIT);
        alertHub(jsonMessage.getString("node_id"), "getTemp", new Float(getTemp()).toString());
      }
    } else if (message.equals("getUnit")) {
      // Return if it's in celsius or not.
      if (desiredTemperature.getUnit() == Unit.CELSIUS) {
        alertHub(jsonMessage.getString("node_id"), "getUnit", "true");
      } else {
        alertHub(jsonMessage.getString("node_id"), "getUnit", "false");
      }
    } else if (message.equals("getCondition")) {
      alertHub(
          jsonMessage.getString("node_id"), "getCondition", new Boolean(getCondition()).toString());
    } else if (message.equals("toggle")) {
      toggle();
    }
  }

  private void trySetTempWithReponse(JSONObject jsonMessage, float temperature, Unit unit) {
    try {
      setTemp(new Temperature(temperature, unit));
    } catch (TemperatureOutofBoundsException e) {
      alertHub(jsonMessage.getString("node_id"), "TemperatureOutofBoundsException");
    }
  }
}
