package ca.uvic.seng330.assn3.admin;

import ca.uvic.seng330.assn3.AuthManager;
import ca.uvic.seng330.assn3.Controller;
import ca.uvic.seng330.assn3.DeviceItem;
import ca.uvic.seng330.assn3.Token;
import ca.uvic.seng330.assn3.ViewTransition;
import ca.uvic.seng330.assn3.Views;
import java.util.UUID;

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
}
