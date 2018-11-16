package ca.uvic.seng330.assn3;

import static org.junit.Assert.assertNotEquals;

import ca.uvic.seng330.assn3.devices.Hub;
import ca.uvic.seng330.assn3.devices.MasterHub;
import ca.uvic.seng330.assn3.devices.SmartPlug;
import ca.uvic.seng330.assn3.devices.smartplug.SmartplugController;
import ca.uvic.seng330.assn3.devices.smartplug.SmartplugModel;
import ca.uvic.seng330.assn3.devices.smartplug.SmartplugView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

// Tests covered: 3
public class SmartplugViewTest extends ApplicationTest {
  private AuthManager authManager;
  private MasterHub allHubs;
  private Scene scene;
  private Stage primaryStage;
  private SmartPlug plug;

  @Override
  public void start(Stage primaryStage) {
    Hub allDevices = new Hub();
    plug = new SmartPlug(allDevices);

    try {
      authManager = new AuthManager();

      allDevices.register(plug);
    } catch (Exception e) {
      return;
    }

    allHubs = new MasterHub(authManager, authManager.getUsers(), allDevices);

    this.primaryStage = primaryStage;

    Token authToken = authManager.getToken("admin", "admin");

    SmartplugModel smartplugModel = new SmartplugModel(authToken, allHubs);
    SmartplugController smartplugController =
        new SmartplugController(
            smartplugModel,
            authManager,
            (from, to, token) -> {
              this.transition(from, to, token);
            });
    SmartplugView smartplugView = new SmartplugView(smartplugController, smartplugModel);
    scene = new Scene(smartplugView.asParent(), 960, 480);
    this.primaryStage.setScene(scene);
    this.primaryStage.show();
  }

  public void transition(Controller from, Views to, Token token) {}

  // GIVEN a non-functional smartplug WHEN I click "Toggle" on the client control THEN I should see
  // that the smartplug is "Turned On"
  // GIVEN a functional smartplug WHEN I click "Toggle" on the client control THEN I should see that
  // the smartplug is "Turned Off"
  // GIVEN a functional smartplug WHEN I look at the client control THEN I should see the plug's
  // current status (On/Off) of the smartplug
  @Test
  public void testToggle() {
    // Toggle on test.
    boolean before = plug.getCondition();
    clickOn("ON");
    assertNotEquals(before, plug.getCondition());

    // Toggle off test which also implies that the ON/OFF status is displayed.
    before = plug.getCondition();
    clickOn("OFF");
    assertNotEquals(before, plug.getCondition());
  }
}
