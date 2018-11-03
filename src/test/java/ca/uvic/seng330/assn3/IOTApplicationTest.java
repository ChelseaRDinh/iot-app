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
import static org.testfx.matcher.control.LabeledMatchers.hasText;
import org.testfx.matcher.control.TextInputControlMatchers;

public class IOTApplicationTest extends ApplicationTest {

  private Scene scene;

  @Override
  public void start(Stage primaryStage) {
    // Could start up like this and then take over its private variables.
    // IOTApplication app = new IOTApplication();
    // app.start(primaryStage);

    // Or just follow the example and start manually...
    LoginModel model = new LoginModel();
    LoginController controller = new LoginController(model);
    LoginView view = new LoginView(controller, model);

    scene = new Scene(view.asParent(), 400, 400);
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
    clickOn("#passwordField").write("password");

    // when:
    clickOn(".button");

    // expect:
    // Not implemented.
    assertEquals(false, true);
  }

  @Test
  public void testClickButton() {
    clickOn(".button");
    
    verifyThat(".button", hasText("processing..."));
  }
}
