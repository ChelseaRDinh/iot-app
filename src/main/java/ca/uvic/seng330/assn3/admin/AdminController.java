package ca.uvic.seng330.assn3.admin;

import ca.uvic.seng330.assn3.AuthManager;
import ca.uvic.seng330.assn3.Controller;
import ca.uvic.seng330.assn3.Token;
import ca.uvic.seng330.assn3.ViewTransition;
import ca.uvic.seng330.assn3.Views;

public class AdminController extends Controller {
  private final AdminModel model;

  /**
   * Constructor for Admin controller for the admin UI.
   *
   * @param model the model for the admin UI
   * @param authManager the auth manager to manage authentication
   * @param transitionNotifier a function that will allow the controller to invoke a view transition
   */
  public AdminController(
      AdminModel model, AuthManager authManager, ViewTransition transitionNotifier) {
    super(authManager, transitionNotifier);
    this.model = model;
  }

  /** Open the user management UI. */
  public void manageUsersGUI() {
    Token token = model.getToken();

    switchViews(this, Views.USER_ADMIN, token);
  }

  /** Open the device management UI. */
  public void manageDevicesGUI() {
    Token token = model.getToken();

    switchViews(this, Views.DEVICE_ADMIN, token);
  }

  /** Return to the home UI. */
  public void homeGUI() {
    Token token = model.getToken();

    switchViews(this, Views.MAIN, token);
  }
}
