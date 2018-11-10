package ca.uvic.seng330.assn3;

import ca.uvic.seng330.assn3.devices.camera.CameraController;
import ca.uvic.seng330.assn3.devices.camera.CameraModel;
import ca.uvic.seng330.assn3.devices.camera.CameraView;
import ca.uvic.seng330.assn3.devices.lightbulb.LightbulbModel;
import ca.uvic.seng330.assn3.devices.lightbulb.LightbulbView;
import ca.uvic.seng330.assn3.devices.lightbulb.LightbulbController;
import ca.uvic.seng330.assn3.devices.smartplug.SmartplugModel;
import ca.uvic.seng330.assn3.devices.smartplug.SmartplugView;
import ca.uvic.seng330.assn3.devices.smartplug.SmartplugController;
import ca.uvic.seng330.assn3.devices.thermostat.ThermostatModel;
import ca.uvic.seng330.assn3.devices.thermostat.ThermostatView;
import ca.uvic.seng330.assn3.devices.thermostat.ThermostatController;
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

    LoginModel startModel = new LoginModel();
    LoginController startController =
        new LoginController(
            startModel,
            authManager,
            (from, to, token) -> {
              this.transition(from, to, token);
            });
    LoginView startView = new LoginView(startController, startModel);

    scene = new Scene(startView.asParent(), 960, 480);
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
        HomeModel homeModel = new HomeModel(authToken);
        HomeController homeController =
            new HomeController(
                homeModel,
                authManager,
                (from, to, token) -> {
                  this.transition(from, to, token);
                });
        HomeView homeView = new HomeView(homeController, homeModel);
        scene = new Scene(homeView.asParent(), 960, 480);
        this.primaryStage.setScene(scene);
        this.primaryStage.show();
        break;
      case LOGIN:
        LoginModel loginModel = new LoginModel();
        LoginController loginController =
            new LoginController(
                loginModel,
                authManager,
                (from, to, token) -> {
                  this.transition(from, to, token);
                });
        LoginView loginView = new LoginView(loginController, loginModel);
        scene = new Scene(loginView.asParent(), 960, 480);
        this.primaryStage.setScene(scene);
        this.primaryStage.show();
        break;
      case CAMERA:
        CameraModel cameraModel = new CameraModel(authToken);
        CameraController cameraController =
            new CameraController(
                cameraModel,
                authManager,
                (from, to, token) -> {
                  this.transition(from, to, token);
                });
        CameraView cameraView = new CameraView(cameraController, cameraModel);
        scene = new Scene(cameraView.asParent(), 960, 480);
        this.primaryStage.setScene(scene);
        this.primaryStage.show();
        break;
      case LIGHTBULB:
        LightbulbModel lightbulbModel = new LightbulbModel(authToken);
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
        break;
      case SMARTPLUG:
        SmartplugModel smartplugModel = new SmartplugModel(authToken);
        SmartplugController smartplugController =
            new SmartplugController(
                smartplugModel,
                authManager,
                (from, to, token) -> {
                  this.transition(from, to, token);
                });
        SmartplugView smartplugView = new SmartplugView(smartplugController, smartplugModel);
        scene = new Scene(smartplugView.asParent(), 960, 480);
        this.primaryStage.setScene(scene);
        this.primaryStage.show();
        break;
      case THERMOSTAT:
        ThermostatModel thermostatModel = new ThermostatModel(authToken);
        ThermostatController thermostatController =
            new ThermostatController(
              thermostatModel,
                authManager,
                (from, to, token) -> {
                  this.transition(from, to, token);
                });
        ThermostatView thermostatView = new ThermostatView(thermostatController, thermostatModel);
        scene = new Scene(thermostatView.asParent(), 960, 480);
        this.primaryStage.setScene(scene);
        this.primaryStage.show();
        break;
      default:
        break;
    }
  }
}
