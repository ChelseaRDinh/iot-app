package ca.uvic.seng330.assn3.devices.lightbulb;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class LightbulbView {
  private GridPane view;
  private LightbulbController controller;
  private LightbulbModel model;
  private Text title;
  private ToggleGroup group;
  private ToggleButton on;
  private ToggleButton off;
  private Button homeButton;

  /** Default constructor for the Lightbulb view. */
  public LightbulbView(LightbulbController controller, LightbulbModel model) {
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
    view.setHgap(10);
    view.setVgap(10);
  }

  private void createAndLayoutControls() {
    title = new Text("Lightbulb Settings");
    title.setFont(new Font(20));
    group = new ToggleGroup();

    // Lightbulb toggle.
    on = new ToggleButton("ON");
    on.setStyle("-fx-base: grey;");
    on.setToggleGroup(group);

    off = new ToggleButton("OFF");
    off.setStyle("-fx-base: grey;");
    off.setToggleGroup(group);

    HBox lightbulbContainer = new HBox(on, off);

    homeButton = new Button("Home");

    view.addRow(0, title);
    view.addRow(2, new Label("Switch:"), lightbulbContainer);
    view.addRow(3, new Label(""), homeButton);

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
