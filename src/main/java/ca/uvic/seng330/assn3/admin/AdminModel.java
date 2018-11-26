package ca.uvic.seng330.assn3.admin;

import ca.uvic.seng330.assn3.Model;
import ca.uvic.seng330.assn3.Token;
import ca.uvic.seng330.assn3.devices.MasterHub;
import org.json.JSONObject;

public class AdminModel extends Model {
	private Token token;
	private MasterHub h;
	public AdminModel(Token token, MasterHub h) {
		super(token, h);
		this.token = token;
		this.h = h;
	}

	public MasterHub getMasterHub() {
		return h;
	}

	@Override
	public void notify(JSONObject message) {}
}
