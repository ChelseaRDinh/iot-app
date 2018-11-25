package ca.uvic.seng330.assn3.devices.smartplug;

import ca.uvic.seng330.assn3.Command;
import ca.uvic.seng330.assn3.Model;
import ca.uvic.seng330.assn3.Token;
import ca.uvic.seng330.assn3.devices.MasterHub;
import ca.uvic.seng330.assn3.devices.SmartPlug;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javafx.beans.property.SimpleBooleanProperty;
import org.json.JSONObject;

public class SmartplugModel extends Model {
  private List<UUID> smartplugs;
  private Map<UUID, SimpleBooleanProperty> smartplugConditions;

  /**
   * Constructor for the model for the Smartplug management UI.
   *
   * @param token the token of the current user
   * @param h the master hub containing all the hubs which may have devices registered to
   */
  public SmartplugModel(Token token, MasterHub h) {
    super(token, h);

    smartplugConditions = new HashMap<UUID, SimpleBooleanProperty>();
    smartplugs = getUUIDOfType(SmartPlug.class.getName());

    for (UUID smartplug : smartplugs) {
      smartplugConditions.put(smartplug, new SimpleBooleanProperty());
      sendMessageToDevice(Command.SMARTPLUG_GET_CONDITION, smartplug);
    }
  }

  /**
   * Gets the amount of smartplugs that the user can see.
   *
   * @return the amount of smartplugs the user can see
   */
  public int getSmartplugCount() {
    return smartplugs.size();
  }

  /**
   * Gets the condition property for the smartplug at a given index.
   *
   * @param index the index of the smartplug
   * @return the property for the smartplug's condition
   */
  public final SimpleBooleanProperty smartplugConditionPropertyAt(int index) {
    return smartplugConditions.get(smartplugs.get(index));
  }

  /**
   * Gets the smartplug condition at a given index.
   *
   * @param index the index of the smartplug
   * @return true if the smartplug is on, false otherwise
   */
  public Boolean getSmartplugConditionAt(int index) {
    return smartplugConditions.get(smartplugs.get(index)).get();
  }

  /**
   * Sets the smartplug condition at a given index.
   *
   * @param index the index of the smartplug
   * @param value the condition to set the smartplug to
   */
  public void setSmartplugConditionAt(int index, Boolean value) {
    if (getSmartplugConditionAt(index) != value) {
      smartplugConditions.get(smartplugs.get(index)).set(value);
      sendMessageToDevice(Command.SMARTPLUG_TOGGLE, smartplugs.get(index));
    }
  }

  @Override
  public void notify(JSONObject jsonMessage) {
    String message = jsonMessage.getString("payload");

    // Try to find a match from the commands that this model handles.
    String match = "";
    String[] commandsToCheck = {CommandsToMessages.get(Command.SMARTPLUG_GET_CONDITION)};

    for (String check : commandsToCheck) {
      if (!message.equals(check)) {
        continue;
      }

      match = check;
      break;
    }

    if (match.equals(CommandsToMessages.get(Command.SMARTPLUG_GET_CONDITION))) {
      Boolean bulbCondition = jsonMessage.getBoolean("data");

      UUID sender = UUID.fromString(jsonMessage.getString("node_id"));

      // Check to see that this UUID has a condition tracked. If not, return.
      if (!smartplugConditions.containsKey(sender)) {
        return;
      }

      smartplugConditions.get(sender).set(bulbCondition);
    }
  }
}
