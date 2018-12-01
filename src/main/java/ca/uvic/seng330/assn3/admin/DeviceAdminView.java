package ca.uvic.seng330.assn3.admin;

import ca.uvic.seng330.assn3.DeviceItem;
import java.util.*;
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

public class DeviceAdminView {
  private DeviceAdminController controller;
  private DeviceAdminModel model;

  private HashMap<String, String> displayNameToType;
  private HashMap<String, String> displayUsername;
  private ObservableList<DeviceItem> deviceData;
  private ObservableList<String> deviceTypeOptions;
  private ObservableList<String> deviceOwnerOptions;

  private GridPane mainView;
  private GridPane topView;
  private GridPane devicesView;
  private GridPane bottomView;
  private Text title;
  private Text otherTitle; // Can't just add title to two different views
  private Text loadingText;
  private TableView<DeviceItem> deviceTable;
  private Button backButton;
  private Button otherBackButton;
  private Button setOwnerButton;
  private Button removeDevice;
  private Button confirmButton;
  private TextField deviceNameField;
  private TextField deviceTypeField;
  private ComboBox deviceTypeBox;
  private ComboBox deviceOwnerBox;

  /** Default constructor for the Manage Devices view. */
  public DeviceAdminView(DeviceAdminController controller, DeviceAdminModel model) {
    this.controller = controller;
    this.model = model;

    // Initialize the map of device display names to class names.
    displayNameToType = new HashMap<String, String>();
    List<String> addableClasses = model.getAddableClasses();
    for (String className : addableClasses) {
      int lastDot = className.lastIndexOf('.');
      displayNameToType.put(className.substring(lastDot + 1, className.length()), className);
    }

    displayUsername = new HashMap<String, String>();
    Set<String> addableUsernames = model.getAllUsernames();
    for (String userName : addableUsernames) {
      if (!userName.equals("admin")) {
        displayUsername.put(userName, userName);
      }
    }

    deviceTypeOptions = FXCollections.observableArrayList(displayNameToType.keySet());
    deviceOwnerOptions = FXCollections.observableArrayList(displayUsername.keySet());

    createAndConfigurePane();
    createAndLayoutControls();
    observeModelAndUpdateControls();
  }

  public Parent asParent() {
    return mainView;
  }

  private void createAndConfigurePane() {
    mainView = new GridPane();
    topView = new GridPane();
    devicesView = new GridPane();
    bottomView = new GridPane();

    ColumnConstraints leftCol = new ColumnConstraints();
    leftCol.setHalignment(HPos.LEFT);
    leftCol.setHgrow(Priority.NEVER);

    ColumnConstraints middleColMain = new ColumnConstraints();
    middleColMain.setHalignment(HPos.CENTER);
    middleColMain.setHgrow(Priority.NEVER);

    ColumnConstraints middleColTop = new ColumnConstraints();
    middleColTop.setHalignment(HPos.CENTER);
    middleColTop.setHgrow(Priority.NEVER);

    ColumnConstraints middleColDevices = new ColumnConstraints();
    middleColDevices.setHalignment(HPos.CENTER);
    middleColDevices.setHgrow(Priority.NEVER);

    ColumnConstraints middleCol = new ColumnConstraints();
    middleCol.setHalignment(HPos.CENTER);
    middleCol.setHgrow(Priority.NEVER);

    ColumnConstraints rightCol = new ColumnConstraints();
    rightCol.setHalignment(HPos.RIGHT);
    rightCol.setHgrow(Priority.NEVER);

    mainView.getColumnConstraints().add(middleColMain);
    topView.getColumnConstraints().add(middleColTop);
    devicesView.getColumnConstraints().add(middleColDevices);
    bottomView.getColumnConstraints().addAll(leftCol, middleCol, rightCol);

    mainView.setAlignment(Pos.CENTER);
    topView.setAlignment(Pos.CENTER);
    devicesView.setAlignment(Pos.CENTER);
    bottomView.setAlignment(Pos.CENTER);

    mainView.setHgap(10);
    bottomView.setHgap(10);

    mainView.setVgap(10);
    topView.setVgap(10);
    devicesView.setVgap(10);
    bottomView.setVgap(10);

    mainView.addRow(0, topView);
    mainView.addRow(1, devicesView);
    mainView.addRow(2, bottomView);
  }

