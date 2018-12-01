package ca.uvic.seng330.assn3;

import ca.uvic.seng330.assn3.admin.*;
import ca.uvic.seng330.assn3.devices.Hub;
import ca.uvic.seng330.assn3.devices.Lightbulb;
import ca.uvic.seng330.assn3.devices.MasterHub;
import ca.uvic.seng330.assn3.devices.Status;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

public class UserAdminTest extends ApplicationTest {
  private AuthManager authManager;
  private MasterHub allHubs;
  private Scene scene;
  private Stage primaryStage;
  private List<Lightbulb> bulbs;
  private UserAdminModel deviceAdminModel;

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

    UserAdminModel userAdminModel = new UserAdminModel(authToken, allHubs);
    UserAdminController userAdminController =
        new UserAdminController(
            userAdminModel,
            authManager,
            (from, to, token) -> {
              this.transition(from, to, token);
            });
    UserAdminView userAdminView = new UserAdminView(userAdminController, userAdminModel);
    scene = new Scene(userAdminView.asParent(), 960, 480);
    this.primaryStage.setScene(scene);
    this.primaryStage.show();
  }

  public void transition(Controller from, Views to, Token token) {}

  @Test
  public void removeUser() {
    clickOn("Remove User");
  }

  @Test
  public void addUser() {
    clickOn("Add User");
  }
}
