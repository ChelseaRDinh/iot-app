package ca.uvic.seng330.assn3.admin;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class AdminView {
  private GridPane view;
  private AdminController controller;
  private AdminModel model;
  private Text title;
  private Button manageUsersButton;
  private Button manageDevicesButton;

  /** Default constructor for the Admin Dashboard view. */
  public AdminView(AdminController controller, AdminModel model) {
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
    title = new Text("Admin console");
    title.setFont(new Font(20));

    manageUsersButton = new Button("Manage Users");
    manageDevicesButton = new Button("Manage Devices");

    view.addRow(0, title);
    view.addRow(1, manageUsersButton);
    view.addRow(2, manageDevicesButton);

    manageUsersButton.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent e) {
            controller.manageUsersGUI();
          }
        });
  }

  private void updateControllerFromListeners() {}

  private void observeModelAndUpdateControls() {}
}
