package ca.uvic.seng330.assn3.login;

import ca.uvic.seng330.assn3.AuthManager;
import ca.uvic.seng330.assn3.Controller;
import ca.uvic.seng330.assn3.ViewTransition;
import ca.uvic.seng330.assn3.Views;

public class LoginController extends Controller {

  private final LoginModel model;

  public LoginController(
      LoginModel model, AuthManager authManager, ViewTransition transitionNotifier) {
    super(authManager, transitionNotifier);
    this.model = model;
  }

  public void updateUsername(String username) {
    model.setUsername(username);
  }

  public void updatePassword(String password) {
    model.setPassword(password);
  }

  public void login() {
    // Check to see if the username and password are valid first.
    String token = authManager.getToken(model.getUsername(), model.getPassword());

    if (token.length() > 0) {
      switchViews(this, Views.MAIN, token);
    }
  }
}
