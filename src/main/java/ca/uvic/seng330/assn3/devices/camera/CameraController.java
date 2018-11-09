package ca.uvic.seng330.assn3.devices.camera;

import ca.uvic.seng330.assn3.AuthManager;
import ca.uvic.seng330.assn3.Controller;
import ca.uvic.seng330.assn3.ViewTransition;

public class CameraController extends Controller {

  private final CameraModel model;

  public CameraController(
      CameraModel model, AuthManager authManager, ViewTransition transitionNotifier) {
    super(authManager, transitionNotifier);
    this.model = model;
  }
}
