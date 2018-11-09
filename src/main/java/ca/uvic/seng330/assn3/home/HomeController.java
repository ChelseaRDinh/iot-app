package ca.uvic.seng330.assn3.home;

import ca.uvic.seng330.assn3.AuthManager;
import ca.uvic.seng330.assn3.Controller;
import ca.uvic.seng330.assn3.Token;
import ca.uvic.seng330.assn3.ViewTransition;
import ca.uvic.seng330.assn3.Views;

public class HomeController extends Controller {

  private final HomeModel model;

  public HomeController(
      HomeModel model, AuthManager authManager, ViewTransition transitionNotifier) {
    super(authManager, transitionNotifier);
    this.model = model;
  }

  //logout from home view, and return to login screen.
  public void logout() {
    //Get existing session token.
    Token token = model.getToken();
    switchViews(this, Views.LOGIN, token);
  }
}
