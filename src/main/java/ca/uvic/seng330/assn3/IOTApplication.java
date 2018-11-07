package ca.uvic.seng330.assn3;

import ca.uvic.seng330.assn3.home.HomeController;
import ca.uvic.seng330.assn3.home.HomeModel;
import ca.uvic.seng330.assn3.home.HomeView;
import ca.uvic.seng330.assn3.login.LoginController;
import ca.uvic.seng330.assn3.login.LoginModel;
import ca.uvic.seng330.assn3.login.LoginView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class IOTApplication extends Application {
  private AuthManager authManager;
  private Scene scene;
  private Stage primaryStage;

  @Override
  public void start(Stage primaryStage) {
    try {
      authManager = new AuthManager();
    } catch (Exception e) {
      return;
    }

    this.primaryStage = primaryStage;

    LoginModel model = new LoginModel();
    LoginController controller =
        new LoginController(
            model,
            authManager,
            (from, to, token) -> {
              this.transition(from, to, token);
            });
    LoginView view = new LoginView(controller, model);

    scene = new Scene(view.asParent(), 960, 480);
    this.primaryStage.setScene(scene);
    this.primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }

  private void transition(Controller sourceController, Views desiredView, Token authToken) {
    if (!authManager.isValidToken(authToken)) {
      return;
    }

    switch (desiredView) {
      case MAIN:
        HomeModel model = new HomeModel(authToken);
        HomeController controller =
            new HomeController(
                model,
                authManager,
                (from, to, token) -> {
                  this.transition(from, to, token);
                });
        HomeView view = new HomeView(controller, model);
        scene = new Scene(view.asParent(), 960, 480);
        this.primaryStage.setScene(scene);
        this.primaryStage.show();
        break;
      default:
        break;
    }
  }
}
