package ca.uvic.seng330.assn3;

import static org.junit.Assert.assertEquals;
import static org.testfx.api.FxAssert.verifyThat;

import ca.uvic.seng330.assn3.login.LoginController;
import ca.uvic.seng330.assn3.login.LoginModel;
import ca.uvic.seng330.assn3.login.LoginView;
import ca.uvic.seng330.assn3.home.HomeController;
import ca.uvic.seng330.assn3.home.HomeModel;
import ca.uvic.seng330.assn3.home.HomeView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;
import static org.testfx.matcher.control.LabeledMatchers.hasText;
import org.testfx.matcher.control.TextInputControlMatchers;

public class IOTApplicationTest extends ApplicationTest {

  private Scene scene;
  private AuthManager authManager;

  private Controller currentController;
  private Controller from;
  private Views to;
  private String token;

  public void transition(Controller from, Views to, String token) {
    this.from = from;
    this.to = to;
    this.token = token;
  }

  @Override
  public void start(Stage primaryStage) {
    // Could start up like this and then take over its private variables.
    // IOTApplication app = new IOTApplication();
    // app.start(primaryStage);

    // Or just follow the example and start manually...
    
    /*
    try {
      authManager = new AuthManager();
    } catch (Exception e) {
      authManager = null;
      return;
    }

    LoginModel model = new LoginModel();
    currentController =
        new LoginController(
            model,
            authManager,
            (from, to, token) -> {
              this.transition(from, to, token);
            });
    LoginView view = new LoginView((LoginController) currentController, model);
    scene = new Scene(view.asParent(), 600, 600);
    primaryStage.setScene(scene);
    primaryStage.show();
    */

    HomeModel model = new HomeModel();
    HomeController controller = new HomeController(model);
    HomeView view = new HomeView(controller, model);

    scene = new Scene(view.asParent(), 600, 600);
    primaryStage.setScene(scene);
    primaryStage.show();
  }
  
  @Test
  public void shouldContainLoginButton() {
    // expect:
    verifyThat(".button", LabeledMatchers.hasText("Login"));
  }
  
  @Test
  public void shouldContainUsernameField() {
    // given:
    clickOn("#usernameField").write("username");
    // expect:
    verifyThat("#usernameField", TextInputControlMatchers.hasText("username"));
  }

  @Test
  public void shouldContainPasswordField() {
    // given:
    clickOn("#passwordField").write("password");
    // expect:
    verifyThat("#passwordField", TextInputControlMatchers.hasText("password"));
  }

  @Test
  public void adminShouldLogin() {
    // given:
    // Might need to ensure pw file is reset here, then restored.
    clickOn("#usernameField").write("admin");
    clickOn("#passwordField").write("admin");

    // when:
    clickOn(".button");

    // expect:
    // The from controller should be the login controller and it should be asking
    // to go to the main view.
    assertEquals(from, currentController);
    assertEquals(to, Views.MAIN);
  }

  @Test
  public void testLoginButton() {
    clickOn(".button");
    
    verifyThat(".button", hasText("processing..."));
  }

  @Test
  public void testHome() {
  }
}
