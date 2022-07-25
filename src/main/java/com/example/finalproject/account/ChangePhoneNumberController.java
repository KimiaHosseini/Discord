package com.example.finalproject.account;

import Client.Client;
import com.example.finalproject.MyAccountController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Popup;

public class ChangePhoneNumberController {

    @FXML
    private TextField phoneNumberTf;

    @FXML
    private Label phoneStatus;

    private static Client client;
    private static MyAccountController origin;

    public static void setOrigin(MyAccountController origin) {
        ChangePhoneNumberController.origin = origin;
    }

    public static void setClient(Client client) {
        ChangePhoneNumberController.client = client;
    }

    @FXML
    void cancelButtonPressed(ActionEvent event) {
        Popup popup = (Popup) ((Node) event.getSource()).getScene().getWindow();
        popup.hide();
        origin.setEffect();
    }

    @FXML
    void doneButtonPressed(ActionEvent event) {
        if(checkPhone()) {
            if (client.changePhone(phoneNumberTf.getText())) {
                origin.updatePhone(phoneNumberTf.getText());
                cancelButtonPressed(event);
            }
        }
    }

    public boolean checkPhone() {
        if (phoneNumberTf.getText().equals("")) {
            phoneStatus.setText("You must enter a phone number.");
            return false;
        }
        else if (phoneNumberTf.getText().matches("(^\\d{10,11}+$|^$|^\\s$)")) {
            phoneStatus.setText("Valid Phone Number.");
            return true;
        } else {
            phoneStatus.setText("Invalid Phone Number.");
            return false;
        }
    }

}
