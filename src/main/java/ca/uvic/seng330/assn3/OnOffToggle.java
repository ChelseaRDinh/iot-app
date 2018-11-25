package ca.uvic.seng330.assn3;

import ca.uvic.seng330.assn3.devices.camera.CameraController;
import ca.uvic.seng330.assn3.devices.camera.CameraModel;
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
  protected HBox container;
  protected ToggleGroup group;
  protected ToggleButton on;
  protected ToggleButton off;

  protected boolean shouldBeOn = false;

  /** Default constructor for the on off toggle button. */
  public OnOffToggle() {
    createButtons();
  }

  /**
   * Constructor for the on/off button for the lightbulb UI.
   *
   * @param model the lightbulb's model
   * @param controller the lightbulb's controller
   * @param index the index of the lightbulb in the model to bind this on/off button to
   */
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

  /**
   * Constructor for the on/off button for the smartplug UI.
   *
   * @param model the smartplug's model
   * @param controller the smartplug's controller
   * @param index the index of the smartplug in the model to bind this on/off button to
   */
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

  /**
   * Constructor for the on/off button for the thermostat UI.
   *
   * @param model the thermostat's model
   * @param controller the thermostat's controller
   * @param index the index of the thermostat in the model to bind this on/off button to
   */
  public OnOffToggle(ThermostatModel model, ThermostatController controller, int index) {
    createButtons();

    shouldBeOn = model.getThermostatConditionAt(index);

    // Fire the event of the selected button so it gets its color.
    if (shouldBeOn) {
      on.fire();
    } else {
      off.fire();
    }

    // Only watch for the on button. Since it's a toggle group, this will trigger no matter which
    // button is clicked.
    model
        .thermostatConditionPropertyAt(index)
        .addListener((obs, oldValue, newValue) -> updateIfNeeded(newValue, on));

    on.selectedProperty()
        .addListener(
            (obs, oldValue, newValue) -> controller.updateThermostatConditionAt(index, newValue));
  }

  /**
   * Constructor for the on/off toggle button for the camera UI.
   *
   * @param model the camera's model
   * @param controller the camera's controller
   * @param index the index of the camera in the model to bind this on/off button to
   */
  public OnOffToggle(CameraModel model, CameraController controller, int index) {
    createButtons();

    shouldBeOn = model.getCameraConditionAt(index);

    // Fire the event of the selected button so it gets its color.
    if (shouldBeOn) {
      on.fire();
    } else {
      off.fire();
    }

    // Only watch for the on button. Since it's a toggle group, this will trigger no matter which
    // button is clicked.
    model
        .cameraConditionPropertyAt(index)
        .addListener((obs, oldValue, newValue) -> updateIfNeeded(newValue, on));

    on.selectedProperty()
        .addListener(
            (obs, oldValue, newValue) -> controller.updateCameraConditionAt(index, newValue));
  }

  /**
   * Gets the container for the on/off toggle button.
   *
   * @return the container for the toggle button
   */
  public HBox getContainer() {
    return container;
  }

  /**
   * Gets whether or not the button is disabled.
   *
   * @return true if the button is enabled, false otherwise
   */
  public boolean isDisabled() {
    return container.isDisabled();
  }

  /**
   * Enalbes/disables the button.
   *
   * @param value true to disable, false to enable
   */
  public void setDisable(boolean value) {
    container.setDisable(value);
  }

  /**
   * Updates the value of a given button if the new value is different than the button's current
   * value.
   *
   * @param value the value to try and set the button to
   * @param button the button to potentially set the value
   */
  protected void updateIfNeeded(Boolean value, ToggleButton button) {
    // maybe disarm, fire, arm
    if (button.isSelected() != value) {
      button.setSelected(value);
    }
  }

  /** Creates the on/off toggle button group. */
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
}
