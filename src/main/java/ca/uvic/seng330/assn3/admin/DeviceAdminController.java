package ca.uvic.seng330.assn3.admin;

import ca.uvic.seng330.assn3.AuthManager;
import ca.uvic.seng330.assn3.Controller;
import ca.uvic.seng330.assn3.DeviceItem;
import ca.uvic.seng330.assn3.Token;
import ca.uvic.seng330.assn3.ViewTransition;
import ca.uvic.seng330.assn3.Views;
import java.util.UUID;
import ca.uvic.seng330.assn3.devices.*;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DeviceAdminController extends Controller {
  private final DeviceAdminModel model;
  private AuthManager authManager;
  private ViewTransition transitionNotifier;
  private Token t;
  private Stage primaryStage;
  private Scene scene;

  /**
   * Constructor for Admin controller for the device admin UI.
   *
   * @param model the model for the device admin UI
   * @param authManager the auth manager to manage authentication
   * @param transitionNotifier a function that will allow the controller to invoke a view transition
   */
  public DeviceAdminController(
      DeviceAdminModel model, AuthManager authManager, ViewTransition transitionNotifier) {
    super(authManager, transitionNotifier);
	this.model = model;
	this.authManager = authManager;
	this.transitionNotifier = transitionNotifier;
  }

  public DeviceItem addNewDevice(String className) {
    return model.addNewDevice(className);
  }

  public boolean removeByUUID(UUID device) {
    return model.removeByUUID(device);
  }

  /** Return to the admin UI. */
  public void adminGUI() {
    model.stopGettingDevices();
    Token token = model.getToken();

    switchViews(this, Views.ADMIN, token);
  }

  /** Open this view in a seperate window */
  public void userDeviceRegGUI() {
	  DeviceRegistrationModel deviceRegistrationModel = new DeviceRegistrationModel(t, model.getMasterHub());
	  DeviceRegistrationController deviceRegistrationController =
		  new DeviceRegistrationController(
			  deviceRegistrationModel,
			  authManager,
			  (from, to, t) -> {
				transitionNotifier.transition(from, to, t);
			  });
	  DeviceRegistrationView deviceRegistrationView =
		  new DeviceRegistrationView(deviceRegistrationController, deviceRegistrationModel);
	  scene = new Scene(deviceRegistrationView.asParent(), 400, 400);
	  primaryStage = new Stage();
	  primaryStage.setScene(scene);
	  primaryStage.show();
  }

  public void confirmOwnerChange() {
	primaryStage.close();
  }

  public void changeDeviceOwner(String oldOwner, String newOwner, String deviceUUID) {
	Hub oldUserHub = model.getHubForUser(oldOwner);
	Hub newUserHub = model.getHubForUser(newOwner);
	Device d = oldUserHub.getDeviceByUUID(UUID.fromString(deviceUUID));
	/**Remove device from old owner, then register device with new selected owner */
	try {
		model.removeDeviceByUUID(UUID.fromString(deviceUUID));
		newUserHub.register(d);
	} catch(HubRegistrationException e) {
		//do nothing
	}
  }
}
