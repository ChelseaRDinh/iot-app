package ca.uvic.seng330.assn3;

import static org.junit.Assert.assertNotEquals;

import ca.uvic.seng330.assn3.devices.Hub;
import ca.uvic.seng330.assn3.devices.Lightbulb;
import ca.uvic.seng330.assn3.devices.MasterHub;
import ca.uvic.seng330.assn3.devices.lightbulb.LightbulbController;
import ca.uvic.seng330.assn3.devices.lightbulb.LightbulbModel;
import ca.uvic.seng330.assn3.devices.lightbulb.LightbulbView;
import java.util.ArrayList;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

// Tests covered: 2
public class LightbulbViewTest extends ApplicationTest {
  private AuthManager authManager;
  private MasterHub allHubs;
  private Scene scene;
  private Stage primaryStage;
  private Lightbulb bulb;

  @Override
  public void start(Stage primaryStage) {
    Hub allDevices = new Hub();
    bulb = new Lightbulb(allDevices);

    try {
      authManager = new AuthManager();

      allDevices.register(bulb);
    } catch (Exception e) {
      return;
    }

    allHubs = new MasterHub(authManager, authManager.getUsers(), allDevices);

    this.primaryStage = primaryStage;

    Token authToken = authManager.getToken("admin", "admin");

    LightbulbModel lightbulbModel = new LightbulbModel(authToken, allHubs);
    LightbulbController lightbulbController =
        new LightbulbController(
            lightbulbModel,
            authManager,
            (from, to, token) -> {
              this.transition(from, to, token);
            });
    LightbulbView lightbulbView = new LightbulbView(lightbulbController, lightbulbModel);
    scene = new Scene(lightbulbView.asParent(), 960, 480);
    this.primaryStage.setScene(scene);
    this.primaryStage.show();
  }

  public void transition(Controller from, Views to, Token token) {}

  // GIVEN a light bulb that is off WHEN I click "Toggle" on the client control THEN I should see
  // the data that the Light Bulb is turned "On".
  // GIVEN a light bulb that is On WHEN I click "Toggle" on the client control THEN I should see
  // that the light bulb is turned "Off".
  @Test
  public void testToggle() {
      boolean before = bulb.getCondition();
      clickOn("ON");
      assertNotEquals(before, bulb.getCondition());

      before = bulb.getCondition();
      clickOn("OFF");
      assertNotEquals(before, bulb.getCondition());
  }
}
