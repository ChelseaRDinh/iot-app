package ca.uvic.seng330.assn3;

import ca.uvic.seng330.assn3.devices.Device;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.json.JSONObject;

public final class JSONMessaging {
  private String identifier;
  private String status;
  private String message;

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
            + ", created_at: "
            + date.format(calendar.getTime())
            + " }");
  }
}
