package ca.uvic.seng330.assn3.devices;

import ca.uvic.seng330.assn3.devices.Temperature.TemperatureOutofBoundsException;
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
      // 8 is "setTemp."
      if (message.length() > 8) {
        String floatPortion = message.substring(8);
        float temperature = 0.0f;
        boolean parsed = false;

        try {
          temperature = Float.parseFloat(floatPortion);
          parsed = true;
        } catch (Exception e) {
          alertHub(
              Hub.targetJSONMessage(jsonMessage.getString("node_id"), "NumberFormatException"));
        }

        // If the temperature value wasn't parsed successfully then return.
        if (!parsed) {
          return;
        }

        try {
          setTemp(new Temperature(temperature, Temperature.Unit.CELSIUS));
        } catch (TemperatureOutofBoundsException e) {
          alertHub(
              Hub.targetJSONMessage(
                  jsonMessage.getString("node_id"), "TemperatureOutofBoundsException"));
        }
      }
    } else if (message.equals("getTemp")) {
      alertHub(
          Hub.targetJSONMessage(
              jsonMessage.getString("node_id"), "getTemp." + new Float(getTemp()).toString()));
    }
  }
}