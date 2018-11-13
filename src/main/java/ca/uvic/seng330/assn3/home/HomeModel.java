package ca.uvic.seng330.assn3.home;

import ca.uvic.seng330.assn3.Model;
import ca.uvic.seng330.assn3.Token;
import ca.uvic.seng330.assn3.devices.MasterHub;
import org.json.JSONObject;

public class HomeModel extends Model {
  public HomeModel(Token token, MasterHub h) {
    super(token, h);
  }

  @Override
  public void notify(JSONObject message) {}
}
