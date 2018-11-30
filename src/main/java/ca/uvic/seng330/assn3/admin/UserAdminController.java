package ca.uvic.seng330.assn3.admin;

import ca.uvic.seng330.assn3.AuthManager;
import ca.uvic.seng330.assn3.Controller;
import ca.uvic.seng330.assn3.Token;
import ca.uvic.seng330.assn3.ViewTransition;
import ca.uvic.seng330.assn3.Views;

public class UserAdminController extends Controller {
  private final UserAdminModel model;

  public UserAdminController(
      UserAdminModel model, AuthManager authManager, ViewTransition transitionNotifier) {
    super(authManager, transitionNotifier);
    this.model = model;
  }

  /** Return back to admin console. */
  public void adminGUI() {
    Token token = model.getToken();

    switchViews(this, Views.ADMIN, token);
  }

  public String[] getUsers() {
	  return authManager.getUsers();
  }
}
