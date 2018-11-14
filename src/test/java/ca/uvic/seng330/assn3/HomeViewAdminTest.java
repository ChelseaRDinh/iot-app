package ca.uvic.seng330.assn3;

import ca.uvic.seng330.assn3.devices.Hub;
import ca.uvic.seng330.assn3.devices.MasterHub;
import ca.uvic.seng330.assn3.home.HomeController;
import ca.uvic.seng330.assn3.home.HomeModel;
import ca.uvic.seng330.assn3.home.HomeView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

// Tests covered: 1
public class HomeViewAdminTest extends ApplicationTest {
  private Scene scene;
  private Stage primaryStage;

  private AuthManager authManager;
  private MasterHub hub;
  private Views to;
  private Token token;

  @Override
  public void start(Stage primaryStage) {
    this.primaryStage = primaryStage;

    try {
      authManager = new AuthManager();
    } catch (Exception e) {
      authManager = null;
      return;
    }

    Hub deviceHub = new Hub();

    hub = new MasterHub(authManager, authManager.getUsers(), deviceHub);

    // Default to admin token for tests.
    Token adminToken = authManager.getToken("admin", "admin");

    // Create here the scene as the IOTApplication would.
    HomeModel model = new HomeModel(adminToken, hub);
    HomeController controller =
        new HomeController(
            model,
            authManager,
            (from, to, token) -> {
              this.transition(from, to, token);
            });
    HomeView view = new HomeView(controller, model);

    scene = new Scene(view.asParent(), 600, 600);
    this.primaryStage.setScene(scene);
    this.primaryStage.show();
  }

  public void transition(Controller from, Views to, Token token) {
    this.to = to;
    this.token = token;
  }

  // GIVEN I want to use my Home Automation System as an admin WHEN I open the app interface THEN
  // I see the user interface and an admin interface.
  @Test
  public void testBothInterfaces() {
    // given: I log in as admin (test by default does this)
    // then:
    // See user interface.
    testUserInterface();

    // See admin interface.
    testAdminInterface();
  }

  @Test
  public void testAdminLogout() {
    // given: logged in as admin
    // when:
    clickOn("Logout");
  }

  private void testUserInterface() {
    // then: I should see user interface
    clickOn("Camera");
    clickOn("Thermostat");
    clickOn("Lightbulb");
    clickOn("SmartPlug");
    clickOn("Logout");
  }

  private void testAdminInterface() {
    // then: I should see admin interface
    clickOn("Admin");
  }
}
