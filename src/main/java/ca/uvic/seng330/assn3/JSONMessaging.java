package ca.uvic.seng330.assn3;

import ca.uvic.seng330.assn3.devices.Device;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import org.json.JSONObject;

public final class JSONMessaging {
  private String identifier;
  private String status;
  private String message;
  private String data = "null";
  private List<UUID> targets = new ArrayList<UUID>();

  /**
   * Constructor for a message from a client.
   *
   * @param c the sender client
   * @param message the message being sent
   */
  public JSONMessaging(Client c, String message) {
    this.message = message;
    identifier = c.getIdentifier().toString();
    status = "client";
  }

  /**
   * Constructor for a message from a client with a target.
   *
   * @param c the sender client
   * @param message the message being sent
   * @param target the intended recipient target of the message
   */
  public JSONMessaging(Client c, String message, UUID target) {
    this.message = message;
    identifier = c.getIdentifier().toString();
    status = "client";
    targets.add(target);
  }

  /**
   * Constructor for a message from a client with multiple targets.
   *
   * @param c the sender client
   * @param message the message being sent
   * @param targets the intended recipient targets of the message
   */
  public JSONMessaging(Client c, String message, List<UUID> targets) {
    this.message = message;
    identifier = c.getIdentifier().toString();
    status = "client";
    this.targets = targets;
  }

  /**
   * Constructor for a message from a device.
   *
   * @param d the sender device
   * @param message the message being sent
   */
  public JSONMessaging(Device d, String message) {
    this.message = message;
    identifier = d.getIdentifier().toString();
    status = d.getStatus().toString();
  }

  /**
   * Constructor for a message from a device with a target.
   *
   * @param d the sender device
   * @param message the message being sent
   * @param target the intended recipient target of the message
   */
  public JSONMessaging(Device d, String message, UUID target) {
    this.message = message;
    identifier = d.getIdentifier().toString();
    status = d.getStatus().toString();
    targets.add(target);
  }

  public void addData(String data) {
    this.data = data;
  }

  /**
   * Gets the targets of the message. Could be an empty list or a list with one element.
   *
   * @return the list with the targets of the message
   */
  public List<UUID> getTargets() {
    return targets;
  }

  /**
   * Gets the status of the sender.
   *
   * @return the status of the sender as a string. "Client" if sender is a client
   */
  public String getStatus() {
    return status;
  }

  /**
   * Gets the message that is to be sent.
   *
   * @return the message to be sent as a string
   */
  public String getMessage() {
    return message;
  }

  /**
   * Invokes this JSONMessaging object and creates a JSONObject representing it.
   *
   * @return the JSONObject representing this message
   */
  public JSONObject invoke() {
    // This is the modern way to get the date in java. Periods used instead of colons for the
    // JSONTokenizer.
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat date = new SimpleDateFormat("yyyy-mm-dd'T'HH.mm.ssZ");

    return new JSONObject(
        "{ "
            + "node_id: "
            + identifier
            + ", status: "
            + status
            + ", payload: "
            + message
            + ", data: "
            + data
            + ", created_at: "
            + date.format(calendar.getTime())
            + " }");
  }
}
