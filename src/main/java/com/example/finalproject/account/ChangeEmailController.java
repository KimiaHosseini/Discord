package com.example.finalproject.account;

import Client.Client;
import com.example.finalproject.MyAccountController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Paint;
import javafx.stage.Popup;

public class ChangeEmailController {

    @FXML
    private TextField emailTf;

    @FXML
    private Label emailStatus;

    private static Client client;
    private static MyAccountController origin;

    public static void setOrigin(MyAccountController origin) {
        ChangeEmailController.origin = origin;
    }

    public static void setClient(Client client) {
        ChangeEmailController.client = client;
    }

    @FXML
    void cancelButtonPressed(ActionEvent event) {
        Scene scene = ((Node) event.getSource()).getScene();
        Popup popup = (Popup) scene.getWindow();
        popup.hide();
        origin.setEffect();
    }

    @FXML
    void doneButtonPressed(ActionEvent event) {
        if(checkEmail()) {
            if (client.changeEmail(emailTf.getText())) {
                origin.updateEmail(emailTf.getText());
                cancelButtonPressed(event);
            }
        }
    }

    @FXML
    public boolean checkEmail() {
        if (emailTf.getText().equals("")) {
            emailStatus.textFillProperty().set(Paint.valueOf("#c94141"));
            emailStatus.setText("You must enter you email.");
            return false;
        }
        if (!emailTf.getText().matches("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")) {
            emailStatus.textFillProperty().set(Paint.valueOf("#c94141"));
            emailStatus.setText("Invalid Email.");
            return false;
        } else {
            emailStatus.textFillProperty().set(Paint.valueOf("#2f9e35"));
            emailStatus.setText("Email valid.");
            return true;
        }
    }
}
