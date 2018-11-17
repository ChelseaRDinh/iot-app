package ca.uvic.seng330.assn3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.testfx.api.FxAssert.verifyThat;

import ca.uvic.seng330.assn3.devices.Hub;
import ca.uvic.seng330.assn3.devices.MasterHub;
import ca.uvic.seng330.assn3.devices.Thermostat;
import ca.uvic.seng330.assn3.devices.thermostat.ThermostatController;
import ca.uvic.seng330.assn3.devices.thermostat.ThermostatModel;
import ca.uvic.seng330.assn3.devices.thermostat.ThermostatView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.control.TextInputControlMatchers;

// Total acceptance tests: 5
public class ThermostatViewTest extends ApplicationTest {
  private AuthManager authManager;
  private MasterHub allHubs;
  private Scene scene;
  private Stage primaryStage;
  private Thermostat thermostat;

  @Override
  public void start(Stage primaryStage) {
    Hub allDevices = new Hub();
    thermostat = new Thermostat(allDevices);

    try {
      authManager = new AuthManager();

      allDevices.register(thermostat);
    } catch (Exception e) {
      return;
    }

    allHubs = new MasterHub(authManager, authManager.getUsers(), allDevices);

    this.primaryStage = primaryStage;

    Token authToken = authManager.getToken("admin", "admin");

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
  }

  public void transition(Controller from, Views to, Token token) {}

  // GIVEN a thermostat WHEN I click "Start" on the Client control THEN the Thermostat becomes
  // functional And I can see the data of current temperature and temperature metrics (Celsius /
  // Fahrenheit)
  // GIVEN a functional thermostat with metric set to Farenheit WHEN I set the temperature metric to
  // Celsius on the Client control THEN I should see the thermostat data changing to its equivalent
  // "Celsius"
  // GIVEN a functional thermostat with metric set to Celcius WHEN I set the temperature metric to
  // Fahrenheit on the Client control THEN I should see the thermostat data changing to its
  // equivalent "Fahrenheit"
  // GIVEN a functional thermostat WHEN I set the temperature to a particular value on the Client
  // control THEN I should see the thermostat temperature value is set to the GIVEN value
  // GIVEN a functional thermostat WHEN I click "Turn Off" on the client control THEN I should see
  // that the thermostat is turned off.
  @Test
  public void testToggle() {
    // Turn on first and verify that it exists, further tests check if all controls enabled.
    boolean before = thermostat.getCondition();
    clickOn("ON"); // "Start" is the same thing as "ON"
    assertNotEquals(before, thermostat.getCondition());

    // Convert to F, check that value changed from 0 to 32.
    float oldTemp = thermostat.getTemp();
    before = thermostat.getUnit();
    clickOn("Fahrenheit");
    assertNotEquals(before, thermostat.getUnit());
    assertNotEquals(oldTemp, thermostat.getTemp(), 0.01f);
    verifyThat("#temperatureField1", TextInputControlMatchers.hasText(new Float(32).toString()));

    // Try setting it to 50F, verify it changed.
    oldTemp = thermostat.getTemp();
    clickOn("#temperatureField1").eraseText(4);
    clickOn("#temperatureField1").write("50");
    clickOn("Set");
    assertNotEquals(oldTemp, thermostat.getTemp(), 0.01f);
    verifyThat("#temperatureField1", TextInputControlMatchers.hasText(new Float(50).toString()));

    // Reset field afterwards for celsius test.
    clickOn("#temperatureField1").eraseText(4);
    clickOn("#temperatureField1").write("32");
    clickOn("Set");

    // Try setting back to C, should be 0 degrees.
    oldTemp = thermostat.getTemp();
    before = thermostat.getUnit();
    clickOn("Celsius");
    assertNotEquals(before, thermostat.getUnit());
    assertNotEquals(oldTemp, thermostat.getTemp(), 0.01f);
    verifyThat("#temperatureField1", TextInputControlMatchers.hasText(new Float(0).toString()));

    // Turn thermostat off and check this.
    before = thermostat.getCondition();
    clickOn("OFF");
    assertNotEquals(before, thermostat.getCondition());

    // Check that controls are disabled.
    oldTemp = thermostat.getTemp();
    before = thermostat.getUnit();
    clickOn("Celsius");
    assertEquals(before, thermostat.getUnit());
    clickOn("#temperatureField1").write("50");
    clickOn("Set");
    assertEquals(oldTemp, thermostat.getTemp(), 0.01f);
    verifyThat("#temperatureField1", TextInputControlMatchers.hasText(new Float(0).toString()));
  }
}
