package ca.uvic.seng330.assn3;

import ca.uvic.seng330.assn3.home.HomeController;
import ca.uvic.seng330.assn3.home.HomeModel;
import ca.uvic.seng330.assn3.home.HomeView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

public class HomeViewTest extends ApplicationTest {
  private Scene scene;
  private Stage primaryStage;

  private AuthManager authManager;
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

    // Default to admin token for tests.
    Token adminToken = authManager.getToken("admin", "admin");

    // Create here the scene as the IOTApplication would.
    HomeModel model = new HomeModel(adminToken);
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

  /**
   * GIVEN I am logged in as a user to the Hub WHEN I open the app interface THEN I see the user
   * interface.
   *
   * <p>GIVEN I want to use my Home Automation System as an admin WHEN I open the app interface THEN
   * I see the user interface and an admin interface.
   *
   * <p>GIVEN I want to use my Home Automation System as an admin WHEN I open the admin interface
   * THEN I can manage devices and users.
   *
   * <p>GIVEN I want to use my Home Automation System as an admin WHEN I open the admin interface
   * THEN I can assign device notifications to users (ALL, NONE, some selection).
   *
   * <p>GIVEN I want to boot my Home Automation System WHEN I log in as an admin AND I open the
   * app's admin interface THEN I need to see a blank screen AND the list of Devices found in my
   * home.
   */
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
  public void testSeeAdminInterface() {
    // given:
    Token adminToken = authManager.getToken("admin", "admin");

    // Create just the admin interface.
    HomeModel model = new HomeModel(adminToken);
    HomeController controller =
        new HomeController(
            model,
            authManager,
            (from, to, token) -> {
              this.transition(from, to, token);
            });
    HomeView view = new HomeView(controller, model);

    scene = new Scene(view.asParent(), 600, 600);
    primaryStage.setScene(scene);
    primaryStage.show();

    // then:
    // See admin interface.
    testAdminInterface();
  }

  @Test
  public void testSeeUserInterface() {
    // given:
    Token userToken = authManager.getToken("user", "user");

    // Create just the user interface.
    HomeModel model = new HomeModel(userToken);
    HomeController controller =
        new HomeController(
            model,
            authManager,
            (from, to, token) -> {
              this.transition(from, to, token);
            });
    HomeView view = new HomeView(controller, model);

    scene = new Scene(view.asParent(), 600, 600);
    primaryStage.setScene(scene);
    primaryStage.show();

    // then:
    // See user interface.
    testUserInterface();
  }

  @Test
  public void testDeviceSwitch() {
    // test each device icon here...
    clickOn("Camera");

    clickOn("Thermostat");

    clickOn("Lightbulb");

    clickOn("SmartPlug");
  }

  @Test
  public void testLogout() {

    // when:
    clickOn("Logout");
  }

  private void testUserInterface() {}

  private void testAdminInterface() {}
}
