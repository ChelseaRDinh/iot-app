package ca.uvic.seng330.assn3.devices.thermostat;

import ca.uvic.seng330.assn3.OnOffToggle;
import ca.uvic.seng330.assn3.devices.Temperature.Unit;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ThermostatView {
  private GridPane view;
  private ThermostatController controller;
  private ThermostatModel model;
  private Text title;
  private Button homeButton;
  private List<Button> setTemperatureButtons;
  private List<OnOffToggle> thermostatUnitSwitches;
  private List<OnOffToggle> thermostatPowerSwitches;
  private List<TextField> temperatureFields;
  private List<Text> errorMsgs;

  /** Default constructor for the Thermostat view. */
  public ThermostatView(ThermostatController controller, ThermostatModel model) {
    this.controller = controller;
    this.model = model;
    createAndConfigurePane();
    createAndLayoutControls();
  }

  public Parent asParent() {
    return view;
  }

  /**
   * Enables or disables the controls for a thermostat at a given index.
   *
   * @param index the index of the controls to enable/disable
   * @param value true to disable, false to enable
   */
  private void setIndexDisabled(int index, boolean value) {
    if (temperatureFields.get(index).isDisabled() != value) {
      temperatureFields.get(index).setDisable(value);
    }

    if (thermostatUnitSwitches.get(index).isDisabled() != value) {
      thermostatUnitSwitches.get(index).setDisable(value);
    }

    if (setTemperatureButtons.get(index).isDisabled() != value) {
      setTemperatureButtons.get(index).setDisable(value);
    }
  }

  /** Creates the view grid pane. */
  private void createAndConfigurePane() {
    view = new GridPane();

    ColumnConstraints leftCol = new ColumnConstraints();
    leftCol.setHalignment(HPos.RIGHT);
    leftCol.setHgrow(Priority.NEVER);

    ColumnConstraints rightCol = new ColumnConstraints();
    rightCol.setHgrow(Priority.NEVER);

    view.getColumnConstraints().addAll(leftCol, rightCol);

    view.setAlignment(Pos.CENTER);
    view.setHgap(5);
    view.setVgap(10);
  }

  /** Creates the controls within the view and hooks it up with the model. */
  private void createAndLayoutControls() {
    thermostatUnitSwitches = new ArrayList<OnOffToggle>();
    thermostatPowerSwitches = new ArrayList<OnOffToggle>();
    setTemperatureButtons = new ArrayList<Button>();
    errorMsgs = new ArrayList<Text>();
    temperatureFields = new ArrayList<TextField>();

    title = new Text("Thermostat Settings");
    title.setFont(new Font(20));

    view.addRow(0, title);

    // This thousand line for loop really has to go if there's time.
    for (int i = 0; i < model.getThermostatCount(); i++) {
      OnOffToggle toggle = new OnOffToggle(model, controller, i, true);

      thermostatUnitSwitches.add(toggle);

      TextField temperatureField = new TextField();
      temperatureField.setMaxWidth(80);
      temperatureField.setText(model.getThermostatValueAt(i).toString());
      temperatureField.setId("temperatureField" + new Integer(i + 1).toString());

      // Configure text field to take in double values for temperature.
      configTextFieldForFloats(temperatureField);

      Button setTemperature = new Button("Set");
      Text errorMsg = new Text();

      setupTemperatureSet(setTemperature, temperatureField, errorMsg, i);

      setTemperatureButtons.add(setTemperature);
      errorMsgs.add(errorMsg);
      temperatureFields.add(temperatureField);

      OnOffToggle powerToggle = new OnOffToggle(model, controller, i, false);

      thermostatPowerSwitches.add(toggle);

      // Set the button state on init.
      setIndexDisabled(i, !model.getThermostatConditionAt(i));

      int startRow = 1 + (i * 4);

      view.addRow(
          startRow,
          new Label("Thermostat " + new Integer(i + 1).toString()),
          powerToggle.getContainer());
      view.addRow(startRow + 1, new Label("Unit: "), toggle.getContainer());
      view.addRow(startRow + 2, new Label("Temperature: "), temperatureField);
      view.add(setTemperature, 2, startRow + 2);
      view.addRow(startRow + 3, new Label(""), errorMsg);
    }

    homeButton = new Button("Home");

    homeButton.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent e) {
            controller.home();
          }
        });

    view.addRow(1 + model.getThermostatCount() * 4, new Label(""), homeButton);
  }

  /**
   * Sets up the action for the set temperature button and the thermostat model listeners. Updates
   * the temperature field and error message on click. This stops entering a value into the
   * temperature triggering a model value update and resetting the temperature field value.
   */
  private void setupTemperatureSet(
      Button setTemperature, TextField temperatureField, Text errorMsg, int i) {
    setTemperature.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent e) {
            float currentValue = model.getThermostatValueAt(i);

            boolean isFloat = false;
            float setValue = currentValue;
            try {
              setValue = Float.parseFloat(temperatureField.getText());
              isFloat = true;
            } catch (Exception ex) {
              // Make sure the value is reset.
              setValue = currentValue;
            }

            if (!isFloat) {
              errorMsg.setFill(Color.rgb(210, 39, 30));
              errorMsg.setText("Value entered isn't a number.");
            } else if ((setValue >= 37 && model.getThermostatIsCelsiusAt(i))
                || (setValue >= Unit.convertUnits(37, Unit.CELSIUS, Unit.FAHRENHEIT)
                    && !model.getThermostatIsCelsiusAt(i))) {
              setValue = currentValue;
              errorMsg.setFill(Color.rgb(210, 39, 30));
              errorMsg.setText("Temperature exceeds max value.");
            } else {
              errorMsg.setFill(Color.rgb(16, 152, 0));
              errorMsg.setText(" ");
            }

            // Always set the value based on the known valid value and rese the text field to that.
            controller.updateTemperatureValue(i, setValue);
            temperatureField.setText(model.getThermostatValueAt(i).toString());
          }
        });

    // Only update the model when the model value changes.
    model
        .thermostatValuePropertyAt(i)
        .addListener(
            (obs, oldTemperatureValue, newTemperatureValue) ->
                updateIfNeeded(newTemperatureValue, temperatureField));

    // Disable/enable buttons based on if the thermostat is on or not.
    model
        .thermostatConditionPropertyAt(i)
        .addListener((obs, oldValue, newValue) -> setIndexDisabled(i, !newValue));
  }

  /**
   * Checks if an update is needed for a given text field based on the value given.
   *
   * @param value the new value to set if the value isn't already set to this
   * @param field the field to check against and potentially set
   */
  private void updateIfNeeded(Number value, TextField field) {
    String s = value.toString();
    if (!field.getText().equals(s)) {
      field.setText(s);
    }
  }

  /**
   * Configures a given text field to take in decimal values.
   *
   * @param field the field to configure
   */
  private void configTextFieldForFloats(TextField field) {
    field.setTextFormatter(
        new TextFormatter<Float>(
            (Change c) -> {
              // Change regex to allow decimal values.
              if (c.getControlNewText().matches("-?\\d*\\.?\\d*")) {
                return c;
              }
              return null;
            }));
  }
}
