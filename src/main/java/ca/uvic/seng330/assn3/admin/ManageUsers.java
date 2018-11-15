package ca.uvic.seng330.assn3.admin;

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

public class ManageUsers {
  private GridPane view;
  private Text title;
  private TableView<String> userTable;
  private TableColumn<String, String> userName;
  private TableColumn<String, String> firstName;
  private TableColumn<String, String> lastName;
  private TableColumn<String, String> userRole;
  private TableColumn<String, String> userDevices;
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

    userTable = new TableView<String>();
    // be able to edit user info in table.
    userTable.setEditable(true);

    // add colmuns to table for user DB
    userName = new TableColumn<String, String>("Username");
    firstName = new TableColumn<String, String>("First");
    lastName = new TableColumn<String, String>("Last");
    userRole = new TableColumn<String, String>("Role");
    userDevices = new TableColumn<String, String>("Devices");

    userTable
        .getColumns()
        .addAll(Arrays.asList(userName, firstName, lastName, userRole, userDevices));

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
