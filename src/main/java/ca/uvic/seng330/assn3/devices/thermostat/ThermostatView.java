package ca.uvic.seng330.assn3.devices.thermostat;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;

public class ThermostatView {
  private GridPane view;
  private ThermostatController controller;
  private ThermostatModel model;
  private Text title;
  private Button homeButton;
  private Button fahrenheit;
  private Button celsius;
  private TextField temperatureField;
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
    temperatureField = new TextField();
    temperatureField.setMaxWidth(80);
    //Configure text field to take in double values for temperature.
    configTextFieldForDoubles(temperatureField);

    view.addRow(0, title);
    view.addRow(1, new Label("Set Temperature: "), temperatureField);
    view.addRow(2, new Label(""), homeButton);

    homeButton.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent e) {
            controller.home();
          }
        });
  }

  private void updateControllerFromListeners() {}

  private void observeModelAndUpdateControls() {}

  /*
  * Adapted from AdditionView.java in Examples folder for starter code repo.
  * Source: https://www.github.com/seng330
  */
  private void configTextFieldForDoubles(TextField field) {
    field.setTextFormatter(
        new TextFormatter<Double>(
            (Change c) -> {
              //Change regex to allow decimal values.
              if (c.getControlNewText().matches("\\d*(\\.\\d*)?")) {
                return c;
              }
              return null;
            }));
  }
}
