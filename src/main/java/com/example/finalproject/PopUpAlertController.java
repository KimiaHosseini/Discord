package com.example.finalproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Popup;

public class PopUpAlertController {

    private static FriendsController origin;

    public static void setOrigin(FriendsController origin) {
        PopUpAlertController.origin = origin;
    }

    @FXML
    void okayButtonPressed(ActionEvent event) {
        Popup popup = (Popup) ((Node) event.getSource()).getScene().getWindow();
        popup.hide();
        origin.setEffect();
    }

}
