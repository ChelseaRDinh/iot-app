package ca.uvic.seng330.assn3.devices.camera;

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
  private Text title;
  private Button recordButton;

  /** Default constructor for the Camera view. */
  public CameraView() {
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

    recordButton = new Button("OFF");

    double r = 30;
    recordButton.setShape(new Circle(r));
    recordButton.setMinSize(2 * r, 2 * r);
    recordButton.setMaxSize(2 * r, 2 * r);
    recordButton.setStyle("-fx-base: red;");

    recordButton.setOnAction(actionEvent -> recordButton.setText("ON"));
    recordButton.setOnAction(actionEvent -> recordButton.setStyle("-fx-base: green;"));

    view.addRow(0, title);
    view.addRow(2, new Label("Record:"), recordButton);

    // Camera Disk Space bar to go here.

  }

  private void updateControllerFromListeners() {}

  private void observeModelAndUpdateControls() {}
}
