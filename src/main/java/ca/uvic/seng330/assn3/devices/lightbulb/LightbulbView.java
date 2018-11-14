package ca.uvic.seng330.assn3.devices.lightbulb;

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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class LightbulbView {
  private GridPane view;
  private LightbulbController controller;
  private LightbulbModel model;
  private Text title;
  private List<OnOffToggle> lightbulbSwitches;
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
    lightbulbSwitches = new ArrayList<OnOffToggle>();
    title = new Text("Lightbulb Settings");
    title.setFont(new Font(20));
    view.addRow(0, title);

    for (int i = 0; i < model.getCount(); i++) {
      OnOffToggle toggle = new OnOffToggle(model, controller, i);
      lightbulbSwitches.add(toggle);

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

  // The model will get the UUIDs, make a list of boolean properties, and we will bind to that. The
  // model will need to extend the client class. Then it can send a message to get xondition of all
  // bulbs.
  private void updateControllerFromListeners() {}

  private void observeModelAndUpdateControls() {}
}
