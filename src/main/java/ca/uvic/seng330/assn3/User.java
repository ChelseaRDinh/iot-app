package ca.uvic.seng330.assn3;

import javafx.beans.property.SimpleStringProperty;

public class User {
  private SimpleStringProperty userName;
  private SimpleStringProperty firstName;
  private SimpleStringProperty lastName;
  private SimpleStringProperty role;

  public User(String userName, String firstName, String lastName, String role) {
    this.userName = new SimpleStringProperty(userName);
    this.firstName = new SimpleStringProperty(firstName);
    this.lastName = new SimpleStringProperty(lastName);
    this.role = new SimpleStringProperty(role);
  }

  public String getUserName() {
    return userName.get();
  }

  public String getFirstName() {
    return firstName.get();
  }

  public String getLastName() {
    return lastName.get();
  }

  public String getRole() {
    return role.get();
  }

  public void setUserName(String s) {
    userName.set(s);
  }

  public void setFirstName(String s) {
    firstName.set(s);
  }

  public void setLastName(String s) {
    lastName.set(s);
  }

  public void setRole(String s) {
    role.set(s);
  }
}
