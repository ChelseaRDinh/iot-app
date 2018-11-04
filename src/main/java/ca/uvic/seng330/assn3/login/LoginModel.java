package ca.uvic.seng330.assn3.login;

/*
 * Code sample from https://stackoverflow.com/questions/36868391/using-javafx-controller-without-fxml/36873768
 */

import javafx.beans.property.SimpleStringProperty;

public class LoginModel {
  private final SimpleStringProperty username = new SimpleStringProperty();
  private final SimpleStringProperty password = new SimpleStringProperty();

  public LoginModel() {}

  public final SimpleStringProperty usernameProperty() {
    return this.username;
  }

  public final SimpleStringProperty passwordProperty() {
    return this.password;
  }

  public final String getUsername() {
    return this.usernameProperty().get();
  }

  public final void setUsername(final String username) {
    this.usernameProperty().set(username);
  }

  public final String getPassword() {
    return this.passwordProperty().get();
  }

  public final void setPassword(final String password) {
    this.passwordProperty().set(password);
  }
}
