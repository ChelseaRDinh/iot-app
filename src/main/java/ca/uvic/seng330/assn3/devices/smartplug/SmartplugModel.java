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

  public SmartplugModel(Token token, MasterHub h) {
    super(token, h);

    smartplugConditions = new HashMap<UUID, SimpleBooleanProperty>();
    smartplugs = getUUIDOfType(SmartPlug.class.getName());

    for (UUID smartplug : smartplugs) {
      smartplugConditions.put(smartplug, new SimpleBooleanProperty());
      sendMessageToDevice(Command.SMARTPLUG_GET_CONDITION, smartplug);
    }
  }

  public int getCount() {
    return smartplugs.size();
  }

  public final SimpleBooleanProperty ConditionPropertyAt(int index) {
    return smartplugConditions.get(smartplugs.get(index));
  }

  public Boolean getConditionAt(int index) {
    return smartplugConditions.get(smartplugs.get(index)).get();
  }

  public void setConditionAt(int index, Boolean value) {
    if (getConditionAt(index) != value) {
      smartplugConditions.get(smartplugs.get(index)).set(value);
      sendMessageToDevice(Command.SMARTPLUG_TOGGLE, smartplugs.get(index));
    }
  }

  @Override
  public void notify(JSONObject jsonMessage) {
    String message = jsonMessage.getString("payload");

    int separator = message.indexOf('.');

    // Return if the separator doesn't exist or there isn't any data afterwards.
    if (separator == -1 || separator == message.length()) {
      return;
    }

    String before = message.substring(0, separator);
    String after = message.substring(separator + 1);

    // Try to find a match from the commands that this model handles.
    String match = "";
    String[] commandsToCheck = {CommandsToMessages.get(Command.SMARTPLUG_GET_CONDITION)};

    for (String check : commandsToCheck) {
      if (!before.equals(check)) {
        continue;
      }

      match = check;
      break;
    }

    if (match.equals(CommandsToMessages.get(Command.SMARTPLUG_GET_CONDITION))) {
      boolean handled = false;
      Boolean bulbCondition = false;

      try {
        bulbCondition = Boolean.parseBoolean(after);
        handled = true;
      } catch (Exception e) {
        // Catch block intentionally left blank.
      }

      if (!handled) {
        return;
      }

      UUID sender = UUID.fromString(jsonMessage.getString("node_id"));

      // Check to see that this UUID has a condition tracked. If not, return.
      if (!smartplugConditions.containsKey(sender)) {
        return;
      }

      smartplugConditions.get(sender).set(bulbCondition);
    }
  }
}
