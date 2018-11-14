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

  public int getLightbulbCount() {
    return lightbulbs.size();
  }

  public final SimpleBooleanProperty lightbulbConditionPropertyAt(int index) {
    return lightbulbConditions.get(lightbulbs.get(index));
  }

  public Boolean getLightbulbConditionAt(int index) {
    return lightbulbConditions.get(lightbulbs.get(index)).get();
  }

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
    String[] commandsToCheck = {CommandsToMessages.get(Command.LIGHTBULB_GET_CONDITION)};

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
    }
  }
}
