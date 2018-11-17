package ca.uvic.seng330.assn3.devices.camera;

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

public class CameraView {
  private GridPane view;
  private CameraController controller;
  private CameraModel model;
  private Text title;
  private List<OnOffToggle> cameraRecordSwitches;
  private List<Text> dataText;
  private Button homeButton;

  /** Default constructor for the Camera view. */
  public CameraView(CameraController controller, CameraModel model) {
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
    cameraRecordSwitches = new ArrayList<OnOffToggle>();
    dataText = new ArrayList<Text>();
    title = new Text("Camera Settings");
    title.setFont(new Font(20));
    view.addRow(0, title);

    for (int i = 0; i < model.getCameraCount(); i++) {
      OnOffToggle record = new OnOffToggle(model, controller, i, false);

      cameraRecordSwitches.add(record);

      Text data = new Text();
      data.setText("DATA SHOWN");
      dataText.add(data);

      // Set the button state on init.
      // setIndexDisabled(i, !model.getThermostatConditionAt(i));

      int startRow = 1 + (i * 2);

      view.addRow(
          startRow, new Label("Camera " + new Integer(i + 1).toString()), record.getContainer());
      view.addRow(startRow + 1, new Label("Video feed: "), data);
    }

    homeButton = new Button("home");
    homeButton.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent e) {
            controller.home();
          }
        });
    view.addRow(2 + model.getCameraCount() * 3, new Label(""), homeButton);
  }

  private void updateControllerFromListeners() {}

  private void observeModelAndUpdateControls() {}
}
