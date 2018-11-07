package ca.uvic.seng330.assn3;

import static org.junit.Assert.assertEquals;
import static org.testfx.api.FxAssert.verifyThat;

import ca.uvic.seng330.assn3.login.LoginController;
import ca.uvic.seng330.assn3.login.LoginModel;
import ca.uvic.seng330.assn3.login.LoginView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.TextInputControlMatchers;

public class IOTApplicationTest extends ApplicationTest {

  private Scene scene;
  private AuthManager authManager;

  private Controller currentController;
  private Controller from;
  private Views to;
  private Token token;

  public void transition(Controller from, Views to, Token token) {
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
    clickOn("Login");

    // expect:
    // The from controller should be the login controller and it should be asking
    // to go to the main view.
    assertEquals(from, currentController);
    assertEquals(to, Views.MAIN);
    assertEquals(authManager.isAdminToken(token), true);
  }

  @Test
  public void testLoginButtonFail() {
    // when:
    clickOn("#usernameField").write("user");
    clickOn("#passwordField").write("wrong");
    clickOn("Login");

    // then:
    // check if the login fail label is visible
    assertEquals(false, true);
  }

  public void testHomeView() {
    // when:
    // entering admin as username and password
    clickOn("#usernameField").write("admin");
    clickOn("#passwordField").write("admin");

    // then:
    // The view should redirect to the home screen.
    assertEquals(false, true);
  }
}
