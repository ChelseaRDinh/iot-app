package ca.uvic.seng330.assn3.login;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/*
 * Code sample from https://stackoverflow.com/questions/36868391/using-javafx-controller-without-fxml/36873768
 */
public class LoginView {
  private GridPane view;
  private Label title;
  private Label username;
  private Label password;
  private TextField usernameField;
  private TextField passwordField;
  private Button loginButton;
  private final Label pwdInvalid = new Label("");

  private LoginController controller;
  private LoginModel model;

  /**
   * Constructor for the login view.
   *
   * @param controller the controller for the login view
   * @param model the model for the login view
   */
  public LoginView(LoginController controller, LoginModel model) {

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

  private void observeModelAndUpdateControls() {
    model
        .usernameProperty()
        .addListener((obs, oldUsername, newUsername) -> updateIfNeeded(newUsername, usernameField));
    model
        .passwordProperty()
        .addListener((obs, oldPassword, newPassword) -> updateIfNeeded(newPassword, passwordField));
  }

  private void updateIfNeeded(String value, TextField field) {
    if (!field.getText().equals(value)) {
      field.setText(value);
    }
  }

  private void updateControllerFromListeners() {
    usernameField
        .textProperty()
        .addListener((obs, oldText, newText) -> controller.updateUsername(newText));
    passwordField
        .textProperty()
        .addListener((obs, oldText, newText) -> controller.updatePassword(newText));
  }

  private void createAndConfigurePane() {
    view = new GridPane();

    ColumnConstraints leftCol = new ColumnConstraints();
    leftCol.setHalignment(HPos.RIGHT);
    leftCol.setHgrow(Priority.NEVER);
    ColumnConstraints rightCol = new ColumnConstraints();
    // This was previously set to 'SOMETIMES', so the text field used to get cut off.
    rightCol.setHgrow(Priority.NEVER);
    view.getColumnConstraints().addAll(leftCol, rightCol);
    view.setAlignment(Pos.CENTER);
    view.setHgap(10);
    view.setVgap(10);
  }

  private void createAndLayoutControls() {
    // Testing with adding labels.
    username = new Label("Username: ");
    view.add(username, 0, 1);

    usernameField = new TextField();
    usernameField.setId("usernameField");
    usernameField.setPromptText("Username");
    view.add(usernameField, 1, 1);

    password = new Label("Password: ");
    view.add(password, 0, 2);

    passwordField = new PasswordField();
    passwordField.setId("passwordField");
    passwordField.setPromptText("Password");
    view.add(passwordField, 1, 2);

    //password validation (hardcoded for now)
    //Should use comparison with user/pwd DB kurt created
    if (!passwordField.getText().equals("wrong")) {
      pwdInvalid.setText("Invalid password.");
      pwdInvalid.setTextFill(Color.rgb(210, 39, 30));
    } else {
    }

    loginButton = new Button("Login");

    loginButton.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent e) {
            controller.login();
          }
        });

    view.addRow(3, new Label(""), loginButton);

    title = new Label("Welcome!");
    title.setFont(new Font(20));
    view.add(title, 1, 0);
  }
}
