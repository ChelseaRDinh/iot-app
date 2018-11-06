package ca.uvic.seng330.assn3.admin;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class AdminDashboard {
  private GridPane view;
  private Text title;
  private Button manageUsers;
  private Button manageDevices;

  public AdminDashboard() {
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
    title = new Text("Admin console");
    title.setFont(new Font(20));

    manageUsers = new Button("Manage Users");
    manageDevices = new Button("Manage Devices");

    view.addRow(0, title);
    view.addRow(1, manageUsers);
    view.addRow(2, manageDevices);
  }

  private void updateControllerFromListeners() {}

  private void observeModelAndUpdateControls() {}
}
