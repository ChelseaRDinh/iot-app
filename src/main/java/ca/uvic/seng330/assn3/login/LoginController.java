package ca.uvic.seng330.assn3.login;

import ca.uvic.seng330.assn3.AuthManager;
import ca.uvic.seng330.assn3.Controller;
import ca.uvic.seng330.assn3.Token;
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

  /*
   * If the user/pass combo is found in master DB, return token.
   * Otherwise, return null.
   */
  public boolean isValidLogin() {
    Token token = authManager.getToken(model.getUsername(), model.getPassword());
    if(authManager.isValidToken(token) == true) {
      return true;
    } else {
      return false;
    }
  }

  public void login() {
    // Check to see if the username and password are valid first.
    Token token = authManager.getToken(model.getUsername(), model.getPassword());

    if (token != null) {
      switchViews(this, Views.MAIN, token);
    }
  }
}
