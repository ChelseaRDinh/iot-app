package ca.uvic.seng330.assn3.devices.smartplug;

import ca.uvic.seng330.assn3.Model;
import ca.uvic.seng330.assn3.Token;
import ca.uvic.seng330.assn3.devices.MasterHub;
import org.json.JSONObject;

public class SmartplugModel extends Model {
  public SmartplugModel(Token token, MasterHub h) {
    super(token, h);
  }

  @Override
  public void notify(JSONObject message) {}
}
