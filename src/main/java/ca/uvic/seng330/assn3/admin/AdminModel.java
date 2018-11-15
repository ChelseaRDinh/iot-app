package ca.uvic.seng330.assn3.admin;

import ca.uvic.seng330.assn3.Command;
import ca.uvic.seng330.assn3.Model;
import ca.uvic.seng330.assn3.Token;
import ca.uvic.seng330.assn3.devices.Lightbulb;
import ca.uvic.seng330.assn3.devices.MasterHub;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javafx.beans.property.SimpleBooleanProperty;
import org.json.JSONObject;

public class AdminModel extends Model {
    public AdminModel(Token token, MasterHub h) {
        super(token, h);
      }
    
      @Override
      public void notify(JSONObject message) {}
}
