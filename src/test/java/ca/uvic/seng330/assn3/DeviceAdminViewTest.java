package ca.uvic.seng330.assn3;

import static org.junit.Assert.assertNotEquals;

import ca.uvic.seng330.assn3.admin.DeviceAdminController;
import ca.uvic.seng330.assn3.admin.DeviceAdminModel;
import ca.uvic.seng330.assn3.admin.DeviceAdminView;
import ca.uvic.seng330.assn3.devices.Hub;
import ca.uvic.seng330.assn3.devices.Lightbulb;
import ca.uvic.seng330.assn3.devices.MasterHub;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

// Tests covered: 1
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

    for (int i = 0; i < 15; i++) {
      bulbs.add(new Lightbulb(allDevices));
    }

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

  // Given I am an administrator WHEN I want to see a device status check THEN the status check
  // thread starts and collects the data without interrupting the UI.
  @Test
  public void testThread() {
    boolean before = deviceAdminModel.isReadyProperty().get();
    clickOn("Loading device list...");

    sleep(2000);

    clickOn("Device");
    assertNotEquals(before, deviceAdminModel.isReadyProperty().get());
  }
}
