package ca.uvic.seng330.assn3;

import ca.uvic.seng330.assn3.devices.MasterHub;
import javafx.beans.property.SimpleBooleanProperty;
import org.json.JSONObject;

public abstract class Model extends Client {
  private final Token token;
  private final MasterHub hub;

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

  public Token getToken() {
    return new Token(token.getBytes());
  }

  public abstract void notify(JSONObject message);

  /*
  * Default method, created so that it can be overriden by lightbulb and smartplug classes.
  */
  public Boolean getConditionAt(int index) {
    return true;
  }

  /*
  * Default method, created so that it can be overriden by lightbulb and smartplug classes.
  */
  public SimpleBooleanProperty ConditionPropertyAt(int index) {
    return new SimpleBooleanProperty();
  }
}
