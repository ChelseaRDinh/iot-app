package ca.uvic.seng330.assn3.devices;

import ca.uvic.seng330.assn3.AuthManager;
import ca.uvic.seng330.assn3.Token;
import java.util.HashMap;

public class MasterHub {
  private AuthManager authManager;
  private HashMap<String, Hub> allHubs;

  /**
   * The constructor for the master hub.
   *
   * @param manager the auth manager to provide authentication
   * @param users the list of users to manage hubs for
   * @param adminHub the admin's hub with all the devices
   */
  public MasterHub(AuthManager manager, String[] users, Hub adminHub) {
    authManager = manager;
    allHubs = new HashMap<String, Hub>();

    for (String user : users) {
      allHubs.put(user, new Hub());
    }

    allHubs.replace("admin", adminHub);
  }

  /**
   * Gets the hub for the user based on their username, given a valid admin token to have access to
   * any user by string.
   *
   * @param adminToken the token of the admin to verify that permission is granted to all user's
   *     hubs
   * @param user the name of the user to get the hub from
   * @return the hub of the given user
   */
  public Hub getHubForUser(Token adminToken, String user) {
    if (!authManager.isAdminToken(adminToken)) {
      return null;
    }

    return allHubs.get(user);
  }

  /**
   * Gets the user's hub based on their token.
   *
   * @param token the user's token
   * @return the hub for the user of the provided token
   */
  public Hub getHubForUser(Token token) {
    String user = authManager.getUsernameFromToken(token);

    return allHubs.get(user);
  }

  public void addUser(String user) {
    allHubs.put(user, new Hub());
  }
}
