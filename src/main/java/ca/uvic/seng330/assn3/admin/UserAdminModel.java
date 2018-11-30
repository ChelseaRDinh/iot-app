package ca.uvic.seng330.assn3.admin;

import ca.uvic.seng330.assn3.User;
import ca.uvic.seng330.assn3.Model;
import ca.uvic.seng330.assn3.Token;
import ca.uvic.seng330.assn3.devices.MasterHub;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javafx.beans.property.SimpleBooleanProperty;
import org.json.JSONObject;

public class UserAdminModel extends Model {
	public UserAdminModel(Token token, MasterHub h) {
		super(token, h);
	}

	@Override
	public void notify(JSONObject message) {}
}