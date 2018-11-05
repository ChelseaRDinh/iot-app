package ca.uvic.seng330.assn3;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

import ca.uvic.seng330.assn3.admin.AdminView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

public class AdminViewTest extends ApplicationTest {

  @Override
  public void start(Stage primaryStage) {
    AdminView view = new AdminView();
    // Scene scene = new Scene(view.asParent(), 600, 600);
    // primaryStage.setScene(scene);
    // primaryStage.show();
  }
}
