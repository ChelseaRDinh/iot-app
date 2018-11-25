package ca.uvic.seng330.assn3;

import ca.uvic.seng330.assn3.devices.thermostat.ThermostatController;
import ca.uvic.seng330.assn3.devices.thermostat.ThermostatModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

public class TemperatureToggle extends OnOffToggle {
  /**
   * Constructor for the celsius/fahrenheit button for the thermostat UI.
   *
   * @param model the thermostat's model
   * @param controller the thermostat's controller
   * @param index the index of the thermostat in the model to bind this on/off button to
   */
  public TemperatureToggle(ThermostatModel model, ThermostatController controller, int index) {
    createUnitButtons();

    shouldBeOn = model.getThermostatIsCelsiusAt(index);

    // Fire the event of the selected button so it gets its color.
    if (shouldBeOn) {
      on.fire();
    } else {
      off.fire();
    }

    // Only watch for the on button. Since it's a toggle group, this will trigger no matter which
    // button is clicked.
    model
        .thermostatIsCelsiusPropertyAt(index)
        .addListener((obs, oldValue, newValue) -> updateIfNeeded(newValue, on));

    on.selectedProperty()
        .addListener(
            (obs, oldValue, newValue) -> controller.updateTemperatureUnit(index, newValue));
  }

  /** Creates a Celsius/Fahrenheit toggle button group. */
  private void createUnitButtons() {
    group = new ToggleGroup();

    off = new ToggleButton("Fahrenheit");
    off.setStyle("-fx-base: grey;");
    off.setToggleGroup(group);

    on = new ToggleButton("Celsius");
    on.setStyle("-fx-base: blue;");
    on.setToggleGroup(group);

    off.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent e) {
            if (!shouldBeOn) {
              off.setStyle("-fx-base: blue;");
              on.setStyle("-fx-base: grey;");
            } else {
              off.setStyle("-fx-base: grey;");
              on.setStyle("-fx-base: blue;");
              on.setSelected(true);
            }
            shouldBeOn = !shouldBeOn;
          }
        });

    on.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent e) {
            if (shouldBeOn) {
              off.setStyle("-fx-base: grey;");
              on.setStyle("-fx-base: blue;");
            } else {
              off.setStyle("-fx-base: blue;");
              on.setStyle("-fx-base: grey;");
            }
            shouldBeOn = !shouldBeOn;
          }
        });

    container = new HBox(on, off);
  }
}
