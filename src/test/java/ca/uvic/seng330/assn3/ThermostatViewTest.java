package ca.uvic.seng330.assn3;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

import ca.uvic.seng330.assn3.devices.thermostat.ThermostatView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

public class ThermostatViewTest extends ApplicationTest {

  /*
  @Override
  public void start(Stage primaryStage) {
    ThermostatView view = new ThermostatView();
    Scene scene = new Scene(view.asParent(), 600, 600);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  @Test
  public void setTemperatureTest() {
    // when:
    clickOn("OFF");

    // then:
    verifyThat(".button", hasText("ON"));
  }
  */
}
