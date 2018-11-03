package ca.uvic.seng330.assn3.Examples;

import ca.uvic.seng330.assn3.Client;
import ca.uvic.seng330.assn3.devices.*;
import java.util.HashMap;
import java.util.UUID;
import org.json.JSONObject;

public final class AndroidClient extends Client {
  private HashMap<UUID, Boolean> lightbulbConditions;

  /**
   * Constructor for the android client that takes a hub.
   *
   * @param m the mediator that this client is under
   */
  public AndroidClient(Mediator m) {
    super(m);

    lightbulbConditions = new HashMap<UUID, Boolean>();
  }

  /**
   * Notify the android client of some message sent to it.
   *
   * @param jsonMessage the message as a JSONObject that's been sent
   */
  @Override
  public void notify(JSONObject jsonMessage) {
    String message = jsonMessage.getString("payload");

    // Needs to be at least this long to check if the message is getCondition.
    if (message.length() < 13) {
      return;
    }

    // If this was targeted then carry out the action if the action is applicable to this device.
    // Don't care about toggle result but we care about the reply from getting the condition.
    if (message.substring(0, 13).equals("getCondition.")) {
      UUID bulb = UUID.fromString(jsonMessage.getString("node_id"));
      Boolean bulbCondition = false;
      boolean handled = false;

      // Try and parse a boolean from the reply, just ignore if it's not valid.
      try {
        bulbCondition = Boolean.parseBoolean(message.substring(13));
        handled = true;
      } catch (Exception e) {
        // Catch block intentionally left blank.
      }

      if (!handled) {
        return;
      }

      // Add the bulb if it's not currently tracked and set its value.
      lightbulbConditions.putIfAbsent(bulb, bulbCondition);
      lightbulbConditions.replace(bulb, bulbCondition);
    }
  }

  public HashMap<UUID, Boolean> getLightbulbConditions() {
    return new HashMap<UUID, Boolean>(lightbulbConditions);
  }
}
