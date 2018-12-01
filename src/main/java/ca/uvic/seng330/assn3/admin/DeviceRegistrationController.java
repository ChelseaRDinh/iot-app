package ca.uvic.seng330.assn3.admin;

import ca.uvic.seng330.assn3.AuthManager;
import ca.uvic.seng330.assn3.Controller;
import ca.uvic.seng330.assn3.ViewTransition;
import ca.uvic.seng330.assn3.devices.*;

public class DeviceRegistrationController extends Controller {
  private final DeviceRegistrationModel model;

  /**
   * Constructor for Admin controller for the device admin UI.
   *
   * @param model the model for the device admin UI
   * @param authManager the auth manager to manage authentication
   * @param transitionNotifier a function that will allow the controller to invoke a view transition
   */
  public DeviceRegistrationController(
      DeviceRegistrationModel model, AuthManager authManager, ViewTransition transitionNotifier) {
    super(authManager, transitionNotifier);
    this.model = model;
  }
}
