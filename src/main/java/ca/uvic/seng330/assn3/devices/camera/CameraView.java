package ca.uvic.seng330.assn3.devices.camera;

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
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class CameraView {
  private GridPane view;
  private CameraController controller;
  private CameraModel model;
  private Text title;
  private Button recordButton;
  private Button home;

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
    title = new Text("Camera Settings");
    title.setFont(new Font(20));

    // By default, recording is 'off'.
    recordButton = new Button("OFF");
    recordButton.setStyle("-fx-base: red;");

    double r = 30;
    recordButton.setShape(new Circle(r));
    recordButton.setMinSize(2 * r, 2 * r);
    recordButton.setMaxSize(2 * r, 2 * r);

    home = new Button("home");

    view.addRow(0, title);
    view.addRow(2, new Label("Record:"), recordButton);
    view.addRow(3, new Label(""), home);

    // toggle record button.
    recordButton.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent e) {
            if (recordButton.getText() == "OFF") {
              recordButton.setText("ON");
              recordButton.setStyle("-fx-base: green;");
            } else {
              recordButton.setText("OFF");
              recordButton.setStyle("-fx-base: red;");
            }
          }
        });
  }

  private void updateControllerFromListeners() {}

  private void observeModelAndUpdateControls() {}
}
