package ca.uvic.seng330.assn3.devices.thermostat;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.paint.Color;

public class ThermostatView {
  private GridPane view;
  private ThermostatController controller;
  private ThermostatModel model;
  private Text title;
  private Button homeButton;
  private Button setTemperature;
  private ToggleButton fahrenheitMode;
  private ToggleButton celsiusMode;
  private TextField temperatureField;
  private ToggleGroup group;
  private Text errorMsg;

  /** Default constructor for the Thermostat view. */
  public ThermostatView(ThermostatController controller, ThermostatModel model) {
    this.controller = controller;
    this.model = model;
    createAndConfigurePane();
    createAndLayoutControls();
    updateControllerFromListeners();
    observeModelAndUpdateControls();
  }

  public Parent asParent() {
    return view;
  }

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

  private void createAndLayoutControls() {

    title = new Text("Thermostat Settings");
    title.setFont(new Font(20));

    homeButton = new Button("Home");

    //Temperature modes
    group = new ToggleGroup();

    fahrenheitMode = new ToggleButton("Fahrenheit");
    fahrenheitMode.setStyle("-fx-base: grey;");
    fahrenheitMode.setToggleGroup(group);

    celsiusMode = new ToggleButton("Celsius");
    celsiusMode.setStyle("-fx-base: blue;");
    celsiusMode.setToggleGroup(group);
    //By default, set mode to be celsius.
    celsiusMode.setSelected(true);
    model.setMode(TempMode.CELSIUS);

    HBox thermostatContainer = new HBox(fahrenheitMode, celsiusMode);

    setTemperature = new Button("Set");

    errorMsg = new Text();

    temperatureField = new TextField();
    temperatureField.setMaxWidth(80);
    //Configure text field to take in double values for temperature.
    configTextFieldForDoubles(temperatureField);
  
    homeButton.setOnAction(
      new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
          controller.home();
        }
      });
      
    //Set buttons to blue when selected; opposite button is set back to default grey.
    fahrenheitMode.setOnAction(
      new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
          fahrenheitMode.setStyle("-fx-base: blue;");
          celsiusMode.setStyle("-fx-base: grey;");
          model.setMode(TempMode.FAHRENHEIT);
        }
      });
    celsiusMode.setOnAction(
      new EventHandler<ActionEvent>() {
      @Override
        public void handle(ActionEvent e) {
          fahrenheitMode.setStyle("-fx-base: grey;");
          celsiusMode.setStyle("-fx-base: blue;");
          model.setMode(TempMode.CELSIUS);
        }
      });

      setTemperature.setOnAction(
        new EventHandler<ActionEvent>() {
        @Override
          public void handle(ActionEvent e) {
            if((model.getTemperatureValue() >= 37) && (model.getMode()==TempMode.CELSIUS) || (model.getTemperatureValue() >= 100) && (model.getMode()==TempMode.FAHRENHEIT)) {
              errorMsg.setFill(Color.rgb(210, 39, 30));
              errorMsg.setText("Temperature exceeds max value.");
            } else {
              errorMsg.setFill(Color.rgb(16, 152, 0));
              errorMsg.setText("New temperature set.");
            }
          }
        });

    view.addRow(0, title);
    view.addRow(1, new Label("Mode: "), thermostatContainer);
    view.addRow(2, new Label("Temperature: "), temperatureField);
    view.add(setTemperature, 2, 2);
    view.addRow(3, new Label(""), errorMsg);
    view.addRow(4, new Label(""), homeButton);

  }

  private void observeModelAndUpdateControls() {
    model.temperatureValueProperty().addListener((obs, oldTemperatureValue, newTemperatureValue) -> updateIfNeeded(newTemperatureValue, temperatureField));
  }

  private void updateIfNeeded(Number value, TextField field) {
    String s = value.toString();
    if (!field.getText().equals(s)) {
      field.setText(s);
    }
  }

  private void updateControllerFromListeners() {
    temperatureField.textProperty().addListener((obs, oldText, newText) -> controller.updateTemperatureValue(newText));
  }

  /*
  * Adapted from AdditionView.java in Examples folder for starter code repo.
  * Source: https://www.github.com/seng330
  * Decimal keeps being added prematurely. Changing to to an integer field for now.
  */
  private void configTextFieldForFloats(TextField field) {
    field.setTextFormatter(
        new TextFormatter<Float>(
            (Change c) -> {
              //Change regex to allow decimal values.
              if (c.getControlNewText().matches("-?\\d*")) {
                return c;
              }
              return null;
            }));
  }
}
