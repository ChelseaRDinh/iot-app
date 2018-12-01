package ca.uvic.seng330.assn3;

import static org.junit.Assert.assertNotEquals;

import ca.uvic.seng330.assn3.admin.DeviceAdminController;
import ca.uvic.seng330.assn3.admin.DeviceAdminModel;
import ca.uvic.seng330.assn3.admin.DeviceAdminView;
import ca.uvic.seng330.assn3.devices.Hub;
import ca.uvic.seng330.assn3.devices.Lightbulb;
import ca.uvic.seng330.assn3.devices.MasterHub;
import ca.uvic.seng330.assn3.devices.SmartPlug;
import ca.uvic.seng330.assn3.devices.Status;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

// Tests covered: 6
public class DeviceAdminViewTest extends ApplicationTest {
  private AuthManager authManager;
  private MasterHub allHubs;
  private Scene scene;
  private Stage primaryStage;
  private List<Lightbulb> bulbs;
  private DeviceAdminModel deviceAdminModel;

  @Override
  public void start(Stage primaryStage) {
    bulbs = new ArrayList<Lightbulb>();
    Hub allDevices = new Hub();

    for (int i = 0; i < 10; i++) {
      bulbs.add(new Lightbulb(allDevices));
    }

    bulbs.get(0).setStatus(Status.ERROR);
    bulbs.get(1).setStatus(Status.OFFLINE);

    try {
      authManager = new AuthManager();

      for (Lightbulb bulb : bulbs) {
        allDevices.register(bulb);
      }
    } catch (Exception e) {
      return;
    }

    allHubs = new MasterHub(authManager, authManager.getUsers(), allDevices);

    this.primaryStage = primaryStage;

    Token authToken = authManager.getToken("admin", "admin");

    deviceAdminModel = new DeviceAdminModel(authToken, allHubs);
    DeviceAdminController deviceAdminController =
        new DeviceAdminController(
            deviceAdminModel,
            authManager,
            (from, to, token) -> {
              this.transition(from, to, token);
            });
    DeviceAdminView deviceAdminView = new DeviceAdminView(deviceAdminController, deviceAdminModel);
    scene = new Scene(deviceAdminView.asParent(), 960, 480);
    this.primaryStage.setScene(scene);
    this.primaryStage.show();
  }

  public void transition(Controller from, Views to, Token token) {}

  // GIVEN I am an administrator WHEN I want to see a device status check THEN the status check
  // thread starts and collects the data without interrupting the UI.
  // Here you need to click the device admin to see the list of devices found, same thing just a
  // different menu that makes things more streamlined.
  // GIVEN I want to boot my Home Automation System WHEN I log in as an admin AND I open the app's
  // admin interface THEN I need to see a blank screen OR the list of Devices found in my home
  // This is the same screen as the status screen, they are integrated.
  // GIVEN the Home Automation System is functional WHEN I want to administer my devices THEN the
  // Hub should show the device config screen
  // GIVEN the Home Automation System is functional WHEN the devices of the system are functional
  // THEN the Hub should display device status visually: functioning, offline, error
  @Test
  public void testThreadAndStatus() {
    boolean before = deviceAdminModel.isReadyProperty().get();
    clickOn("Loading device list...");

    sleep(1500);

    assertNotEquals(before, deviceAdminModel.isReadyProperty().get());

    // Shows the device config screen, status screen, device state
    clickOn("ERROR");
    clickOn("OFFLINE");
    clickOn("NORMAL");
  }

  // GIVEN the Home Automation System is functional WHEN I want to administer my devices THEN the
  // Hub should show the device config screen
  // GIVEN the Home Automation System is functional WHEN I add a new device to my system on the
  // Client control THEN the Hub should register this new device to the system.
  // GIVEN the Home Automation System is functional WHEN I remove a device from my system on the
  // Client Control THEN the Hub should deregister this new device from the system.
  @Test
  public void testRegisterAndUnregister() {
    // Wait for the model to load.
    sleep(1500);

    // Remove lightbulbs, also proves that it's a device config screen.
    for (int i = 0; i < 5; i++) {
      clickOn(Lightbulb.class.getName());
      clickOn("Remove Selected Device");
    }

    // Select "Smartplug" from the device box.
    clickOn("#deviceTypeBox");
    type(KeyCode.DOWN);
    type(KeyCode.DOWN);
    type(KeyCode.DOWN);
    type(KeyCode.ENTER);

    clickOn("Confirm Add Device");

    clickOn(SmartPlug.class.getName());
    clickOn("Remove Selected Device");
  }
}
