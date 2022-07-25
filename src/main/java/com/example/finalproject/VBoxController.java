package com.example.finalproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Popup;

public class VBoxController {
    private static MainController origin;

    public static void setOrigin(MainController origin) {
        VBoxController.origin = origin;
    }

    @FXML
    void okButtonPressed(ActionEvent event) {
        Popup popup = (Popup) ((Node) event.getSource()).getScene().getWindow();
        popup.hide();
        origin.setEffect();
    }
}
