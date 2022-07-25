package com.example.finalproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Popup;

import static com.example.finalproject.welcome.SignInController.client;

public class CreateServerController {

    @FXML
    private TextField serverNameTf;

    private static MainController origin;

    public static void setOrigin(MainController origin) {
        CreateServerController.origin = origin;
    }

    @FXML
    void cancelButtonPressed(ActionEvent event) {
        Popup popup = (Popup) ((Node) event.getSource()).getScene().getWindow();
        popup.hide();
        origin.setEffect();
    }

    @FXML
    void doneButtonPressed(ActionEvent event) {
        client.createServer(serverNameTf.getText());
        origin.update();
        cancelButtonPressed(event);
    }

}