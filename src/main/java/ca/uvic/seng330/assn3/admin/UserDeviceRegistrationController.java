package ca.uvic.seng330.assn3.admin;

import ca.uvic.seng330.assn3.AuthManager;
import ca.uvic.seng330.assn3.Controller;
import ca.uvic.seng330.assn3.DeviceItem;
import ca.uvic.seng330.assn3.Token;
import ca.uvic.seng330.assn3.ViewTransition;
import ca.uvic.seng330.assn3.Views;
import java.util.UUID;

public class UserDeviceRegistrationController extends Controller {
  private final UserDeviceRegistrationModel model;

  /**
   * Constructor for Admin controller for the device admin UI.
   *
   * @param model the model for the device admin UI
   * @param authManager the auth manager to manage authentication
   * @param transitionNotifier a function that will allow the controller to invoke a view transition
   */
  public UserDeviceRegistrationController(
      UserDeviceRegistrationModel model,
      AuthManager authManager,
      ViewTransition transitionNotifier) {
    super(authManager, transitionNotifier);
    this.model = model;
  }

  public DeviceItem addNewDevice(String className) {
    return model.addNewDevice(className);
  }

  public boolean removeByUUID(UUID device) {
    return model.removeByUUID(device);
  }

  public void deviceAdminGUI() {
    Token token = model.getToken();

    switchViews(this, Views.DEVICE_ADMIN, token);
  }

  public String[] getUsers() {
    return authManager.getUsers();
  }
}
