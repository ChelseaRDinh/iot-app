package ca.uvic.seng330.assn3;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

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
    //when:

    //then:
  }

  @Test
  public void testDevicesDB() {
    //when:

    //then:
  }
}
