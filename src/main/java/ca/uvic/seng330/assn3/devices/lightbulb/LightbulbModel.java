package ca.uvic.seng330.assn3.devices.lightbulb;

import ca.uvic.seng330.assn3.Command;
import ca.uvic.seng330.assn3.Model;
import ca.uvic.seng330.assn3.Token;
import ca.uvic.seng330.assn3.devices.Lightbulb;
import ca.uvic.seng330.assn3.devices.MasterHub;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javafx.beans.property.SimpleBooleanProperty;
import org.json.JSONObject;

public class LightbulbModel extends Model {
  private List<UUID> lightbulbs;
  private Map<UUID, SimpleBooleanProperty> lightbulbConditions;

  /**
   * Constructor for the model for the Lightbulb management UI.
   *
   * @param token the token of the current user
   * @param h the master hub containing all the hubs which may have devices registered to
   */
  public LightbulbModel(Token token, MasterHub h) {
    super(token, h);

    lightbulbConditions = new HashMap<UUID, SimpleBooleanProperty>();
    lightbulbs = getUUIDOfType(Lightbulb.class.getName());

    for (UUID lightbulb : lightbulbs) {
      lightbulbConditions.put(lightbulb, new SimpleBooleanProperty());
      sendMessageToDevice(Command.LIGHTBULB_GET_CONDITION, lightbulb);
    }
  }

  public int getLightbulbCount() {
    return lightbulbs.size();
  }

  /**
   * Gets the property of the lightbulb's condition at a given index in the model.
   *
   * @param index the index of the lightbulb in the model
   * @return the property of the lightbulb's condition
   */
  public final SimpleBooleanProperty lightbulbConditionPropertyAt(int index) {
    return lightbulbConditions.get(lightbulbs.get(index));
  }

  /**
   * Gets the lightbulb condition at a given index in the model.
   *
   * @param index the index of the lightbulb in the model
   * @return true if the lightbulb is on, false otherwise
   */
  public Boolean getLightbulbConditionAt(int index) {
    return lightbulbConditions.get(lightbulbs.get(index)).get();
  }

  /**
   * Sets the lightbulb condition at a given index in the model.
   *
   * @param index the index of the lightbulb in the model
   * @param value the condition to set the lightbulb to
   */
  public void setLightbulbConditionAt(int index, Boolean value) {
    if (getLightbulbConditionAt(index) != value) {
      lightbulbConditions.get(lightbulbs.get(index)).set(value);
      sendMessageToDevice(Command.LIGHTBULB_TOGGLE, lightbulbs.get(index));
    }
  }

  @Override
  public void notify(JSONObject jsonMessage) {
    String message = jsonMessage.getString("payload");

    // Try to find a match from the commands that this model handles.
    String match = "";
    String[] commandsToCheck = {
      CommandsToMessages.get(Command.LIGHTBULB_GET_CONDITION),
      CommandsToMessages.get(Command.LIGHTBULB_CONDITION_CHANGED)
    };

    for (String check : commandsToCheck) {
      if (!message.equals(check)) {
        continue;
      }

      match = check;
      break;
    }

    if (match.equals(CommandsToMessages.get(Command.LIGHTBULB_GET_CONDITION))) {
      Boolean bulbCondition = jsonMessage.getBoolean("data");

      UUID sender = UUID.fromString(jsonMessage.getString("node_id"));

      // Check to see that this UUID has a condition tracked. If not, return.
      if (!lightbulbConditions.containsKey(sender)) {
        return;
      }

      lightbulbConditions.get(sender).set(bulbCondition);
    } else if (match.equals(CommandsToMessages.get(Command.LIGHTBULB_CONDITION_CHANGED))) {
      sendMessageToDevice(
          Command.LIGHTBULB_GET_CONDITION, UUID.fromString(jsonMessage.getString("node_id")));
    }
  }
}
