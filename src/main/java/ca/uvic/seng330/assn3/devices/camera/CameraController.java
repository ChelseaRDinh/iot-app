package ca.uvic.seng330.assn3.devices.camera;

import ca.uvic.seng330.assn3.AuthManager;
import ca.uvic.seng330.assn3.Controller;
import ca.uvic.seng330.assn3.Token;
import ca.uvic.seng330.assn3.ViewTransition;
import ca.uvic.seng330.assn3.Views;

public class CameraController extends Controller {

  private final CameraModel model;

  public CameraController(
      CameraModel model, AuthManager authManager, ViewTransition transitionNotifier) {
    super(authManager, transitionNotifier);
    this.model = model;
  }

  //return to home dashboard
  public void home() {
    Token token = model.getToken();

    switchViews(this, Views.MAIN, token);
  }
}
