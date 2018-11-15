package ca.uvic.seng330.assn3.admin;

import ca.uvic.seng330.assn3.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.util.Arrays;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;

public class ManageUsers {
  private GridPane view;
  private Text title;
  private TableView<User> userTable;
  private ObservableList<User> data;
  private TableColumn userName;
  private TableColumn firstName;
  private TableColumn lastName;
  private TableColumn userRole;
  private TableColumn userDevices;
  private Button addUser;
  private Button removeUser;

  /** Default constructor for the Manage Users view. */
  public ManageUsers() {
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
    title = new Text("Registered Users:");
    title.setFont(new Font(20));

    userTable = new TableView<User>();
    // be able to edit user info in table.
    userTable.setEditable(true);
    //Create observable list of users
    data = FXCollections.observableArrayList();

    // add columns to table for user DB
    userName = new TableColumn("Username");
    userName.setMinWidth(100);
    userName.setCellValueFactory(new PropertyValueFactory<User, String>("Username"));
    firstName = new TableColumn("First");
    firstName.setMinWidth(100);
    firstName.setCellValueFactory(new PropertyValueFactory<User, String>("First"));
    lastName = new TableColumn("Last");
    lastName.setMinWidth(100);
    lastName.setCellValueFactory(new PropertyValueFactory<User, String>("Last"));
    userRole = new TableColumn("Role");
    userRole.setMinWidth(100);
    userRole.setCellValueFactory(new PropertyValueFactory<User, String>("Role"));
    userDevices = new TableColumn("Devices");

    data.add(new User("Admin", "Admin", "Admin", "Admin"));
    userTable.setItems(data);

    userTable
        .getColumns()
        .addAll(userName, firstName, lastName, userRole, userDevices);

    addUser = new Button("Add User");
    removeUser = new Button("Remove User");

    view.addRow(0, title);
    view.addRow(1, userTable);
    view.add(addUser, 1, 2);
    view.add(removeUser, 2, 2);
  }

  private void updateControllerFromListeners() {}

  private void observeModelAndUpdateControls() {}
}
