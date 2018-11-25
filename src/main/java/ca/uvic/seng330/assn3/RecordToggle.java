package ca.uvic.seng330.assn3;

import ca.uvic.seng330.assn3.devices.camera.CameraController;
import ca.uvic.seng330.assn3.devices.camera.CameraModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

public class RecordToggle extends OnOffToggle {
  /**
   * Constructor for the record/stop toggle button for the camera UI.
   *
   * @param model the camera's model
   * @param controller the camera's controller
   * @param index the index of the camera in the model to bind this on/off button to
   */
  public RecordToggle(CameraModel model, CameraController controller, int index) {
    createRecordButtons();

    shouldBeOn = model.getCameraIsRecordingAt(index);

    // Fire the event of the selected button so it gets its color.
    if (shouldBeOn) {
      on.fire();
    } else {
      off.fire();
    }

    // Only watch for the on button. Since it's a toggle group, this will trigger no matter which
    // button is clicked.
    model
        .cameraIsRecordingPropertyAt(index)
        .addListener((obs, oldValue, newValue) -> updateIfNeeded(newValue, on));

    on.selectedProperty()
        .addListener(
            (obs, oldValue, newValue) -> controller.updateCameraRecordingAt(index, newValue));
  }

  /** Creates a record/stop toggle button group. */
  private void createRecordButtons() {
    group = new ToggleGroup();

    // Lightbulb toggle.
    on = new ToggleButton("Record");
    on.setStyle("-fx-base: grey;");
    on.setToggleGroup(group);

    off = new ToggleButton("Stop");
    off.setStyle("-fx-base: grey;");
    off.setToggleGroup(group);

    on.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent e) {
            if (shouldBeOn) {
              on.setStyle("-fx-base: green;");
              off.setStyle("-fx-base: grey;");
            } else {
              on.setStyle("-fx-base: grey;");
              off.setStyle("-fx-base: red;");
            }
            shouldBeOn = !shouldBeOn;
          }
        });

    off.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent e) {
            if (!shouldBeOn) {
              on.setStyle("-fx-base: grey;");
              off.setStyle("-fx-base: red;");
            } else {
              on.setStyle("-fx-base: green;");
              off.setStyle("-fx-base: grey;");
              on.setSelected(true);
            }
            shouldBeOn = !shouldBeOn;
          }
        });

    container = new HBox(on, off);
  }
}
