package ca.uvic.seng330.assn3;

import ca.uvic.seng330.assn3.admin.AdminDashboard;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

public class AdminDashboardTest extends ApplicationTest {

  @Override
  public void start(Stage primaryStage) {
    AdminDashboard view = new AdminDashboard();
    Scene scene = new Scene(view.asParent(), 600, 600);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  @Test
  public void testUsersDB() {
    // when:
    clickOn("Manage Users");
    // then:
  }

  @Test
  public void testDevicesDB() {
    // when:
    clickOn("Manage Devices");
    // then:
  }
}