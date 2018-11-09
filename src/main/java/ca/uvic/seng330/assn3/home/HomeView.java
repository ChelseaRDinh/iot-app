package ca.uvic.seng330.assn3.home;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class HomeView {
  private GridPane view;
  private HomeController controller;
  private HomeModel model;
  private Text title;

  /**
   * Constructor for Home view.
   *
   * @param controller the view's controller
   * @param model the view's model
   */
  public HomeView(HomeController controller, HomeModel model) {
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
    rightCol.setHgrow(Priority.NEVER);

    view.getColumnConstraints().addAll(leftCol, rightCol);

    view.setAlignment(Pos.CENTER);
    view.setHgap(5);
    view.setVgap(10);
  }

  private void createAndLayoutControls() {

    title = new Text("Device Management System");
    title.setFont(new Font(20));

    // making buttons circular-shaped.
    final double r = 50;

    Button cameraButton = new Button("Camera");
    cameraButton.setShape(new Circle(r));
    cameraButton.setMinSize(2 * r, 2 * r);
    cameraButton.setMaxSize(2 * r, 2 * r);

    Button thermostatButton = new Button("Thermostat");
    thermostatButton.setShape(new Circle(r));
    thermostatButton.setMinSize(2 * r, 2 * r);
    thermostatButton.setMaxSize(2 * r, 2 * r);

    Button lightbulbButton = new Button("Lightbulb");
    lightbulbButton.setShape(new Circle(r));
    lightbulbButton.setMinSize(2 * r, 2 * r);
    lightbulbButton.setMaxSize(2 * r, 2 * r);

    Button smartplugButton = new Button("SmartPlug");
    smartplugButton.setShape(new Circle(r));
    smartplugButton.setMinSize(2 * r, 2 * r);
    smartplugButton.setMaxSize(2 * r, 2 * r);

    Button logoutButton = new Button("Logout");

    view.add(title, 1, 0);
    view.add(cameraButton, 1, 1);
    view.add(thermostatButton, 2, 1);
    view.add(lightbulbButton, 1, 2);
    view.add(smartplugButton, 2, 2);
    view.add(logoutButton, 1, 3);

    /*
     * Actions for the buttons can be done here.
     * Depending on which device button is clicked will determine which
     * view will be displayed.
     */
    logoutButton.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent e) {
            controller.logout();
          }
        });
  }
}
