package ca.uvic.seng330.assn3.home;

import ca.uvic.seng330.assn3.AuthManager;
import ca.uvic.seng330.assn3.Controller;
import ca.uvic.seng330.assn3.Token;
import ca.uvic.seng330.assn3.ViewTransition;
import ca.uvic.seng330.assn3.Views;

public class HomeController extends Controller {

  private final HomeModel model;
  private AuthManager authManager;

  /**
   * Constructor for the Home Controller.
   *
   * @param model the model for the controller
   * @param authManager the auth manager to manage authentication
   * @param transitionNotifier a function that will allow the controller to invoke a view transition
   */
  public HomeController(
      HomeModel model, AuthManager authManager, ViewTransition transitionNotifier) {
    super(authManager, transitionNotifier);
    this.model = model;
    this.authManager = authManager;
  }

  /**
   * Returns whether or not the current user is an admin.
   *
   * @return true if the current user is an admin, false otherwise
   */
  public boolean isAdmin() {
    return authManager.isAdminToken(model.getToken());
  }

  /** Logout from home view, and return to login screen. */
  public void logout() {
    // Get existing session token.
    Token token = model.getToken();

    switchViews(this, Views.LOGIN, token);
  }

  /** Open the camera management UI. */
  public void cameraGUI() {
    Token token = model.getToken();

    switchViews(this, Views.CAMERA, token);
  }

  /** Open the lightbulb management UI. */
  public void lightbulbGUI() {
    Token token = model.getToken();

    switchViews(this, Views.LIGHTBULB, token);
  }

  /** Open the smartplug management UI. */
  public void smartplugGUI() {
    Token token = model.getToken();

    switchViews(this, Views.SMARTPLUG, token);
  }

  /** Open the thermostat management UI. */
  public void thermostatGUI() {
    Token token = model.getToken();

    switchViews(this, Views.THERMOSTAT, token);
  }

  /** Open the admin UI. */
  public void adminGUI() {
    Token token = model.getToken();

    switchViews(this, Views.ADMIN, token);
  }
}
