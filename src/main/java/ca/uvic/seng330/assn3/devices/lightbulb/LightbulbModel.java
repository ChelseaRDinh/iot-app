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

  public LightbulbModel(Token token, MasterHub h) {
    super(token, h);

    lightbulbConditions = new HashMap<UUID, SimpleBooleanProperty>();
    lightbulbs = getUUIDOfType(Lightbulb.class.getName());

    for (UUID lightbulb : lightbulbs) {
      lightbulbConditions.put(lightbulb, new SimpleBooleanProperty());
      sendMessageToDevice(Command.LIGHTBULB_GET_CONDITION, lightbulb);
    }
  }

  public int getCount() {
    return lightbulbs.size();
  }

  public final SimpleBooleanProperty ConditionPropertyAt(int index) {
    return lightbulbConditions.get(lightbulbs.get(index));
  }

  public Boolean getConditionAt(int index) {
    return lightbulbConditions.get(lightbulbs.get(index)).get();
  }

  public void setConditionAt(int index, Boolean value) {
    if (getConditionAt(index) != value) {
      lightbulbConditions.get(lightbulbs.get(index)).set(value);
      sendMessageToDevice(Command.LIGHTBULB_TOGGLE, lightbulbs.get(index));
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
    String[] commandsToCheck = {CommandsToMessages.get(Command.LIGHTBULB_GET_CONDITION)};

    for (String check : commandsToCheck) {
      if (!before.equals(check)) {
        continue;
      }

      match = check;
      break;
    }

    if (match.equals(CommandsToMessages.get(Command.LIGHTBULB_GET_CONDITION))) {
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
      if (!lightbulbConditions.containsKey(sender)) {
        return;
      }

      lightbulbConditions.get(sender).set(bulbCondition);
    }
  }
}
