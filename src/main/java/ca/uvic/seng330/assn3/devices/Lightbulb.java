package ca.uvic.seng330.assn3.devices;

import org.json.JSONObject;

public final class Lightbulb extends Device {
  private boolean isOn;

  /**
   * Constructor for lightbulb that takes a hub.
   *
   * @param h the hub that this device is under
   */
  public Lightbulb(Hub h) {
    super(h);

    isOn = false;
  }

  /** Toggles the lightbulb on if it's off, off if it's on. */
  public void toggle() {
    isOn = !isOn;

    if (isOn) {
      setStatus(Status.OFF);
      alertHub("Light is now off.");
    } else {
      setStatus(Status.NORMAL);
      alertHub("Light is now on.");
    }
  }

  /**
   * Gets whether the light is on or off.
   *
   * @return whether the light is on or off
   */
  public boolean getCondition() {
    return isOn;
  }

  /**
   * Notify the lightbulb of some message sent to it.
   *
   * @param jsonMessage the message as a JSONObject that's been sent
   */
  @Override
  public void notify(JSONObject jsonMessage) {
    String message = jsonMessage.getString("payload");

    // If this was targeted then carry out the action if the action is applicable to this device.
    if (message.equals("toggle")) {
      toggle();
    } else if (message.equals("getCondition")) {
      alertHub(
          jsonMessage.getString("node_id"),
          "getCondition." + new Boolean(getCondition()).toString());
    }
  }
}
