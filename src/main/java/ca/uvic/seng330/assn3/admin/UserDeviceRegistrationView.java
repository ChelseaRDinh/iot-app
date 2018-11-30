package ca.uvic.seng330.assn3.admin;

import ca.uvic.seng330.assn3.DeviceItem;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class UserDeviceRegistrationView {
  private UserDeviceRegistrationController controller;
  private UserDeviceRegistrationModel model;
  private GridPane view;
  private Text title;
  private Button backButton;

  public UserDeviceRegistrationView(
      UserDeviceRegistrationController controller, UserDeviceRegistrationModel model) {
		  this.controller = controller;
		  this.model = model;
	   
	  createAndConfigurePane();
      createAndLayoutControls();
      observeModelAndUpdateControls();
	  }
  
  public Parent asParent() {
	  return view;
  }

  public void createAndConfigurePane() {
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

  public void createAndLayoutControls() {
	title = new Text("Currently Registered Devices for Users");

	backButton = new Button("Back");

	/** Go back to admin console */
    backButton.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent e) {
            controller.deviceAdminGUI();
          }
        });

	view.addRow(0, title);
	view.addRow(1, backButton);
  }

  private void observeModelAndUpdateControls() {
  }
}
