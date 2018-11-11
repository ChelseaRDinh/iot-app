package ca.uvic.seng330.assn3.devices.smartplug;

import ca.uvic.seng330.assn3.AuthManager;
import ca.uvic.seng330.assn3.Controller;
import ca.uvic.seng330.assn3.Token;
import ca.uvic.seng330.assn3.ViewTransition;
import ca.uvic.seng330.assn3.Views;

public class SmartplugController extends Controller {

  private final SmartplugModel model;

  public SmartplugController(
      SmartplugModel model, AuthManager authManager, ViewTransition transitionNotifier) {
    super(authManager, transitionNotifier);
    this.model = model;
  }

  public void home() {
    Token token = model.getToken();
    switchViews(this, Views.MAIN, token);
  }
}
