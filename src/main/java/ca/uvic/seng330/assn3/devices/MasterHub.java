package ca.uvic.seng330.assn3.devices;

import ca.uvic.seng330.assn3.AuthManager;
import ca.uvic.seng330.assn3.Token;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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

  /**
   * Adds a new hub for a new user.
   *
   * @param user the username of the user
   */
  public void addUser(String user) {
    allHubs.put(user, new Hub());
  }

  /**
   * Gets the names of all the users.
   *
   * @return the names of all the users as a set
   */
  public Set<String> getAllUsernames() {
    return new HashSet<String>(allHubs.keySet());
  }

  /**
   * Adds a given device to the user's hub.
   *
   * @param token the admin's token
   * @param username the username of the user to add the device to
   * @param device the device to add to the user
   * @return true if success, false otherwise
   */
  public boolean addDeviceForUser(Token token, String username, UUID device) {
    if (!authManager.isAdminToken(token)) {
      return false;
    }

    Hub adminHub = allHubs.get("admin");
    Hub userHub = allHubs.get(username);

    if (!adminHub.doesDeviceExist(device)) {
      return false;
    }

    try {
      userHub.register(adminHub.getDeviceByUUID(device));
    } catch (HubRegistrationException e) {
      return false;
    }

    return true;
  }

  /**
   * Removes a given device from a user.
   *
   * @param token the admin's token
   * @param username the username of the user to remove the device from
   * @param device the device to remove from the user
   * @return true if success, false otherwise
   */
  public boolean removeDeviceFromUser(Token token, String username, UUID device) {
    if (!authManager.isAdminToken(token)) {
      return false;
    }

    Hub adminHub = allHubs.get("admin");
    Hub userHub = allHubs.get(username);

    if (!adminHub.doesDeviceExist(device)) {
      return false;
    }

    try {
      userHub.unregister(adminHub.getDeviceByUUID(device));
    } catch (HubRegistrationException e) {
      return false;
    }

    return true;
  }

  /**
   * Completely removes a device from all users.
   *
   * @param token the admin's token
   * @param device the device to remove completely
   * @return true if success, false otherwise
   */
  public boolean removeDeviceByUUID(Token token, UUID device) {
    if (!authManager.isAdminToken(token)) {
      return false;
    }

    // Remove the device from all the hubs.
    for (String user : allHubs.keySet()) {
      Hub hub = allHubs.get(user);

      if (!hub.doesDeviceExist(device)) {
        continue;
      }

      try {
        hub.unregister(hub.getDeviceByUUID(device));
      } catch (HubRegistrationException e) {
        return false;
      }
    }

    return true;
  }

  /*
   * Return a HashMap of all the hubs currently registered.
   */
  public HashMap<String, Hub> getAllHubs() {
    return allHubs;
  }

  /**
   * Gets the owners of a device separated by commas.
   *
   * @param device the device to get user ownership over
   * @return the owners of the device separated by commas
   */
  public String getDeviceOwners(UUID device) {
    String users = "";
    for (String user : allHubs.keySet()) {
      Hub hub = allHubs.get(user);

      if (!hub.doesDeviceExist(device)) {
        continue;
      }

      users += user + ", ";
    }

    // If the length isn't at least 3 then nothing was added.
    if (users.length() < 3) {
      return null;
    }

    // Remove the trailing comma.
    users = users.substring(0, users.length() - 2);
    return users;
  }
}
