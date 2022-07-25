package com.example.finalproject.account;

import Client.Client;
import com.example.finalproject.MyAccountController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Popup;

public class ChangePasswordController {

    @FXML
    private TextField confirmPassTf;

    @FXML
    private TextField currentPassTf;

    @FXML
    private TextField newPassTf;

    @FXML
    private Label passwordCharStatus;

    @FXML
    private Label passwordCapStatus;

    @FXML
    private Label passwordNumStatus;

    @FXML
    private Label passwordConfirmStatus;

    @FXML
    private Label currentPasswordStatus;


    private static Client client;
    private static MyAccountController origin;

    public static void setOrigin(MyAccountController origin) {
        ChangePasswordController.origin = origin;
    }

    public static void setClient(Client client) {
        ChangePasswordController.client = client;
    }

    @FXML
    public boolean checkCurrent() {
        if (!client.logIn(client.getUsername(), currentPassTf.getText())) {
            currentPasswordStatus.setText("Incorrect Password");
            return false;
        }
        return true;
    }

    @FXML
    public boolean checkConfirm() {
        if (newPassTf.getText().equals(confirmPassTf.getText())) {
            passwordConfirmStatus.textFillProperty().set(Paint.valueOf("#2f9e35"));
            passwordConfirmStatus.setText("Password valid.");
            return true;
        } else {
            passwordConfirmStatus.textFillProperty().set(Paint.valueOf("#c94141"));
            passwordConfirmStatus.setText("Does not match.");
            return false;
        }
    }

    @FXML
    void cancelButtonPressed(ActionEvent event) {
        Popup popup = (Popup) ((Node) event.getSource()).getScene().getWindow();
        popup.hide();
        origin.setEffect();
    }

    @FXML
    void doneButtonPressed(ActionEvent event) {
        if (checkPassword() & checkConfirm() & checkCurrent()) {
            if (client.changePassword(newPassTf.getText())) {
                cancelButtonPressed(event);
            }
        }
    }

    @FXML
    private boolean checkPassword() {
        boolean validPass = true;
        if ((newPassTf.getText().length() < 8)) {
            passwordCharStatus.textFillProperty().set(Paint.valueOf("#c94141"));
            passwordCharStatus.setText("Your password must be at least 8 characters.");
            validPass = false;
        } else {
            passwordCharStatus.textFillProperty().set(Paint.valueOf("#2f9e35"));
            passwordCharStatus.setText("password contains at least 8 characters ✅");
        }

        if (newPassTf.getText().toLowerCase().equals(newPassTf.getText())) {
            passwordCapStatus.textFillProperty().set(Paint.valueOf("#c94141"));
            passwordCapStatus.setText("Your password must include at least one upper case character.");
            validPass = false;
        } else {
            passwordCapStatus.textFillProperty().set(Paint.valueOf("#2f9e35"));
            passwordCapStatus.setText("password contains an upper case letter ✅");
        }
        if (newPassTf.getText().matches("[a-zA-Z]+") || newPassTf.getText().isEmpty()) {
            passwordNumStatus.textFillProperty().set(Paint.valueOf("#c94141"));
            passwordNumStatus.setText("Your password must include at least one number.");
            validPass = false;
        } else {
            passwordNumStatus.textFillProperty().set(Paint.valueOf("#2f9e35"));
            passwordNumStatus.setText("password contains a number ✅");
        }

        if ((newPassTf.getText().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$"))) {
            passwordNumStatus.textFillProperty().set(Paint.valueOf("#2f9e35"));
            passwordCharStatus.setText("Password valid ✅");
            passwordCapStatus.setText("");
            passwordNumStatus.setText("");
        }

        return validPass;

    }

}
