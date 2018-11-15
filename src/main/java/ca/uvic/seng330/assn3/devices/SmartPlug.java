package ca.uvic.seng330.assn3.devices;

import org.json.JSONObject;

// Plenty of other devices have full implementations for example of operation.
public final class SmartPlug extends Device {
  boolean isOn;
  /**
   * Constructor for smart plug that takes a hub.
   *
   * @param h the hub that this device is under
   */
  public SmartPlug(Hub h) {
    super(h);

    isOn = false;
  }

  public boolean getCondition() {
    return isOn;
  }

  public void toggle() {
    isOn = !isOn;
  }

  @Override
  public void notify(JSONObject jsonMessage) {
    String message = jsonMessage.getString("payload");

    // If this was targeted then carry out the action if the action is applicable to this device.
    if (message.equals("toggle")) {
      toggle();
    } else if (message.equals("getCondition")) {
      alertHub(
          jsonMessage.getString("node_id"), "getCondition", new Boolean(getCondition()).toString());
    }
  }
}
