package ca.uvic.seng330.assn3;

import ca.uvic.seng330.assn3.devices.Hub;
import ca.uvic.seng330.assn3.devices.MasterHub;
import java.util.List;
import java.util.UUID;

public class Model {
  private final Token token;
  private final MasterHub hub;

  public Model(Token token, MasterHub h) {
    this.token = token;
    this.hub = h;
  }

  public Token getToken() {
    return new Token(token.getBytes());
  }

  /**
   * Gets the list of UUIDs for this user's hub given by the model's token.
   *
   * @param type the type of the class you wish to get the UUIDs of
   * @return the list of UUIDs who belong to objects of type
   */
  public List<UUID> getUUIDOfType(String type) {
    Hub h = hub.getHubForUser(token);

    return h.getUUIDOfType(type);
  }
}
