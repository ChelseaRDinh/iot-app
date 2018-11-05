package ca.uvic.seng330.assn3;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

import ca.uvic.seng330.assn3.admin.ManageDevices;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

public class ManageDevicesTest extends ApplicationTest {

  @Override
  public void start(Stage primaryStage) {
    ManageDevices view = new ManageDevices();
    Scene scene = new Scene(view.asParent(), 600, 600);
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
