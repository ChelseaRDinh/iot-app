package ca.uvic.seng330.assn3.devices.smartplug;

import ca.uvic.seng330.assn3.OnOffToggle;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class SmartplugView {
  private GridPane view;
  private SmartplugController controller;
  private SmartplugModel model;
  private Text title;
  private ToggleGroup group;
  private ToggleButton on;
  private ToggleButton off;
  private Button homeButton;
  private List<OnOffToggle> smartplugSwitches;

  /** Default constructor for the Smartplug view. */
  public SmartplugView(SmartplugController controller, SmartplugModel model) {
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
    smartplugSwitches = new ArrayList<OnOffToggle>();
    title = new Text("Smartplug Settings");
    title.setFont(new Font(20));
    view.addRow(0, title);

    for (int i = 0; i < model.getCount(); i++) {
      OnOffToggle toggle = new OnOffToggle(model, controller, i);
      smartplugSwitches.add(toggle);

      view.addRow(
          2 + i,
          new Label("Switch " + new Integer(i + 1).toString() + ": "),
          toggle.getContainer());
    }

    homeButton = new Button("Home");
    homeButton.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent e) {
            controller.home();
          }
        });
    view.addRow(2 + model.getCount() + 1, new Label(""), homeButton);
  }

  private void updateControllerFromListeners() {}

  private void observeModelAndUpdateControls() {}
}
