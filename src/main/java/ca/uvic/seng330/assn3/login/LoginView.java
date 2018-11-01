package ca.uvic.seng330.assn3.login;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/*
 * Code sample from https://stackoverflow.com/questions/36868391/using-javafx-controller-without-fxml/36873768
 */
public class LoginView {
  private GridPane view;
  private Label username;

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

    view = new GridPane();
    // Testing with adding labels.
    Label userName = new Label("User Name: ");
    view.add(userName, 0, 1);

    TextField userTextField = new TextField();
    view.add(userTextField, 1, 1);

    Label passPhrase = new Label("Passphrase: ");
    view.add(passPhrase, 0, 2);

    TextField passPhraseField = new TextField();
    view.add(passPhraseField, 1, 2);

    Button b = new Button("Login");
    view.addRow(3, new Label(""), b);
  }

  public Parent asParent() {
    return view;
  }
}
