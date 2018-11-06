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
