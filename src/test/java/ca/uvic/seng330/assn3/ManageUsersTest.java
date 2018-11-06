package ca.uvic.seng330.assn3;

import ca.uvic.seng330.assn3.admin.ManageUsers;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

public class ManageUsersTest extends ApplicationTest {

  @Override
  public void start(Stage primaryStage) {
    ManageUsers view = new ManageUsers();
    Scene scene = new Scene(view.asParent(), 600, 600);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  @Test
  public void testUserInfo() {
    // when:
    clickOn(".button");
    // then:
  }
}
