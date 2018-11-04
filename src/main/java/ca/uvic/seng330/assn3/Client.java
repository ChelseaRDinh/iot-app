package ca.uvic.seng330.assn3;

import ca.uvic.seng330.assn3.devices.Hub;
import ca.uvic.seng330.assn3.devices.Mediator;
import java.util.UUID;
import org.json.JSONObject;

public abstract class Client {
  protected UUID uuid;
  private Mediator mediator;

  public Client(Mediator m) {
    uuid = UUID.randomUUID();
    mediator = m;
  }

  public final UUID getIdentifier() {
    return uuid;
  }

  public final void toggleAllLightbulbs() {
    mediator.alert(this, "toggle");
  }

  public final void toggleLightbulb(UUID lightbulb) {
    String message = Hub.targetJSONMessage(lightbulb.toString(), "toggle");
    mediator.alert(this, message);
  }

  public final void retrieveLightbulbCondition(UUID lightbulb) {
    String message = Hub.targetJSONMessage(lightbulb.toString(), "getCondition");
    mediator.alert(this, message);
  }

  /**
   * This function alerts the hub with a message from the client. It protects the hub from the
   * client implementations.
   *
   * @param message the message to send to the hub.
   */
  protected final void alertMediator(String message) {
    mediator.alert(this, message);
  }

  public abstract void notify(JSONObject message);
}
