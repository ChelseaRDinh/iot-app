package ca.uvic.seng330.assn3.devices;

import ca.uvic.seng330.assn3.JSONMessaging;
import java.util.UUID;
import org.json.JSONObject;

public abstract class Device implements IOTDevice {
  protected UUID uuid;
  protected Status status;
  private Hub hub;

  /**
   * Constructor for device that takes a hub.
   *
   * @param h the hub that this device is under
   */
  public Device(Hub h) {
    uuid = UUID.randomUUID();
    status = Status.NORMAL;
    hub = h;
  }

  @Override
  public final void setStatus(Status s) {
    status = s;
  }

  @Override
  public final Status getStatus() {
    return status;
  }

  @Override
  public final UUID getIdentifier() {
    return uuid;
  }

  public void startup() {
    status = Status.NORMAL;
  }

  public void shutdown() {
    status = Status.OFF;
  }

  public Hub getHub() {
    return hub;
  }

  /**
   * This function alerts the hub with a message from the device. It protects the hub from the
   * device implementations.
   *
   * @param message the message to send to the hub.
   */
  protected final void alertHub(String message) {
    hub.alert(new JSONMessaging(this, message));
  }

  protected final void alertHub(String target, String message) {
    hub.alert(new JSONMessaging(this, message, UUID.fromString(target)));
  }

  protected final void alertHub(String target, String message, String data) {
    JSONMessaging m = new JSONMessaging(this, message, UUID.fromString(target));
    m.addData(data);
    hub.alert(m);
  }

  /**
   * This function receives notifications from the hub which are sent from the clients. The device
   * implementation should filter based on target UUID/message.
   *
   * @param message the JSONObject that has all the required notification parameters
   */
  public abstract void notify(JSONObject message);
}
