package ca.uvic.seng330.assn3.devices.camera;

import ca.uvic.seng330.assn3.OnOffToggle;
import ca.uvic.seng330.assn3.RecordToggle;
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
  private List<RecordToggle> cameraRecordSwitches;
  private List<OnOffToggle> cameraSwitches;
  private List<Text> dataText;
  private Button homeButton;

  /** Default constructor for the Camera view. */
  public CameraView(CameraController controller, CameraModel model) {
    this.controller = controller;
    this.model = model;

    createAndConfigurePane();
    createAndLayoutControls();
  }

  public Parent asParent() {
    return view;
  }

  /**
   * Enables or disables the controls for a camera at a given index.
   *
   * @param index the index of the controls to enable/disable
   * @param value true to disable, false to enable
   */
  private void setIndexDisabled(int index, boolean value) {
    if (cameraRecordSwitches.get(index).isDisabled() != value) {
      cameraRecordSwitches.get(index).setDisable(value);

      if (value) {
        dataText.get(index).setText("NO FEED");
      } else {
        dataText.get(index).setText("FEED SHOWN");
      }
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
    view.setHgap(10);
    view.setVgap(10);
  }

  /** Creates the controls within the view and hooks it up with the model. */
  private void createAndLayoutControls() {
    cameraRecordSwitches = new ArrayList<RecordToggle>();
    cameraSwitches = new ArrayList<OnOffToggle>();
    dataText = new ArrayList<Text>();
    title = new Text("Camera Settings");
    title.setFont(new Font(20));
    view.addRow(0, title);

    for (int i = 0; i < model.getCameraCount(); i++) {
      RecordToggle record = new RecordToggle(model, controller, i);

      cameraRecordSwitches.add(record);

      OnOffToggle power = new OnOffToggle(model, controller, i);

      cameraSwitches.add(power);

      Text data = new Text();
      data.setText("FEED SHOWN");
      dataText.add(data);

      addConditionListener(i);

      // Set the button state on init.
      setIndexDisabled(i, !model.getCameraConditionAt(i));

      int startRow = 1 + (i * 3);

      view.addRow(
          startRow, new Label("Camera " + new Integer(i + 1).toString()), power.getContainer());
      view.addRow(startRow + 1, new Label("Recording state:"), record.getContainer());
      view.addRow(startRow + 2, new Label("Video feed: "), data);
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

  /**
   * Adds a listener to the camera condition property at a given index to trigger the
   * enabling/disabling of controls.
   *
   * @param i the index of the camera condition property
   */
  private void addConditionListener(int i) {
    model
        .cameraConditionPropertyAt(i)
        .addListener((obs, oldValue, newValue) -> setIndexDisabled(i, !newValue));

    model
        .cameraDataPropertyAt(i)
        .addListener((obs, oldValue, newValue) -> updateIfNeeded(newValue, dataText.get(i)));
  }

  /**
   * Checks if an update is needed for a given text field based on the value given.
   *
   * @param value the new value to set if the value isn't already set to this
   * @param field the field to check against and potentially set
   */
  private void updateIfNeeded(String value, Text field) {
    if (!field.getText().equals(value)) {
      field.setText(value);
    }
  }
}
