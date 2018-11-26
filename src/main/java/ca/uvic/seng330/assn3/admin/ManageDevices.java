package ca.uvic.seng330.assn3.admin;

import ca.uvic.seng330.assn3.Token;
import ca.uvic.seng330.assn3.devices.MasterHub;
import ca.uvic.seng330.assn3.devices.Hub;
import ca.uvic.seng330.assn3.devices.Camera;
import ca.uvic.seng330.assn3.devices.Lightbulb;
import ca.uvic.seng330.assn3.devices.SmartPlug;
import ca.uvic.seng330.assn3.devices.Thermostat;
import ca.uvic.seng330.assn3.devices.Device;
import ca.uvic.seng330.assn3.devices.HubRegistrationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.Label;

public class ManageDevices {
  private GridPane view;
  private Text title;
  private TableView deviceTable;
  private ObservableList<Device> deviceData;
  private ObservableList<String> deviceTypeOptions;
  private TableColumn userName;
  private TableColumn device;
  private Button addDevice;
  private Button removeDevice;
  private Button confirmButton;
  private TextField deviceNameField;
  private TextField deviceTypeField;
  private TextField deviceOwnerField;
  private AdminController controller;
  private AdminModel model;
  private MasterHub h;
  private Token token;
  private Hub adminHub;

  /** Default constructor for the Manage Devices view. */
  public ManageDevices(AdminController controller, AdminModel model) {
	  this.controller = controller;
	  this.model = model;
	  h = model.getMasterHub();
	  token = model.getUserToken();
	  adminHub = h.getHubForUser(token);
    createAndConfigurePane();
    createAndLayoutControls();
    updateControllerFromListeners();
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
    title = new Text("Registered Devices:");
	title.setFont(new Font(20));

    deviceTable = new TableView();
    // be able to edit device info in table.
    deviceTable.setEditable(true);

    // add columns to table for device DB
    userName = new TableColumn("Username");
    device = new TableColumn("Device");

    deviceTable.getColumns().addAll(userName, device);
    deviceTable.setItems(deviceData);

    addDevice = new Button("Add Device");
    removeDevice = new Button("Remove Device");
    confirmButton = new Button("Confirm");

    /** Text fields for adding a user. */
    deviceNameField = new TextField();
    deviceNameField.setPromptText("Device Name");

    //Change this to drop-down menu? Makes more sense.
    //Then the user has a choice of what devices they are allowed to register
    deviceTypeField = new TextField();
    deviceTypeField.setPromptText("Device Type");

    deviceOwnerField = new TextField();
    deviceOwnerField.setPromptText("Device Owner Username");

    view.addRow(0, title);
    view.addRow(1, deviceTable);
    view.add(addDevice, 1, 2);
	view.add(removeDevice, 2, 2);
	
	/*Device types for drop-down menu
	* That appears after the button 'Add Device' is selected.
	*/
	deviceTypeOptions = FXCollections.observableArrayList("Camera","Lightbulb","SmartPlug","Thermostat");
	ComboBox deviceTypeBox = new ComboBox(deviceTypeOptions);

    //When the add user button is selected
    //Show fields to add device information
    addDevice.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent e) {
            //view.add(deviceNameField, 2,3);
			view.add(deviceTypeBox, 2, 4);
            view.add(deviceOwnerField, 2, 5);
            view.add(confirmButton, 2, 6);
          }
		});
      
      confirmButton.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent e) {
			  if(deviceTypeBox.getValue().toString() == "Camera") {
				Camera c = new Camera(adminHub);
				try {
					adminHub.register(c);
				} catch (HubRegistrationException h) {
					//do nothing
				}
			  } else if(deviceTypeBox.getValue().toString() == "Lightbulb") {
				Lightbulb l = new Lightbulb(adminHub);
				try {
					adminHub.register(l);
				} catch (HubRegistrationException h) {
					//do nothing
				}
			  } else if(deviceTypeBox.getValue().toString() == "SmartPlug") {
				SmartPlug s = new SmartPlug(adminHub);
				try {
					adminHub.register(s);
				} catch (HubRegistrationException h) {
					//do nothing
				}
			  } else if(deviceTypeBox.getValue().toString() == "Thermostat") {
				  Thermostat t = new Thermostat(adminHub);
				  try {
					  adminHub.register(t);
				  } catch (HubRegistrationException h) {
					  /*
					  * Add error msg here that the device cannot be added to the DB,
					  * Because no device type was specified.
					  */
				  }
			  } else {
				  //Do nothing, because no device type was selected.
			  }
          }
		});
  }

  private void updateControllerFromListeners() {}

  private void observeModelAndUpdateControls() {}
}
