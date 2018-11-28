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
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class CameraView {
  private ScrollPane pane;
  private GridPane view;
  private CameraController controller;
  private CameraModel model;
  private Text title;
  private List<RecordToggle> cameraRecordSwitches;
  private List<OnOffToggle> cameraSwitches;
  private List<Text> dataText;
  private List<MediaView> mediaViews;
  private Button homeButton;

  /** Default constructor for the Camera view. */
  public CameraView(CameraController controller, CameraModel model) {
    this.controller = controller;
    this.model = model;

    createAndConfigurePane();
    createAndLayoutControls();
  }

  public Parent asParent() {
    return pane; // view;
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

      dataText.get(index).setVisible(value);
      dataText.get(index).setManaged(value);
      mediaViews.get(index).setVisible(!value);
      mediaViews.get(index).setManaged(!value);
    }
  }

  /** Creates the view grid pane. */
  private void createAndConfigurePane() {
    pane = new ScrollPane();
    view = new GridPane();

    pane.setContent(view);

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
    mediaViews = new ArrayList<MediaView>();
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

      MediaView mediaView = new MediaView(model.getMediaPlayer(i));
      mediaView.setId("player" + Integer.toString(i));
      mediaViews.add(mediaView);

      addConditionListener(i);

      // Set the button state on init.
      setIndexDisabled(i, !model.getCameraConditionAt(i));

      int startRow = 1 + (i * 4);

      view.addRow(
          startRow, new Label("Camera " + new Integer(i + 1).toString()), power.getContainer());
      view.addRow(startRow + 1, new Label("Recording state:"), record.getContainer());
      view.addRow(startRow + 2, new Label("Video feed: "), data);
      view.addRow(startRow + 3, new Label(""), mediaView);
    }

    homeButton = new Button("home");
    homeButton.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent e) {
            controller.home();
          }
        });
    view.addRow(2 + model.getCameraCount() * 4, new Label(""), homeButton);
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
  }
}
