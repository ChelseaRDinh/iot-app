package ca.uvic.seng330.assn3;

import ca.uvic.seng330.assn3.devices.Mediator;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.json.JSONObject;

public abstract class Client {
  protected static final Map<Command, String> CommandsToMessages;

  static {
    Map<Command, String> commandMap = new HashMap<Command, String>();
    commandMap.put(Command.LIGHTBULB_GET_CONDITION, "getCondition");
    commandMap.put(Command.LIGHTBULB_TOGGLE, "toggle");
    commandMap.put(Command.THERMOSTAT_GET_CONDITION, "getCondition");
    commandMap.put(Command.THERMOSTAT_TOGGLE, "toggle");
    commandMap.put(Command.THERMOSTAT_SET_TEMP, "setTemp");
    commandMap.put(Command.THERMOSTAT_GET_TEMP, "getTemp");
    commandMap.put(Command.THERMOSTAT_SET_UNIT, "setUnit");
    commandMap.put(Command.THERMOSTAT_GET_UNIT, "getUnit");
    commandMap.put(Command.SMARTPLUG_GET_CONDITION, "getCondition");
    commandMap.put(Command.SMARTPLUG_TOGGLE, "toggle");
    commandMap.put(Command.CAMERA_GET_CONDITION, "getCondition");
    commandMap.put(Command.CAMERA_TOGGLE, "toggle");
    commandMap.put(Command.CAMERA_IS_RECORDING, "isRecording");
    commandMap.put(Command.CAMERA_RECORD, "record");
    commandMap.put(Command.CAMERA_STOP_RECORD, "stopRecording");

    // Make sure all Command are in the map.
    assert commandMap.size() == Command.values().length;

    CommandsToMessages = Collections.unmodifiableMap(commandMap);
  }

  protected UUID uuid;
  private Mediator mediator;

  public Client(Mediator m) {
    uuid = UUID.randomUUID();
    mediator = m;
  }

  public final UUID getIdentifier() {
    return uuid;
  }

  /**
   * Gets the list of UUIDs from the mediator of a given type.
   *
   * @param type the type of the class you wish to get the UUIDs of
   * @return the list of UUIDs who belong to objects of type
   */
  public final List<UUID> getUUIDOfType(String type) {
    return mediator.getUUIDOfType(type);
  }

  public final void sendMessageToDevice(Command command, UUID device, String data) {
    JSONMessaging message = new JSONMessaging(this, CommandsToMessages.get(command), device);
    message.addData(data);
    mediator.alert(message);
  }

  public final void sendMessageToDevice(Command command, UUID device) {
    mediator.alert(new JSONMessaging(this, CommandsToMessages.get(command), device));
  }

  public final void sendMessageToDevices(Command command, List<UUID> devices) {
    mediator.alert(new JSONMessaging(this, CommandsToMessages.get(command), devices));
  }

  public final void sendMessageToAllDevices(Command command) {
    mediator.alert(new JSONMessaging(this, CommandsToMessages.get(command)));
  }

  public abstract void notify(JSONObject message);
}
