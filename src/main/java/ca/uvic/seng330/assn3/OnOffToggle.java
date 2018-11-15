package ca.uvic.seng330.assn3;

import ca.uvic.seng330.assn3.devices.lightbulb.LightbulbController;
import ca.uvic.seng330.assn3.devices.lightbulb.LightbulbModel;
import ca.uvic.seng330.assn3.devices.smartplug.SmartplugController;
import ca.uvic.seng330.assn3.devices.smartplug.SmartplugModel;
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

  /** Default constructor for the on off toggle button. */
  public OnOffToggle() {
    createButtons();
  }

  public OnOffToggle(LightbulbModel model, LightbulbController controller, int index) {
    createButtons();
    on.setId("lightbulbOn" + new Integer(index).toString());
    off.setId("lightbulbOff" + new Integer(index).toString());

    // Fire the event of the selected button so it gets its color.
    if (model.getLightbulbConditionAt(index)) {
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
    on.setId("lightbulbOn" + new Integer(index).toString());
    off.setId("lightbulbOff" + new Integer(index).toString());

    // Fire the event of the selected button so it gets its color.
    if (model.getSmartplugConditionAt(index)) {
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

  public HBox getContainer() {
    return container;
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
            on.setStyle("-fx-base: green;");
            off.setStyle("-fx-base: grey;");
          }
        });

    off.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent e) {
            on.setStyle("-fx-base: grey;");
            off.setStyle("-fx-base: red;");
          }
        });

    container = new HBox(on, off);
  }
}
