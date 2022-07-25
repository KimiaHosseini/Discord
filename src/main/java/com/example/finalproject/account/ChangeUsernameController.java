package com.example.finalproject.account;

import Client.Client;
import Model.ResponseStatus;
import com.example.finalproject.MyAccountController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Popup;

import java.io.IOException;

public class ChangeUsernameController{

    @FXML
    private TextField usernameTf;

    @FXML
    private Label usernameStatus;

    private static Client client;
    private static MyAccountController origin;

    public static void setOrigin(MyAccountController origin) {
        ChangeUsernameController.origin = origin;
    }

    public static void setClient(Client client) {
        ChangeUsernameController.client = client;
    }

    @FXML
    void cancelButtonPressed(ActionEvent event) throws IOException {
        Popup popup = (Popup) ((Node) event.getSource()).getScene().getWindow();
        popup.hide();
        origin.setEffect();
    }

    @FXML
    void doneButtonPressed(ActionEvent event) throws IOException {
        if(checkUsername()) {
            if (client.changeUsername(usernameTf.getText())) {
                origin.updateUsername(usernameTf.getText());
                cancelButtonPressed(event);
            }
        }
    }

    public boolean checkUsername() {
        if (usernameTf.getText().equals("")) {
            usernameStatus.textFillProperty().set(Paint.valueOf("#c94141"));
            usernameStatus.setText("You must enter a username.");
            return false;
        }
        if (!usernameTf.getText().matches("^[a-zA-Z0-9].{5,}$")) {
            usernameStatus.textFillProperty().set(Paint.valueOf("#c94141"));
            usernameStatus.setText("Username must be at least 6 characters.");
            return false;
        }
        if (client.checkUsername(usernameTf.getText()).getResponseStatus().equals(ResponseStatus.INVALID_USERNAME)) {
            usernameStatus.textFillProperty().set(Paint.valueOf("#c94141"));
            usernameStatus.setText("The username you've entered already exists. Please try again.");
            return false;
        } else {
            usernameStatus.textFillProperty().set(Paint.valueOf("#2f9e35"));
            usernameStatus.setText("Username valid ✅");
            return true;
        }
    }
}