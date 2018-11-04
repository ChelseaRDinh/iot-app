package ca.uvic.seng330.assn3.devices;

import org.json.JSONObject;

// Plenty of other devices have full implementations for example of operation.
public final class SmartPlug extends Device {
  /**
   * Constructor for smart plug that takes a hub.
   *
   * @param h the hub that this device is under
   */
  public SmartPlug(Hub h) {
    super(h);
  }

  @Override
  public void notify(JSONObject jsonMessage) {}
}
