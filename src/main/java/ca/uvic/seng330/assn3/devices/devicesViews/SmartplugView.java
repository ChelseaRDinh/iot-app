package ca.uvic.seng330.assn3.devices.devicesViews;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class SmartplugView {
  private GridPane view;
  private Text title;
  private ToggleGroup group;
  private ToggleButton on;
  private ToggleButton off;

  public SmartplugView() {
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
    title = new Text("Smartplug Settings");
    title.setFont(new Font(20));
    group = new ToggleGroup();

    // Lightbulb toggle.
    on = new ToggleButton("ON");
    on.setUserData(Color.GREEN);
    on.setToggleGroup(group);

    off = new ToggleButton("OFF");
    off.setUserData(Color.RED);
    off.setToggleGroup(group);

    HBox SmartplugContainer = new HBox(on, off);

    view.addRow(0, title);
    view.addRow(2, new Label("Switch:"), SmartplugContainer);
  }

  private void updateControllerFromListeners() {}

  private void observeModelAndUpdateControls() {}
}
