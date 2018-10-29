package ca.uvic.seng330.assn3.login;

import javafx.scene.Parent;
import javafx.scene.layout.GridPane;

/*
 * Code sample from https://stackoverflow.com/questions/36868391/using-javafx-controller-without-fxml/36873768
 */
public class LoginView {
  private GridPane view;

  private LoginController controller;
  private LoginModel model;

  public LoginView(LoginController controller, LoginModel model) {
    this.controller = controller;
    this.model = model;

    view = new GridPane();
  }

  public Parent asParent() {
    return view;
  }
}
