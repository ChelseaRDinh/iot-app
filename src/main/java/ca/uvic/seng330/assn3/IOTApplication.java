package ca.uvic.seng330.assn3;

import ca.uvic.seng330.assn3.admin.AdminController;
import ca.uvic.seng330.assn3.admin.AdminModel;
import ca.uvic.seng330.assn3.admin.AdminView;
import ca.uvic.seng330.assn3.admin.DeviceAdminController;
import ca.uvic.seng330.assn3.admin.DeviceAdminModel;
import ca.uvic.seng330.assn3.admin.DeviceAdminView;
import ca.uvic.seng330.assn3.admin.UserAdminController;
import ca.uvic.seng330.assn3.admin.UserAdminModel;
import ca.uvic.seng330.assn3.admin.UserAdminView;
import ca.uvic.seng330.assn3.devices.Camera;
import ca.uvic.seng330.assn3.devices.Hub;
import ca.uvic.seng330.assn3.devices.Lightbulb;
import ca.uvic.seng330.assn3.devices.MasterHub;
import ca.uvic.seng330.assn3.devices.SmartPlug;
import ca.uvic.seng330.assn3.devices.Thermostat;
import ca.uvic.seng330.assn3.devices.camera.CameraController;
import ca.uvic.seng330.assn3.devices.camera.CameraModel;
import ca.uvic.seng330.assn3.devices.camera.CameraView;
import ca.uvic.seng330.assn3.devices.lightbulb.LightbulbController;
import ca.uvic.seng330.assn3.devices.lightbulb.LightbulbModel;
import ca.uvic.seng330.assn3.devices.lightbulb.LightbulbView;
import ca.uvic.seng330.assn3.devices.smartplug.SmartplugController;
import ca.uvic.seng330.assn3.devices.smartplug.SmartplugModel;
import ca.uvic.seng330.assn3.devices.smartplug.SmartplugView;
import ca.uvic.seng330.assn3.devices.thermostat.ThermostatController;
import ca.uvic.seng330.assn3.devices.thermostat.ThermostatModel;
import ca.uvic.seng330.assn3.devices.thermostat.ThermostatView;
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
  private MasterHub allHubs;
  private Scene scene;
  private Stage primaryStage;

  @Override
  public void start(Stage primaryStage) {
    Hub allDevices = new Hub();
    Lightbulb l1 = new Lightbulb(allDevices);
    Lightbulb l2 = new Lightbulb(allDevices);
    Lightbulb l3 = new Lightbulb(allDevices);
    Lightbulb l4 = new Lightbulb(allDevices);
    Lightbulb l5 = new Lightbulb(allDevices);
    Lightbulb l6 = new Lightbulb(allDevices);

    SmartPlug p1 = new SmartPlug(allDevices);
    SmartPlug p2 = new SmartPlug(allDevices);
    SmartPlug p3 = new SmartPlug(allDevices);

    Thermostat t1 = new Thermostat(allDevices);
    Thermostat t2 = new Thermostat(allDevices);

    Camera c1 = new Camera(allDevices);
    Camera c2 = new Camera(allDevices);

    try {
      authManager = new AuthManager();

      allDevices.register(l1);
      allDevices.register(l2);
      allDevices.register(l3);
      allDevices.register(l4);
      allDevices.register(l5);
      allDevices.register(l6);

      allDevices.register(p1);
      allDevices.register(p2);
      allDevices.register(p3);

      allDevices.register(t1);
      allDevices.register(t2);

      allDevices.register(c1);
      allDevices.register(c2);
    } catch (Exception e) {
      return;
    }

    allHubs = new MasterHub(authManager, authManager.getUsers(), allDevices);

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
        HomeModel homeModel = new HomeModel(authToken, allHubs);
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
        CameraModel cameraModel = new CameraModel(authToken, allHubs);
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
        LightbulbModel lightbulbModel = new LightbulbModel(authToken, allHubs);
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
        SmartplugModel smartplugModel = new SmartplugModel(authToken, allHubs);
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
        ThermostatModel thermostatModel = new ThermostatModel(authToken, allHubs);
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
      case ADMIN:
        AdminModel adminModel = new AdminModel(authToken, allHubs);
        AdminController adminController =
            new AdminController(
                adminModel,
                authManager,
                (from, to, token) -> {
                  this.transition(from, to, token);
                });
        AdminView adminView = new AdminView(adminController, adminModel);
        scene = new Scene(adminView.asParent(), 960, 480);
        this.primaryStage.setScene(scene);
        this.primaryStage.show();
		break;
	case USER_ADMIN:
		UserAdminModel userAdminModel = new UserAdminModel(authToken, allHubs);
		UserAdminController userAdminController = 
			new UserAdminController(
				userAdminModel,
				authManager,
				(from, to, token) -> {
					this.transition(from, to, token);
				});
		UserAdminView userAdminView =
				new UserAdminView(userAdminController, userAdminModel);
		scene = new Scene(userAdminView.asParent(), 960, 480);
		this.primaryStage.setScene(scene);
		this.primaryStage.show();
		break;
      case DEVICE_ADMIN:
        DeviceAdminModel deviceAdminModel = new DeviceAdminModel(authToken, allHubs);
        DeviceAdminController deviceAdminController =
            new DeviceAdminController(
                deviceAdminModel,
                authManager,
                (from, to, token) -> {
                  this.transition(from, to, token);
                });
        DeviceAdminView deviceAdminView =
            new DeviceAdminView(deviceAdminController, deviceAdminModel);
        scene = new Scene(deviceAdminView.asParent(), 960, 480);
        this.primaryStage.setScene(scene);
        this.primaryStage.show();
        break;
      default:
        break;
    }
  }
}
