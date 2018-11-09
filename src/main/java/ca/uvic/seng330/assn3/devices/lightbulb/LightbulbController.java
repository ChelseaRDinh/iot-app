package ca.uvic.seng330.assn3.devices.lightbulb;

import ca.uvic.seng330.assn3.AuthManager;
import ca.uvic.seng330.assn3.Controller;
import ca.uvic.seng330.assn3.ViewTransition;

public class LightbulbController extends Controller {

  private final LightbulbModel model;

  public LightbulbController(
      LightbulbModel model, AuthManager authManager, ViewTransition transitionNotifier) {
    super(authManager, transitionNotifier);
    this.model = model;
  }
}
