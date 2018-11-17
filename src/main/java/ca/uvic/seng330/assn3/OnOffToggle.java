package ca.uvic.seng330.assn3;

import ca.uvic.seng330.assn3.devices.lightbulb.LightbulbController;
import ca.uvic.seng330.assn3.devices.lightbulb.LightbulbModel;
import ca.uvic.seng330.assn3.devices.smartplug.SmartplugController;
import ca.uvic.seng330.assn3.devices.smartplug.SmartplugModel;
import ca.uvic.seng330.assn3.devices.thermostat.ThermostatController;
import ca.uvic.seng330.assn3.devices.thermostat.ThermostatModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

public class OnOffToggle {
  private HBox container;
  private ToggleGroup group;
  private ToggleButton on;
  private ToggleButton off;

  private boolean shouldBeOn = false;

  /** Default constructor for the on off toggle button. */
  public OnOffToggle() {
    createButtons();
  }

  public OnOffToggle(LightbulbModel model, LightbulbController controller, int index) {
    createButtons();
    on.setId("lightbulbOn" + new Integer(index).toString());
    off.setId("lightbulbOff" + new Integer(index).toString());

    shouldBeOn = model.getLightbulbConditionAt(index);

    // Fire the event of the selected button so it gets its color.
    if (shouldBeOn) {
      on.fire();
    } else {
      off.fire();
    }

    // Only watch for the on button. Since it's a toggle group, this will trigger no matter which
    // button is clicked.
    model
        .lightbulbConditionPropertyAt(index)
        .addListener((obs, oldValue, newValue) -> updateIfNeeded(newValue, on));

    on.selectedProperty()
        .addListener(
            (obs, oldValue, newValue) -> controller.updateLightbulbConditionAt(index, newValue));
  }

  public OnOffToggle(SmartplugModel model, SmartplugController controller, int index) {
    createButtons();
    shouldBeOn = model.getSmartplugConditionAt(index);
    // Fire the event of the selected button so it gets its color.
    if (shouldBeOn) {
      on.fire();
    } else {
      off.fire();
    }

    // Only watch for the on button. Since it's a toggle group, this will trigger no matter which
    // button is clicked.
    model
        .smartplugConditionPropertyAt(index)
        .addListener((obs, oldValue, newValue) -> updateIfNeeded(newValue, on));

    on.selectedProperty()
        .addListener(
            (obs, oldValue, newValue) -> controller.updateSmartplugConditionAt(index, newValue));
  }

  public OnOffToggle(
      ThermostatModel model,
      ThermostatController controller,
      int index,
      boolean isTemperatureUnit) {
    if (isTemperatureUnit) {
      createUnitButtons();
    } else {
      createButtons();
    }

    if (isTemperatureUnit) {
      shouldBeOn = model.getThermostatIsCelsiusAt(index);
    } else {
      shouldBeOn = model.getThermostatConditionAt(index);
    }

    // Fire the event of the selected button so it gets its color.
    if (shouldBeOn) {
      on.fire();
    } else {
      off.fire();
    }

    // Only watch for the on button. Since it's a toggle group, this will trigger no matter which
    // button is clicked.
    if (isTemperatureUnit) {
      model
          .thermostatIsCelsiusPropertyAt(index)
          .addListener((obs, oldValue, newValue) -> updateIfNeeded(newValue, on));

      on.selectedProperty()
          .addListener(
              (obs, oldValue, newValue) -> controller.updateTemperatureUnit(index, newValue));
    } else {
      model
          .thermostatConditionPropertyAt(index)
          .addListener((obs, oldValue, newValue) -> updateIfNeeded(newValue, on));

      on.selectedProperty()
          .addListener(
              (obs, oldValue, newValue) -> controller.updateThermostatConditionAt(index, newValue));
    }
  }

  public HBox getContainer() {
    return container;
  }

  public boolean isDisabled() {
    return container.isDisabled();
  }

  public void setDisable(boolean value) {
    container.setDisable(value);
  }

  private void updateIfNeeded(Boolean value, ToggleButton button) {
    // maybe disarm, fire, arm
    if (button.isSelected() != value) {
      button.setSelected(value);
    }
  }

  private void createButtons() {
    group = new ToggleGroup();

    // Lightbulb toggle.
    on = new ToggleButton("ON");
    on.setStyle("-fx-base: grey;");
    on.setToggleGroup(group);

    off = new ToggleButton("OFF");
    off.setStyle("-fx-base: grey;");
    off.setToggleGroup(group);

    on.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent e) {
            if (shouldBeOn) {
              on.setStyle("-fx-base: green;");
              off.setStyle("-fx-base: grey;");
            } else {
              on.setStyle("-fx-base: grey;");
              off.setStyle("-fx-base: red;");
            }
            shouldBeOn = !shouldBeOn;
          }
        });

    off.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent e) {
            if (!shouldBeOn) {
              on.setStyle("-fx-base: grey;");
              off.setStyle("-fx-base: red;");
            } else {
              on.setStyle("-fx-base: green;");
              off.setStyle("-fx-base: grey;");
              on.setSelected(true);
            }
            shouldBeOn = !shouldBeOn;
          }
        });

    container = new HBox(on, off);
  }

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
