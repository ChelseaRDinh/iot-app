package ca.uvic.seng330.assn3.admin;

import ca.uvic.seng330.assn3.DeviceItem;
import ca.uvic.seng330.assn3.Token;
import ca.uvic.seng330.assn3.devices.Camera;
import ca.uvic.seng330.assn3.devices.Hub;
import ca.uvic.seng330.assn3.devices.HubRegistrationException;
import ca.uvic.seng330.assn3.devices.Lightbulb;
import ca.uvic.seng330.assn3.devices.MasterHub;
import ca.uvic.seng330.assn3.devices.SmartPlug;
import ca.uvic.seng330.assn3.devices.Thermostat;
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

public class ManageDevices {
  private GridPane view;
  private Text title;
  private TableView<DeviceItem> deviceTable;
  private ObservableList<DeviceItem> deviceData;
  private ObservableList<String> deviceTypeOptions;
  private TableColumn deviceOwner;
  private TableColumn deviceType;
  private TableColumn deviceUUID;
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

    deviceTable = new TableView<DeviceItem>();
    // be able to edit device info in table.
    deviceTable.setEditable(true);
    // Create observable list of devices
    deviceData = FXCollections.observableArrayList();

    // add columns to table for device DB
    deviceType = new TableColumn("Device");
    deviceType.setMinWidth(100);
    deviceType.setCellValueFactory(new PropertyValueFactory<DeviceItem, String>("Type"));
    deviceUUID = new TableColumn("UUID");
    deviceUUID.setMinWidth(200);
    deviceUUID.setCellValueFactory(new PropertyValueFactory<DeviceItem, String>("UUID"));
    deviceOwner = new TableColumn("Owner");
    deviceOwner.setMinWidth(100);
    deviceOwner.setCellValueFactory(new PropertyValueFactory<DeviceItem, String>("Owner"));

    deviceTable.getColumns().addAll(deviceType, deviceUUID, deviceOwner);
    deviceTable.setItems(deviceData);

    addDevice = new Button("Add Device");
    removeDevice = new Button("Remove Device");
    confirmButton = new Button("Confirm");

    deviceOwnerField = new TextField();
    deviceOwnerField.setPromptText("Device Owner Username");

    view.addRow(0, title);
    view.addRow(1, deviceTable);
    view.add(addDevice, 1, 2);
    view.add(removeDevice, 2, 2);

    /*Device types for drop-down menu
     * That appears after the button 'Add Device' is selected.
     */
    deviceTypeOptions =
        FXCollections.observableArrayList("Camera", "Lightbulb", "SmartPlug", "Thermostat");
    ComboBox deviceTypeBox = new ComboBox(deviceTypeOptions);

    // When the add user button is selected
    // Show fields to add device information
    addDevice.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent e) {
            view.add(deviceTypeBox, 2, 4);
            view.add(deviceOwnerField, 2, 5);
            view.add(confirmButton, 2, 6);
          }
        });

    confirmButton.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent e) {
            if (deviceTypeBox.getValue().toString() == "Camera") {
              Camera c = new Camera(adminHub);
              deviceData.add(
                  new DeviceItem(
                      "Camera", String.valueOf(c.getIdentifier()), deviceOwnerField.getText()));
              try {
                adminHub.register(c);
              } catch (HubRegistrationException h) {
                // do nothing
              }
            } else if (deviceTypeBox.getValue().toString() == "Lightbulb") {
              Lightbulb l = new Lightbulb(adminHub);
              try {
                adminHub.register(l);
                deviceData.add(
                    new DeviceItem(
                        "Lightbulb",
                        String.valueOf(l.getIdentifier()),
                        deviceOwnerField.getText()));
              } catch (HubRegistrationException h) {
                // do nothing
              }
            } else if (deviceTypeBox.getValue().toString() == "SmartPlug") {
              SmartPlug s = new SmartPlug(adminHub);
              try {
                adminHub.register(s);
                deviceData.add(
                    new DeviceItem(
                        "Smartplug",
                        String.valueOf(s.getIdentifier()),
                        deviceOwnerField.getText()));
              } catch (HubRegistrationException h) {
                // do nothing
              }
            } else if (deviceTypeBox.getValue().toString() == "Thermostat") {
              Thermostat t = new Thermostat(adminHub);
              try {
                adminHub.register(t);
                deviceData.add(
                    new DeviceItem(
                        "Thermostat",
                        String.valueOf(t.getIdentifier()),
                        deviceOwnerField.getText()));
              } catch (HubRegistrationException h) {
              }
            } else {
              /*
               * Add error msg here that the device cannot be added to the DB,
               * Because no device type was specified.
               */
            }
          }
        });
  }

  private void updateControllerFromListeners() {}

  private void observeModelAndUpdateControls() {}
}
