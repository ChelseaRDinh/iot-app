package ca.uvic.seng330.assn3;

import static org.junit.Assert.assertEquals;

import ca.uvic.seng330.assn3.admin.AdminController;
import ca.uvic.seng330.assn3.admin.AdminModel;
import ca.uvic.seng330.assn3.admin.AdminView;
import ca.uvic.seng330.assn3.devices.Camera;
import ca.uvic.seng330.assn3.devices.Device;
import ca.uvic.seng330.assn3.devices.Hub;
import ca.uvic.seng330.assn3.devices.Lightbulb;
import ca.uvic.seng330.assn3.devices.MasterHub;
import ca.uvic.seng330.assn3.devices.SmartPlug;
import ca.uvic.seng330.assn3.devices.Status;
import ca.uvic.seng330.assn3.devices.Thermostat;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

// Tests covered: 2
public class AdminDashboardTest extends ApplicationTest {
  private AuthManager authManager;

  private MasterHub allHubs;
  private Scene scene;
  private Stage primaryStage;

  @Override
  public void start(Stage primaryStage) {
    Hub allDevices = new Hub();
    Lightbulb l1 = new Lightbulb(allDevices);
    SmartPlug s1 = new SmartPlug(allDevices);
    Camera c1 = new Camera(allDevices);
    Thermostat t1 = new Thermostat(allDevices);

    try {
      authManager = new AuthManager();

      allDevices.register(l1);
      allDevices.register(s1);
      allDevices.register(c1);
      allDevices.register(t1);
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

  // GIVEN I want to use my Home Automation System as an admin WHEN I open the admin interface THEN
  // I can manage devices and users.
  @Test
  public void testManageDevices() {
    clickOn("Manage Devices");
  }

  // GIVEN the Home Automation System is functional WHEN I click shutdown from the Client Control
  // THEN the Hub should turn off all the devices AND should safely shutdown the system AND notify
  // about the activity on the Client.
  @Test
  public void testShutdownDevices() {
    Token authToken = authManager.getToken("admin", "admin");
    Hub h = allHubs.getHubForUser(authToken);

    clickOn("Shutdown Devices");
    assertEquals(h.getStatus() == Status.OFFLINE, true);
    for (Device d : h.getDevices().values()) {
      assertEquals(d.getStatus() == Status.OFFLINE, true);
    }

    // If the button changes to the startup button, then the hub notified about the activity.
    clickOn("Startup Devices");
    assertEquals(h.getStatus() == Status.NORMAL, true);
    for (Device d : h.getDevices().values()) {
      assertEquals(d.getStatus() == Status.NORMAL, true);
    }
  }
}
