package ca.uvic.seng330.assn3.admin;

import ca.uvic.seng330.assn3.Model;
import ca.uvic.seng330.assn3.Token;
import ca.uvic.seng330.assn3.devices.Hub;
import ca.uvic.seng330.assn3.devices.MasterHub;
import java.util.*;
import org.json.JSONObject;

public class DeviceRegistrationModel extends Model {
  private MasterHub h;
  private Token token;

  /**
   * Constructor for the model of the device admin UI.
   *
   * @param token the user's token
   * @param h the hub containing all the hubs
   */
  public DeviceRegistrationModel(Token token, MasterHub h) {
    super(token, h);

    this.h = h;
    this.token = token;
  }

  /*
   * Get HashSet of all usernames.
   */
  public Set<String> getAllUsernames() {
    return h.getAllUsernames();
  }

  public Hub getHubForUser(String user) {
    return h.getHubForUser(token, user);
  }

  @Override
  public void notify(JSONObject message) {}
}
