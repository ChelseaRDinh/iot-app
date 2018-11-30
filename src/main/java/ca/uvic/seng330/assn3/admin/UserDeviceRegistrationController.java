package ca.uvic.seng330.assn3.admin;

import ca.uvic.seng330.assn3.AuthManager;
import ca.uvic.seng330.assn3.Controller;
import ca.uvic.seng330.assn3.Token;
import ca.uvic.seng330.assn3.ViewTransition;
import ca.uvic.seng330.assn3.Views;

public class UserDeviceRegistrationController extends Controller {
  private final UserDeviceRegistrationModel model;

  public UserDeviceRegistrationController(
      UserDeviceRegistrationModel model,
      AuthManager authManager,
      ViewTransition transitionNotifier) {
    super(authManager, transitionNotifier);
    this.model = model;
  }

  /** Return to Device Admin GUI */
  public void deviceAdminGUI() {
    Token token = model.getToken();

    switchViews(this, Views.DEVICE_ADMIN, token);
  }
}
