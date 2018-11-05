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

  @Override
  public void start(Stage primaryStage) {
    HomeModel model = new HomeModel();
    HomeController controller = new HomeController(model);
    HomeView view = new HomeView(controller, model);
    scene = new Scene(view.asParent(), 600, 600);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  @Test
  public void testDeviceSwitch() {
    // test each device icon here...
  }
}
