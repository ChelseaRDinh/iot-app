package ca.uvic.seng330.assn3.devices.thermostat;

import ca.uvic.seng330.assn3.Model;
import ca.uvic.seng330.assn3.Token;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.ReadOnlyFloatProperty;
import ca.uvic.seng330.assn3.devices.MasterHub;
import org.json.JSONObject;

/*
* Setting up temperature field to be bound;
* so it can retrieved later for JSON messaging.
*
*/

public class ThermostatModel extends Model {
  private final FloatProperty temperatureValue = new SimpleFloatProperty();
  //private TempMode temperatureMode = TempMode.CELSIUS;
  private boolean isCelsius = true;

  public ThermostatModel(Token token, MasterHub h) {
    super(token, h);
  }

  public final FloatProperty temperatureValueProperty() {
    return this.temperatureValue;
  }

  public final double getTemperatureValue() {
    return this.temperatureValueProperty().get();
  }

  public final void setTemperatureValueProperty(final float x) {
    this.temperatureValueProperty().set(x);
  }

  public void setFahrenheit() {
    this.isCelsius = false;
  }

  public void setCelsius() {
    this.isCelsius = true;
  }

  public boolean isCelsius() {
    return this.isCelsius;
  }

  @Override
  public void notify(JSONObject message) {}
}
