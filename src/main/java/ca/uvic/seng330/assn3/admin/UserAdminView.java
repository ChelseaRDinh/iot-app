package ca.uvic.seng330.assn3.admin;

import ca.uvic.seng330.assn3.User;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class UserAdminView {
  private UserAdminController controller;
  private UserAdminModel model;

  private GridPane view;
  private Text title;
  private TableView<User> userTable;
  private ObservableList<User> userData;
  private TableColumn userName;
  private TableColumn firstName;
  private TableColumn lastName;
  private TableColumn userRole;
  private TableColumn userDevices;
  private Button addUser;
  private Button removeUser;
  private Button backButton;
  private TextField userNameField;
  private TextField firstNameField;
  private TextField lastNameField;
  private TextField roleField;

  /** Default constructor for the User Admin view. */
  public UserAdminView(UserAdminController controller, UserAdminModel model) {
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
    title = new Text("Registered Users:");
    title.setFont(new Font(20));

    userTable = new TableView<User>();
    // be able to edit user info in table.
    userTable.setEditable(true);
    // Create observable list of users
    userData = FXCollections.observableArrayList();

    // add columns to table for user DB
    userName = new TableColumn("Username");
    userName.setMinWidth(100);
    userName.setCellValueFactory(new PropertyValueFactory<User, String>("userName"));
    firstName = new TableColumn("First");
    firstName.setMinWidth(100);
    firstName.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
    lastName = new TableColumn("Last");
    lastName.setMinWidth(100);
    lastName.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
    userRole = new TableColumn("Role");
    userRole.setMinWidth(100);
    userRole.setCellValueFactory(new PropertyValueFactory<User, String>("role"));

    // test adding users
    User adminUser = new User("admin", "John", "Smith", "Admin");
    User basicUser = new User("user", "Nancy", "Walsh", "Basic User");
    userData.add(adminUser);
    userData.add(basicUser);
    userTable.setItems(userData);

    userTable.getColumns().addAll(userName, firstName, lastName, userRole);

    addUser = new Button("Add User");
	removeUser = new Button("Remove User");
	backButton = new Button("Back");

    /** Text fields for adding a user. */
    userNameField = new TextField();
    userNameField.setPromptText("Username");

    firstNameField = new TextField();
    firstNameField.setPromptText("First");

    lastNameField = new TextField();
    lastNameField.setPromptText("Last");

    roleField = new TextField();
    roleField.setPromptText("Role");

    // add user based on inputting username, first, last, and role params.
    addUser.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent e) {
            userData.add(
                new User(
                    userNameField.getText(),
                    firstNameField.getText(),
                    lastNameField.getText(),
                    roleField.getText()));
          }
        });

    // remove user based on selected row.
    removeUser.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent e) {
            User selectedItem = userTable.getSelectionModel().getSelectedItem();
            userTable.getItems().remove(selectedItem);
          }
		});
	
	/**Go back to admin console */
	backButton.setOnAction(
		new EventHandler<ActionEvent>() {
		  @Override
		  public void handle(ActionEvent e) {
			controller.adminGUI();
		  }
		}
	);

    view.addRow(0, title);
    view.addRow(1, userTable);
    view.addRow(2, userNameField);
    view.addRow(3, firstNameField);
    view.addRow(4, lastNameField);
    view.addRow(5, roleField);
    view.add(addUser, 5, 5);
	view.add(removeUser, 5, 6);
	view.add(backButton, 5, 7);
  }

  private void updateControllerFromListeners() {}

  private void observeModelAndUpdateControls() {}
}
