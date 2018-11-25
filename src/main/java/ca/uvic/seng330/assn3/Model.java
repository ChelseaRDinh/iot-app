package ca.uvic.seng330.assn3;

import ca.uvic.seng330.assn3.devices.MasterHub;
import org.json.JSONObject;

public abstract class Model extends Client {
  private final Token token;
  private final MasterHub hub;

  /**
   * Constructor for the model class, which registers itself with the user's hub.
   *
   * @param token the token of the user
   * @param h the master hub with all of the user hubs
   */
  public Model(Token token, MasterHub h) {
    super(h.getHubForUser(token));
    try {
      h.getHubForUser(token).register(this);
    } catch (Exception e) {
      // Won't ever throw for this.
    }

    this.token = token;
    this.hub = h;
  }

  /**
   * Gets the user's token from the model.
   *
   * @return the user's token
   */
  public Token getToken() {
    return new Token(token.getBytes());
  }

  public abstract void notify(JSONObject message);
}
