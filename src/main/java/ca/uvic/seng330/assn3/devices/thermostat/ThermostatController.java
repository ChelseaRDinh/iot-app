package ca.uvic.seng330.assn3.devices.thermostat;

import ca.uvic.seng330.assn3.AuthManager;
import ca.uvic.seng330.assn3.Controller;
import ca.uvic.seng330.assn3.Token;
import ca.uvic.seng330.assn3.ViewTransition;
import ca.uvic.seng330.assn3.Views;

public class ThermostatController extends Controller {

  private final ThermostatModel model;

  public ThermostatController(
      ThermostatModel model, AuthManager authManager, ViewTransition transitionNotifier) {
    super(authManager, transitionNotifier);
    this.model = model;
  }

  public void home() {
    Token token = model.getToken();
    switchViews(this, Views.MAIN, token);
  }

  public void updateTemperatureValue(int index, float value) {
    model.setThermostatValueAt(index, value);
  }

  public void updateTemperatureUnit(int index, boolean isCelsius) {
    model.setThermostatIsCelsiusAt(index, isCelsius);
  }

  public void updateThermostatConditionAt(int index, boolean value) {
    // setIndexDisabled(index, value);
    model.setThermostatConditionAt(index, value);
  }

  /*
   * Convert string value in text field to double.
   * This may already be covered in original text field declaration
   * but making this anyway for good measure.
   */
  private float convertStringToFloat(String s) {
    if (s == null || s.isEmpty()) {
      return 0;
    }
    if ("-".equals(s)) {
      return 0;
    }
    return Float.parseFloat(s);
  }
}
