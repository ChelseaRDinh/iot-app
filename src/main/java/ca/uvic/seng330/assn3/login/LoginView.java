package ca.uvic.seng330.assn3.login;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/*
 * Code sample from https://stackoverflow.com/questions/36868391/using-javafx-controller-without-fxml/36873768
 */
public class LoginView {
  private GridPane view;
  private Label title;
  private Label userName;
  private Label passPhrase;
  private TextField passPhraseField;
  private Button b;

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
    //This was previously set to 'SOMETIMES', so the text field used to get cut off.
    rightCol.setHgrow(Priority.NEVER);
    view.getColumnConstraints().addAll(leftCol, rightCol);
    view.setAlignment(Pos.CENTER);
    view.setHgap(10);
    view.setVgap(10);
  }

  private void createAndLayoutControls() {
    // Testing with adding labels.
    userName = new Label("User Name: ");
    view.add(userName, 0, 1);

    TextField userTextField = new TextField();
    view.add(userTextField, 1, 1);

    passPhrase = new Label("Passphrase: ");
    view.add(passPhrase, 0, 2);

    passPhraseField = new TextField();
    view.add(passPhraseField, 1, 2);

    b = new Button("Login");
    view.addRow(3, new Label(""), b);

    title = new Label("Welcome!");
    view.add(title, 1, 0);
  }
}
