package ca.uvic.seng330.assn3.admin;

import ca.uvic.seng330.assn3.AuthManager;
import ca.uvic.seng330.assn3.Controller;
import ca.uvic.seng330.assn3.DeviceItem;
import ca.uvic.seng330.assn3.Token;
import ca.uvic.seng330.assn3.ViewTransition;
import ca.uvic.seng330.assn3.Views;
import java.util.UUID;
import ca.uvic.seng330.assn3.devices.*;

public class DeviceAdminController extends Controller {
  private final DeviceAdminModel model;

  /**
   * Constructor for Admin controller for the device admin UI.
   *
   * @param model the model for the device admin UI
   * @param authManager the auth manager to manage authentication
   * @param transitionNotifier a function that will allow the controller to invoke a view transition
   */
  public DeviceAdminController(
      DeviceAdminModel model, AuthManager authManager, ViewTransition transitionNotifier) {
    super(authManager, transitionNotifier);
    this.model = model;
  }

  public DeviceItem addNewDevice(String className) {
    return model.addNewDevice(className);
  }

  public boolean removeByUUID(UUID device) {
    return model.removeByUUID(device);
  }

  /** Return to the admin UI. */
  public void adminGUI() {
    model.stopGettingDevices();
    Token token = model.getToken();

    switchViews(this, Views.ADMIN, token);
  }

  public void userDeviceRegistrationGUI() {
    Token token = model.getToken();

    switchViews(this, Views.USER_DEVICE_REG, token);
  
  }

  public void changeDeviceOwner(String oldOwner, String newOwner, String deviceUUID) {
	Hub oldUserHub = model.getHubForUser(oldOwner);
	Hub newUserHub = model.getHubForUser(newOwner);
	Device d = oldUserHub.getDeviceByUUID(UUID.fromString(deviceUUID));
	/**Remove device from old owner, then register device with new selected owner */
	try {
		model.removeDeviceByUUID(UUID.fromString(deviceUUID));
		newUserHub.register(d);
	} catch(HubRegistrationException e) {
		//do nothing
	}
  }
}
