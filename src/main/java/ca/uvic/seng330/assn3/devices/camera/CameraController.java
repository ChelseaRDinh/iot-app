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

  public void updateCameraRecordingAt(int index, boolean isCelsius) {
    model.setCameraIsRecordingAt(index, isCelsius);
  }

  public void updateCameraConditionAt(int index, boolean value) {
    model.setCameraConditionAt(index, value);
  }

  /** Return to home dashboard. */
  public void home() {
    model.stopAllPlayers();

    Token token = model.getToken();
    switchViews(this, Views.MAIN, token);
  }
}
