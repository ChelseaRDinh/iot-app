package ca.uvic.seng330.assn3;

import ca.uvic.seng330.assn3.admin.AdminController;
import ca.uvic.seng330.assn3.admin.AdminModel;
import ca.uvic.seng330.assn3.admin.AdminView;
import ca.uvic.seng330.assn3.devices.Hub;
import ca.uvic.seng330.assn3.devices.MasterHub;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

// Tests covered: 1
public class AdminDashboardTest extends ApplicationTest {
  /**
   * GIVEN I want to use my Home Automation System as an admin WHEN I open the admin interface THEN
   * I can manage devices and users.
   *
   * <p>GIVEN I want to use my Home Automation System as an admin WHEN I open the admin interface
   * THEN I can assign device notifications to users (ALL, NONE, some selection).
   *
   * <p>GIVEN I want to boot my Home Automation System WHEN I log in as an admin AND I open the
   * app's admin interface THEN I need to see a blank screen AND the list of Devices found in my
   * home.
   */
  private AuthManager authManager;

  private MasterHub allHubs;
  private Scene scene;
  private Stage primaryStage;

  @Override
  public void start(Stage primaryStage) {
    Hub allDevices = new Hub();

    try {
      authManager = new AuthManager();
    } catch (Exception e) {
      return;
    }

    allHubs = new MasterHub(authManager, authManager.getUsers(), allDevices);

    this.primaryStage = primaryStage;

    Token authToken = authManager.getToken("admin", "admin");

    AdminModel adminModel = new AdminModel(authToken, allHubs);
    AdminController adminController =
        new AdminController(
            adminModel,
            authManager,
            (from, to, token) -> {
              this.transition(from, to, token);
            });
    AdminView adminView = new AdminView(adminController, adminModel);
    scene = new Scene(adminView.asParent(), 960, 480);
    this.primaryStage.setScene(scene);
    this.primaryStage.show();
  }

  public void transition(Controller from, Views to, Token token) {}

  // GIVEN I want to use my Home Automation System as an admin WHEN I open the admin interface THEN
  // I can manage devices and users.
  @Test
  public void testManageUsers() {
    clickOn("Manage Users");
  }

  @Test
  public void testManageDevices() {
    clickOn("Manage Devices");
  }
}
