package ca.uvic.seng330.assn3.admin;

import ca.uvic.seng330.assn3.Command;
import ca.uvic.seng330.assn3.Model;
import ca.uvic.seng330.assn3.Token;
import ca.uvic.seng330.assn3.devices.MasterHub;
import org.json.JSONObject;

public class AdminModel extends Model {
  private boolean isHubOn;

  /**
   * Constructor for admin model for the admin UI.
   *
   * @param token the user's token
   * @param h the master hub containing all the user's hubs
   */
  public AdminModel(Token token, MasterHub h) {
    super(token, h);

    sendMessageToAllDevices(Command.HUB_GET_CONDITION);
  }

  /**
   * Shutsdown the hub if it's on, starts it up if it's offline, returns its new status.
   *
   * @return true if on, false if offline
   */
  public boolean toggleHubPower() {
    sendMessageToAllDevices(Command.HUB_TOGGLE_POWER);

    return isHubOn;
  }

  public boolean getIsHubOn() {
    return isHubOn;
  }

  @Override
  public void notify(JSONObject jsonMessage) {
    String message = jsonMessage.getString("payload");

    if (message.equals(CommandsToMessages.get(Command.HUB_GET_CONDITION))
        || message.equals(CommandsToMessages.get(Command.HUB_TOGGLE_POWER))) {
      isHubOn = !jsonMessage.getBoolean("data");
    }
  }
}
