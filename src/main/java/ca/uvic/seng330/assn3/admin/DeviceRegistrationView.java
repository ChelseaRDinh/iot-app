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
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.*;
import ca.uvic.seng330.assn3.admin.DeviceRegistrationView;
import ca.uvic.seng330.assn3.admin.DeviceRegistrationModel;
import ca.uvic.seng330.assn3.admin.DeviceRegistrationController;

public class DeviceRegistrationView {
  private DeviceRegistrationController controller;
  private DeviceRegistrationModel model;

  private GridPane view;
  private Text title;
  private CheckBox userCheckBox;
  private HashMap<String, String> userNames;
  private Button confirmButton;

  /** Default constructor for the Manage Devices view. */
  public DeviceRegistrationView(DeviceRegistrationController controller, DeviceRegistrationModel model) {
    this.controller = controller;
	this.model = model;

    createAndConfigurePane();
    createAndLayoutControls();
    observeModelAndUpdateControls();
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
    view.setHgap(10);
    view.setVgap(10);
  }

  private void createAndLayoutControls() {
    title = new Text("Select Owner(s):");
	title.setFont(new Font(20));
	view.addRow(0, title);

	confirmButton = new Button("Confirm");
	createUserCheckBoxes();

	confirmButton.setOnAction(
		new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Stage stage = (Stage) confirmButton.getScene().getWindow();
				stage.close();
			}
		});
  }

  private void observeModelAndUpdateControls() {
  }

  private void createUserCheckBoxes() {
	userNames = new HashMap<String, String>();
	Set<String> addableUsernames = model.getAllUsernames();
	int i = 1;
	for(String userName : addableUsernames) {
		if(!userName.equals("admin")) {
			userCheckBox = new CheckBox(userName);
			view.add(userCheckBox,0,i);
		}
		i++;
	}
	view.addRow(i+1, confirmButton);
  }


}
