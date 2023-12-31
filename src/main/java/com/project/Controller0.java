package com.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class Controller0 {

    @FXML
    private Button button1, button2;
    @FXML
    private AnchorPane container;

    // Mètode per canviar la vista actual a la vista 1

    @FXML
    private void animateToView1(ActionEvent event) {
        UtilsViews.setViewAnimating("View1");
    }
    
    // Mètode per canviar la vista actual a la vista 2

    @FXML
    private void animateToView2(ActionEvent event) {
        UtilsViews.setViewAnimating("View2");
    }
}