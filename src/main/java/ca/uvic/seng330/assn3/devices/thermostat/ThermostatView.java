package ca.uvic.seng330.assn3.devices.thermostat;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ThermostatView {
  private GridPane view;
  private ThermostatController controller;
  private ThermostatModel model;
  private Slider tempSlider;
  private Text title;
  private Button homeButton;

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

    // Slider for changing temperature.
    tempSlider = new Slider(0.0, 50.0, 100.0);
    tempSlider.setShowTickMarks(true);
    tempSlider.setShowTickLabels(true);

    homeButton = new Button("Home");

    view.addRow(0, title);
    view.addRow(1, new Label("Temperature:"), tempSlider);
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
}
