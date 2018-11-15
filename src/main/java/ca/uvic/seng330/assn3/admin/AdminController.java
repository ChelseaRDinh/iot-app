package ca.uvic.seng330.assn3.admin;

import ca.uvic.seng330.assn3.AuthManager;
import ca.uvic.seng330.assn3.Controller;
import ca.uvic.seng330.assn3.Token;
import ca.uvic.seng330.assn3.ViewTransition;
import ca.uvic.seng330.assn3.Views;

public class AdminController extends Controller {

  private final AdminModel model;

  public AdminController(
      AdminModel model, AuthManager authManager, ViewTransition transitionNotifier) {
    super(authManager, transitionNotifier);
    this.model = model;
  }

  public void manageUsersGUI() {
    Token token = model.getToken();
    switchViews(this, Views.MANAGE_USERS, token);
  }
}
