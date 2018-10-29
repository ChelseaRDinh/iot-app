package ca.uvic.seng330.assn3;

import ca.uvic.seng330.assn3.login.LoginController;
import ca.uvic.seng330.assn3.login.LoginModel;
import ca.uvic.seng330.assn3.login.LoginView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class IOTApplication extends Application {

  @Override
  public void start(Stage primaryStage) {
    LoginModel model = new LoginModel();
    LoginController controller = new LoginController(model);
    LoginView view = new LoginView(controller, model);

    Scene scene = new Scene(view.asParent(), 960, 480);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
