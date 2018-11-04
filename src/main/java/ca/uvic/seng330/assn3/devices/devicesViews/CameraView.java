package ca.uvic.seng330.assn3.devices.devicesViews;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class CameraView {
    private GridPane view;
    private Label title;
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

    private void createAndLayoutControls() {}

    private void updateControllerFromListeners() {}

    private void observeModelAndUpdateControls() {}
}