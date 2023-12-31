package com.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Controller6 {

    @FXML
    private Button button0;

    // Mètode per canviar la vista actual a la vista 0

    @FXML
    private void animateToView0(ActionEvent event) {
        UtilsViews.setViewAnimating("View0");
    }
}
