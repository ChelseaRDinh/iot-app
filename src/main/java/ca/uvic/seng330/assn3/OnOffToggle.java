package ca.uvic.seng330.assn3;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

public class OnOffToggle {
  private HBox container;
  private ToggleGroup group;
  private ToggleButton on;
  private ToggleButton off;

  /** Default constructor for the on off toggle button. */
  public OnOffToggle() {
    group = new ToggleGroup();

    // Lightbulb toggle.
    on = new ToggleButton("ON");
    on.setStyle("-fx-base: grey;");
    on.setToggleGroup(group);

    off = new ToggleButton("OFF");
    off.setStyle("-fx-base: grey;");
    off.setToggleGroup(group);

    on.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent e) {
            on.setStyle("-fx-base: green;");
            off.setStyle("-fx-base: grey;");
          }
        });

    off.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent e) {
            on.setStyle("-fx-base: grey;");
            off.setStyle("-fx-base: red;");
          }
        });

    container = new HBox(on, off);
  }

  public HBox getContainer() {
    return container;
  }
}
