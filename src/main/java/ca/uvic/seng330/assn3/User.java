package ca.uvic.seng330.assn3;

import javafx.beans.property.SimpleStringProperty;

public class User {
  private SimpleStringProperty userName;
  private SimpleStringProperty firstName;
  private SimpleStringProperty lastName;

  /**
   * The constructor for a user class.
   *
   * @param userName the user's name
   * @param firstName the user's first name
   * @param lastName the user's last name
   */
  public User(String userName, String firstName, String lastName) {
    this.userName = new SimpleStringProperty(userName);
    this.firstName = new SimpleStringProperty(firstName);
    this.lastName = new SimpleStringProperty(lastName);
  }

  /**
   * Gets the user's username.
   *
   * @return the user's username
   */
  public String getUserName() {
    return userName.get();
  }

  /**
   * Gets the user's first name.
   *
   * @return the user's first name
   */
  public String getFirstName() {
    return firstName.get();
  }

  /**
   * Gets the user's last name.
   *
   * @return the user's last name
   */
  public String getLastName() {
    return lastName.get();
  }

  /**
   * Sets the user's username.
   *
   * @param s the username to set
   */
  public void setUserName(String s) {
    userName.set(s);
  }

  /**
   * Sets the user's first name.
   *
   * @param s the user's first name to set
   */
  public void setFirstName(String s) {
    firstName.set(s);
  }

  /**
   * Sets the user's last name.
   *
   * @param s the user's last name to set
   */
  public void setLastName(String s) {
    lastName.set(s);
  }
}
