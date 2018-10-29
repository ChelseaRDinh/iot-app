package ca.uvic.seng330.assn3;

import ca.uvic.seng330.assn3.login.LoginController;
import ca.uvic.seng330.assn3.login.LoginModel;
import ca.uvic.seng330.assn3.login.LoginView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

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
  public void shouldContainLoginButton() {}

  @Test
  public void shouldContainUsernameField() {}

  @Test
  public void shouldContainPasswordField() {}
}
