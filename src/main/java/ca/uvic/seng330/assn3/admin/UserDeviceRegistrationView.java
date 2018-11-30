package ca.uvic.seng330.assn3.admin;

import ca.uvic.seng330.assn3.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class UserDeviceRegistrationView {
  private UserDeviceRegistrationController controller;
  private UserDeviceRegistrationModel model;
  private TableColumn userName;
  private TableColumn firstName;
  private TableColumn lastName;
  private TableView<User> userTable;
  private ObservableList<User> userData;
  private String[] users;
  private GridPane view;
  private Text title;
  private Button backButton;
  private Button manageDevicesButton;

  public UserDeviceRegistrationView(
      UserDeviceRegistrationController controller, UserDeviceRegistrationModel model) {
    this.controller = controller;
    this.model = model;

    createAndConfigurePane();
    createAndLayoutControls();
    observeModelAndUpdateControls();
  }

  public Parent asParent() {
    return view;
  }

  public void createAndConfigurePane() {
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

  public void createAndLayoutControls() {
    title = new Text("Currently Registered Devices for Users");
    title.setFont(new Font(20));

    addButtonActions();
    createUserTable();
    addUsersToTable();

    view.addRow(0, title);
    view.addRow(1, userTable);
    view.addRow(2, backButton);
  }

  private void observeModelAndUpdateControls() {}

  private void createUserTable() {
    userTable = new TableView<User>();
    userTable.setEditable(true);
    userData = FXCollections.observableArrayList();

    userName = new TableColumn("Username");
    userName.setMinWidth(100);
    userName.setCellValueFactory(new PropertyValueFactory<User, String>("Username"));

    firstName = new TableColumn("First");
    firstName.setMinWidth(100);
    firstName.setCellValueFactory(new PropertyValueFactory<User, String>("First"));

    lastName = new TableColumn("Last");
    lastName.setMinWidth(100);
    lastName.setCellValueFactory(new PropertyValueFactory<User, String>("Last"));

    userTable.getColumns().addAll(userName, firstName, lastName);
    userTable.setItems(userData);
  }

  /** Add current users in DB from the auth manager. */
  private void addUsersToTable() {
    users = controller.getUsers();

    for (int i = 0; i < users.length; i++) {
      String currentUser = users[i];
      User newUser = new User(currentUser, "Test", "Test");
      userData.add(newUser);
    }
  }

  private void addButtonActions() {
    backButton = new Button("Back");

    /** Go back to admin console */
    backButton.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent e) {
            controller.deviceAdminGUI();
          }
        });
  }
}
