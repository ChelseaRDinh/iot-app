package ca.uvic.seng330.assn3.admin;
import ca.uvic.seng330.assn3.AuthManager;
import ca.uvic.seng330.assn3.Controller;
import ca.uvic.seng330.assn3.Token;
import ca.uvic.seng330.assn3.ViewTransition;
import ca.uvic.seng330.assn3.Views;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AdminController extends Controller {

  private final AdminModel model;
  private final AuthManager authManager;
  private final ViewTransition transitionNotifier;

  /**
   * Constructor for Admin controller for the admin UI.
   *
   * @param model the model for the admin UI
   * @param authManager the auth manager to manage authentication
   * @param transitionNotifier a function that will allow the controller to invoke a view transition
   */
  public AdminController(
      AdminModel model, AuthManager authManager, ViewTransition transitionNotifier) {
    super(authManager, transitionNotifier);
	this.model = model;
	this.authManager = authManager;
	this.transitionNotifier = transitionNotifier;
  }

  /** Open the user management UI. */
  public void manageUsersGUI() {
    Stage manageUsersStage = new Stage();
    ManageUsers manageUsersView = new ManageUsers();
    manageUsersStage.setScene(new Scene(manageUsersView.asParent(), 960, 480));
    manageUsersStage.show();
  }

  /** Open the device management UI. */
  public void manageDevicesGUI() {
	Stage manageDevicesStage = new Stage();
	AdminController controller = new AdminController(model, authManager, transitionNotifier);
	ManageDevices manageDevicesView = new ManageDevices(controller, model);
    manageDevicesStage.setScene(new Scene(manageDevicesView.asParent(), 960, 480));
    manageDevicesStage.show();
  }

  /** Return to the home UI. */
  public void homeGUI() {
    Token token = model.getToken();

    switchViews(this, Views.MAIN, token);
  }
}
