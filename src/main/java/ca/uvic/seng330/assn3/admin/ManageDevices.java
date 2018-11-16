package ca.uvic.seng330.assn3.admin;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.util.Arrays;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ManageDevices {
  private GridPane view;
  private Text title;
  private TableView deviceTable;
  private TableColumn userName;
  private TableColumn device;
  private Button addDevice;
  private Button removeDevice;

  /** Default constructor for the Manage Devices view. */
  public ManageDevices() {
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

    deviceTable
        .getColumns()
        .addAll(userName, device);

    addDevice = new Button("Add Device");
    removeDevice = new Button("Remove Device");

    view.addRow(0, title);
    view.addRow(1, deviceTable);
    view.add(addDevice, 1, 2);
    view.add(removeDevice, 2, 2);
  }

  private void updateControllerFromListeners() {}

  private void observeModelAndUpdateControls() {}
}
