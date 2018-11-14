package ca.uvic.seng330.assn3.devices.thermostat;

import ca.uvic.seng330.assn3.Model;
import ca.uvic.seng330.assn3.Token;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import ca.uvic.seng330.assn3.devices.MasterHub;
import org.json.JSONObject;

/*
* Setting up temperature field to be bound;
* so it can retrieved later for JSON messaging.
*
*/

public class ThermostatModel extends Model {
  private final DoubleProperty temperatureValue = new SimpleDoubleProperty();
  private TempMode temperatureMode = TempMode.CELSIUS;

  public ThermostatModel(Token token, MasterHub h) {
    super(token, h);
  }

  public final DoubleProperty temperatureValueProperty() {
    return this.temperatureValue;
  }

  public final double getTemperatureValue() {
    return this.temperatureValueProperty().get();
  }

  public final void setTemperatureValueProperty(final double x) {
    this.temperatureValueProperty().set(x);
  }

  public void setMode(TempMode mode) {
    this.temperatureMode = mode;
  }

  public TempMode getMode() {
    return this.temperatureMode;
  }

  @Override
  public void notify(JSONObject message) {}
}
