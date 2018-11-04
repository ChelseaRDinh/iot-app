package ca.uvic.seng330.assn3.home;

import ca.uvic.seng330.assn3.AuthManager;
import ca.uvic.seng330.assn3.Controller;
import ca.uvic.seng330.assn3.ViewTransition;

public class HomeController extends Controller {

  private final HomeModel model;

  public HomeController(
      HomeModel model, AuthManager authManager, ViewTransition transitionNotifier) {
    super(authManager, transitionNotifier);
    this.model = model;
  }
}
