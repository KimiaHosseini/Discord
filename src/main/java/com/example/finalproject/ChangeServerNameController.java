package com.example.finalproject;

import Client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Popup;

import static com.example.finalproject.ServerController.serverIndex;
import static com.example.finalproject.welcome.SignInController.client;

public class ChangeServerNameController {

    @FXML
    private TextField serverNameTf;

    private static MainController origin;

    public static void setOrigin(MainController origin) {
        ChangeServerNameController.origin = origin;
    }

    @FXML
    void cancelButtonPressed(ActionEvent event) {
        javafx.stage.Popup popup = (Popup) ((Node) event.getSource()).getScene().getWindow();
        popup.hide();
        origin.setEffect();
    }

    @FXML
    void doneButtonPressed(ActionEvent event) {
        client.changeServerName(serverNameTf.getText(),serverIndex);
        cancelButtonPressed(event);
        origin.update();
    }

}
