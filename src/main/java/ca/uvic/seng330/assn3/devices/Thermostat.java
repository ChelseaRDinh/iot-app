package ca.uvic.seng330.assn3.devices;

import ca.uvic.seng330.assn3.devices.Temperature.TemperatureOutofBoundsException;
import ca.uvic.seng330.assn3.devices.Temperature.Unit;
import org.json.JSONObject;

public final class Thermostat extends Device {
  private Temperature desiredTemperature;

  /**
   * Constructor for thermostat that takes a hub.
   *
   * @param h the hub that this device is under
   */
  public Thermostat(Hub h) {
    super(h);

    desiredTemperature = new Temperature();
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
      String unit = jsonMessage.getString("data");
      Unit currentUnit = desiredTemperature.getUnit();
      float currentTemp = desiredTemperature.getTemperature();

      // Set the current temp to the other unit, converting its current value.
      if (unit.equals("celsius") && currentUnit != Unit.CELSIUS) {
        float temperature = Unit.convertUnits(currentTemp, currentUnit, Unit.CELSIUS);

        trySetTempWithReponse(jsonMessage, temperature, Unit.CELSIUS);
      } else if (unit.equals("fahrenheit") && currentUnit != Unit.FAHRENHEIT) {
        float temperature = Unit.convertUnits(currentTemp, currentUnit, Unit.FAHRENHEIT);

        trySetTempWithReponse(jsonMessage, temperature, Unit.FAHRENHEIT);
      }
    } else if (message.equals("getUnit")) {
      if (desiredTemperature.getUnit() == Unit.CELSIUS) {
        alertHub(jsonMessage.getString("node_id"), "getUnit", "celsius");
      } else {
        alertHub(jsonMessage.getString("node_id"), "getUnit", "fahrenheit");
      }
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
