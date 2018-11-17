package ca.uvic.seng330.assn3.home;

import ca.uvic.seng330.assn3.AuthManager;
import ca.uvic.seng330.assn3.Controller;
import ca.uvic.seng330.assn3.Token;
import ca.uvic.seng330.assn3.ViewTransition;
import ca.uvic.seng330.assn3.Views;

public class HomeController extends Controller {

  private final HomeModel model;
  private AuthManager authManager;

  public HomeController(
      HomeModel model, AuthManager authManager, ViewTransition transitionNotifier) {
    super(authManager, transitionNotifier);
    this.model = model;
    this.authManager = authManager;
  }

  public boolean isAdmin() {
    return authManager.isAdminToken(model.getToken());
  }

  // logout from home view, and return to login screen.
  public void logout() {
    // Get existing session token.
    Token token = model.getToken();

    switchViews(this, Views.LOGIN, token);
  }

  public void cameraGUI() {
    Token token = model.getToken();

    switchViews(this, Views.CAMERA, token);
  }

  public void lightbulbGUI() {
    Token token = model.getToken();

    switchViews(this, Views.LIGHTBULB, token);
  }

  public void smartplugGUI() {
    Token token = model.getToken();

    switchViews(this, Views.SMARTPLUG, token);
  }

  public void thermostatGUI() {
    Token token = model.getToken();

    switchViews(this, Views.THERMOSTAT, token);
  }

  public void adminGUI() {
    Token token = model.getToken();

    switchViews(this, Views.ADMIN, token);
  }
}