  private void createAndLayoutControls() {
    title = new Text("Registered Devices:");
    title.setFont(new Font(20));
    otherTitle = new Text("Registered Devices:");
    otherTitle.setFont(new Font(20));

    loadingText = new Text("Loading device list...");

    createDeviceTable();

    backButton = new Button("Back");
    otherBackButton = new Button("Back");
    setOwnerButton = new Button("Change Selected Device Owner");
    removeDevice = new Button("Remove Selected Device");
    confirmButton = new Button("Confirm Add Device");

    topView.addRow(0, title);
    topView.addRow(1, loadingText);
    topView.addRow(2, backButton);

    devicesView.addRow(0, otherTitle);
    devicesView.addRow(1, deviceTable);

    bottomView.add(confirmButton, 0, 0);
    bottomView.add(setOwnerButton, 1, 0);
    bottomView.add(removeDevice, 2, 0);

    bottomView.add(otherBackButton, 1, 2);

    // Device types for drop-down menu that appears after the button 'Add Device' is selected.
    deviceTypeBox = new ComboBox(deviceTypeOptions);
    deviceTypeBox.setId("deviceTypeBox");

    deviceOwnerBox = new ComboBox(deviceOwnerOptions);
    deviceOwnerBox.setId("deviceOwnerBox");

    bottomView.add(deviceTypeBox, 0, 1);
    bottomView.add(deviceOwnerBox, 1, 1);

    addButtonActions();
    setLoadingControlsHidden(false);
  }

  private void createDeviceTable() {
    deviceTable = new TableView<DeviceItem>();
    // be able to edit device info in table.
    deviceTable.setEditable(true);
    // Create observable list of devices
    deviceData = FXCollections.observableArrayList();

    // add columns to table for device DB
    TableColumn deviceType = new TableColumn("Type");
    deviceType.setMinWidth(260);
    deviceType.setCellValueFactory(new PropertyValueFactory<DeviceItem, String>("Type"));

    TableColumn deviceUUID = new TableColumn("UUID");
    deviceUUID.setMinWidth(200);
    deviceUUID.setCellValueFactory(new PropertyValueFactory<DeviceItem, String>("UUID"));

    TableColumn deviceStatus = new TableColumn("Status");
    deviceStatus.setMinWidth(80);
    deviceStatus.setCellValueFactory(new PropertyValueFactory<DeviceItem, String>("Status"));

    TableColumn deviceOwner = new TableColumn("Owner");
    deviceOwner.setMinWidth(100);
    deviceOwner.setCellValueFactory(new PropertyValueFactory<DeviceItem, String>("Owner"));

    deviceTable.getColumns().addAll(deviceType, deviceUUID, deviceStatus, deviceOwner);
    deviceTable.setItems(deviceData);
  }

  private void addButtonActions() {

    confirmButton.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent e) {
            DeviceItem newDevice =
                controller.addNewDevice(displayNameToType.get(deviceTypeBox.getValue().toString()));

            if (newDevice != null) {
              deviceData.add(newDevice);
            } else {
              /*
               * Add error msg here that the device cannot be added to the DB,
               * Because no device type was specified or some other error.
               */
            }
          }
        });

    // Deregister selected device upon selection.
    removeDevice.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent e) {
            // De-register device from admin hub before removing from table.
            DeviceItem selectedItem = deviceTable.getSelectionModel().getSelectedItem();

            boolean result = controller.removeByUUID(UUID.fromString(selectedItem.getUUID()));
            if (result) {
              deviceTable.getItems().remove(selectedItem);
            } else {
              // Show error message.
            }
          }
        });

    /*setOwnerButton.setOnAction(
    new EventHandler<ActionEvent>() {
    	@Override
    	public void handle(ActionEvent e) {
    		DeviceItem selectedItem = deviceTable.getSelectionModel().getSelectedItem();
    		controller.userDeviceRegGUI();
    	}
    });*/

    setOwnerButton.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent e) {
            /**
             * After selecting device to change its ownership, show combo box of avail users to
             * change ownership to Then update user field of table in place.
             */
            DeviceItem selectedItem = deviceTable.getSelectionModel().getSelectedItem();
            String oldOwner = selectedItem.getOwner();
            String selectedOwner = deviceOwnerBox.getValue().toString();
            String selectedDeviceUUID = selectedItem.getUUID();

            controller.changeDeviceOwner(oldOwner, selectedOwner, selectedDeviceUUID);
            selectedItem.setOwner(selectedOwner);
            /** Refresh table after changing owner so user doesn't have to refresh the page. */
            deviceTable.refresh();
          }
        });

    backButton.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent e) {
            controller.adminGUI();
          }
        });

    otherBackButton.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent e) {
            controller.adminGUI();
          }
        });
  }

  private void addDevicesToTable(Boolean newValue) {
    setLoadingControlsHidden(newValue);

    if (!newValue) {
      return;
    }

    for (int i = 0; i < model.getDeviceCount(); i++) {
      DeviceItem device = model.getDeviceItemAt(i);
      deviceData.add(device);
    }
  }

  private void setLoadingControlsHidden(Boolean value) {
    topView.setVisible(!value);
    topView.setManaged(!value);
    devicesView.setVisible(value);
    devicesView.setManaged(value);
    bottomView.setVisible(value);
    bottomView.setManaged(value);
  }

  private void observeModelAndUpdateControls() {
    model.isReadyProperty().addListener((obs, oldValue, newValue) -> addDevicesToTable(newValue));
  }
}
