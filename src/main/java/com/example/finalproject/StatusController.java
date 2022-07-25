package com.example.finalproject;

import Client.Client;
import UserFeatures.Status;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class StatusController {

    private Popup popup;

    private static Client client;
    private static HomeController origin;

    public static void setOrigin(HomeController origin) {
        StatusController.origin = origin;
    }

    public static void setClient(Client client) {
        StatusController.client = client;
    }
    @FXML
    void dndButtonPressed(ActionEvent event) {
        client.setStatus(3);
        origin.setStatusPhoto();
        close(event);
    }

    @FXML
    void idleButtonPressed(ActionEvent event) {
        client.setStatus(2);
        origin.setStatusPhoto();
        close(event);
    }

    @FXML
    void invisibleButtonPressed(ActionEvent event) {
        client.setStatus(5);
        origin.setStatusPhoto();
        close(event);
    }

    @FXML
    void onlineButtonPressed(ActionEvent event) {
        client.setStatus(1);
        origin.setStatusPhoto();
        close(event);
    }

    private void close(ActionEvent event){
        popup = (Popup) ((Node) event.getSource()).getScene().getWindow();
        popup.hide();
        origin.setEffect();
    }
}