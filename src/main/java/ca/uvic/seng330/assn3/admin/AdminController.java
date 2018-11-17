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

  public AdminController(
      AdminModel model, AuthManager authManager, ViewTransition transitionNotifier) {
    super(authManager, transitionNotifier);
    this.model = model;
  }

  public void manageUsersGUI() {
    Stage manageUsersStage = new Stage();
    ManageUsers manageUsersView = new ManageUsers();
    manageUsersStage.setScene(new Scene(manageUsersView.asParent(), 960, 480));
    manageUsersStage.show();
  }

  public void manageDevicesGUI() {
    Stage manageDevicesStage = new Stage();
    ManageDevices manageDevicesView = new ManageDevices();
    manageDevicesStage.setScene(new Scene(manageDevicesView.asParent(), 960, 480));
    manageDevicesStage.show();
  }

  public void homeGUI() {
    Token token = model.getToken();

    switchViews(this, Views.MAIN, token);
  }
}
