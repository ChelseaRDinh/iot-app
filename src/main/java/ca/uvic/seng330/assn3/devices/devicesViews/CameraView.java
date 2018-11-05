package ca.uvic.seng330.assn3.devices.devicesViews;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.HBox;

public class CameraView {
    private GridPane view;
    private Label title;
    private ToggleGroup group;
    private ToggleButton on;
    private ToggleButton off;

    public CameraView() {
    
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
        title = new Label("Camera Settings");
        group = new ToggleGroup();

        //Recording toggle.
        on = new ToggleButton("ON");
        on.setUserData(Color.GREEN);
        on.setToggleGroup(group);

        off = new ToggleButton("OFF");
        off.setUserData(Color.RED);
        off.setToggleGroup(group);

        HBox recordContainer = new HBox(on, off);

        view.addRow(0, title);
        view.addRow(2, new Label("Record:"), recordContainer);

        //Camera Disk Space bar:



    }

    private void updateControllerFromListeners() {}

    private void observeModelAndUpdateControls() {}
}