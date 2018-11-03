package ca.uvic.seng330.assn3.home;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class HomeView {
    private GridPane view;
    private HomeController controller;
    private HomeModel model;

    public HomeView(HomeController controller, HomeModel model) {
        this.controller = controller;
        this.model = model;
    }
}