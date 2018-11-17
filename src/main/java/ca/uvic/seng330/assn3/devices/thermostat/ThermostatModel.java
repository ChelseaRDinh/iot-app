package ca.uvic.seng330.assn3.devices.thermostat;

import ca.uvic.seng330.assn3.Command;
import ca.uvic.seng330.assn3.Model;
import ca.uvic.seng330.assn3.Token;
import ca.uvic.seng330.assn3.devices.MasterHub;
import ca.uvic.seng330.assn3.devices.Thermostat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import org.json.JSONObject;

public class ThermostatModel extends Model {
  private List<UUID> thermostats;
  private Map<UUID, SimpleFloatProperty> thermostatValues;
  private Map<UUID, SimpleBooleanProperty> thermostatIsCelsius;
  private Map<UUID, SimpleBooleanProperty> thermostatConditions;

  public ThermostatModel(Token token, MasterHub h) {
    super(token, h);

    thermostatValues = new HashMap<UUID, SimpleFloatProperty>();
    thermostatIsCelsius = new HashMap<UUID, SimpleBooleanProperty>();
    thermostatConditions = new HashMap<UUID, SimpleBooleanProperty>();

    thermostats = getUUIDOfType(Thermostat.class.getName());

    for (UUID thermostat : thermostats) {
      thermostatValues.put(thermostat, new SimpleFloatProperty());
      thermostatIsCelsius.put(thermostat, new SimpleBooleanProperty());
      thermostatConditions.put(thermostat, new SimpleBooleanProperty());
      sendMessageToDevice(Command.THERMOSTAT_GET_TEMP, thermostat);
      sendMessageToDevice(Command.THERMOSTAT_GET_UNIT, thermostat);
      sendMessageToDevice(Command.THERMOSTAT_GET_CONDITION, thermostat);
    }
  }

  public int getThermostatCount() {
    return thermostats.size();
  }

  public final SimpleBooleanProperty thermostatIsCelsiusPropertyAt(int index) {
    return thermostatIsCelsius.get(thermostats.get(index));
  }

  public Boolean getThermostatIsCelsiusAt(int index) {
    return thermostatIsCelsius.get(thermostats.get(index)).get();
  }

  public void setThermostatIsCelsiusAt(int index, Boolean value) {
    if (getThermostatIsCelsiusAt(index) != value) {
      thermostatIsCelsius.get(thermostats.get(index)).set(value);
      sendMessageToDevice(Command.THERMOSTAT_SET_UNIT, thermostats.get(index), value.toString());
    }
  }

  public final SimpleBooleanProperty thermostatConditionPropertyAt(int index) {
    return thermostatConditions.get(thermostats.get(index));
  }

  public Boolean getThermostatConditionAt(int index) {
    return thermostatConditions.get(thermostats.get(index)).get();
  }

  public void setThermostaConditionAt(int index, Boolean value) {
    if (getThermostatConditionAt(index) != value) {
      thermostatConditions.get(thermostats.get(index)).set(value);
      sendMessageToDevice(Command.THERMOSTAT_TOGGLE, thermostats.get(index));
    }
  }

  public final SimpleFloatProperty thermostatValuePropertyAt(int index) {
    return thermostatValues.get(thermostats.get(index));
  }

  public Float getThermostatValueAt(int index) {
    return thermostatValues.get(thermostats.get(index)).get();
  }

  public void setThermostatValueAt(int index, Float value) {
    if (getThermostatValueAt(index) != value) {
      thermostatValues.get(thermostats.get(index)).set(value);
      sendMessageToDevice(Command.THERMOSTAT_SET_TEMP, thermostats.get(index), value.toString());
    }
  }

  @Override
  public void notify(JSONObject jsonMessage) {
    String message = jsonMessage.getString("payload");

    // Try to find a match from the commands that this model handles.
    String match = "";
    String[] commandsToCheck = {
      CommandsToMessages.get(Command.THERMOSTAT_GET_TEMP),
      CommandsToMessages.get(Command.THERMOSTAT_GET_UNIT),
      CommandsToMessages.get(Command.THERMOSTAT_GET_CONDITION)
    };

    for (String check : commandsToCheck) {
      if (!message.equals(check)) {
        continue;
      }

      match = check;
      break;
    }

    if (match.equals(CommandsToMessages.get(Command.THERMOSTAT_GET_TEMP))) {
      Float temperature = jsonMessage.getFloat("data");

      UUID sender = UUID.fromString(jsonMessage.getString("node_id"));

      // Check to see that this UUID has a condition tracked. If not, return.
      if (!thermostatValues.containsKey(sender)) {
        return;
      }

      thermostatValues.get(sender).set(temperature);
    }

    if (match.equals(CommandsToMessages.get(Command.THERMOSTAT_GET_UNIT))) {
      Boolean isCelsius = jsonMessage.getBoolean("data");

      UUID sender = UUID.fromString(jsonMessage.getString("node_id"));

      // Check to see that this UUID has a condition tracked. If not, return.
      if (!thermostatIsCelsius.containsKey(sender)) {
        return;
      }

      thermostatIsCelsius.get(sender).set(isCelsius);
    }

    if (match.equals(CommandsToMessages.get(Command.THERMOSTAT_GET_CONDITION))) {
      Boolean condition = jsonMessage.getBoolean("data");

      UUID sender = UUID.fromString(jsonMessage.getString("node_id"));

      // Check to see that this UUID has a condition tracked. If not, return.
      if (!thermostatConditions.containsKey(sender)) {
        return;
      }

      thermostatConditions.get(sender).set(condition);
    }
  }
}
