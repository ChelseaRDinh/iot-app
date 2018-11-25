package ca.uvic.seng330.assn3.login;

import ca.uvic.seng330.assn3.AuthManager;
import ca.uvic.seng330.assn3.Controller;
import ca.uvic.seng330.assn3.Token;
import ca.uvic.seng330.assn3.ViewTransition;
import ca.uvic.seng330.assn3.Views;

public class LoginController extends Controller {

  private final LoginModel model;

  /**
   * Constructor for the Login Controller.
   *
   * @param model the model for the controller
   * @param authManager the auth manager to manage authentication
   * @param transitionNotifier a function that will allow the controller to invoke a view transition
   */
  public LoginController(
      LoginModel model, AuthManager authManager, ViewTransition transitionNotifier) {
    super(authManager, transitionNotifier);
    this.model = model;
  }

  /**
   * Updates the model's username value with a given username.
   *
   * @param username the username to set
   */
  public void updateUsername(String username) {
    model.setUsername(username);
  }

  /**
   * Updates the model's password value with a given password.
   *
   * @param password the password to set
   */
  public void updatePassword(String password) {
    model.setPassword(password);
  }

  /**
   * Checks to see if the given credentials are valid for a user.
   *
   * @return true if given credentials are valid, false otherwise.
   */
  public boolean isValidLogin() {
    Token token = authManager.getToken(model.getUsername(), model.getPassword());

    return authManager.isValidToken(token);
  }

  /**
   * Checks to see if the given credentials are valid for the admin.
   *
   * @return true if given credentials are for the admin, false otherwise.
   */
  public boolean isAdminUser() {
    Token token = authManager.getToken(model.getUsername(), model.getPassword());
    return authManager.isAdminToken(token);
  }

  /**
   * Try to perform the login action with the current username and password. Will try and switch
   * views if the token is valid.
   */
  public void login() {
    // Check to see if the username and password are valid first.
    Token token = authManager.getToken(model.getUsername(), model.getPassword());

    if (token != null) {
      switchViews(this, Views.MAIN, token);
    }
  }
}
