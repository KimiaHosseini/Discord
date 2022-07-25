package com.example.finalproject;

import Server.Server;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Popup;

import static com.example.finalproject.ServerController.serverIndex;
import static com.example.finalproject.welcome.SignInController.client;

public class CreateChannelController {

    @FXML
    private Label alertLabel;

    @FXML
    private TextField channelNameTf;

    private static ServerController origin;

    public static void setOrigin(ServerController origin) {
        CreateChannelController.origin = origin;
    }

    @FXML
    void cancelButtonPressed(ActionEvent event) {
        javafx.stage.Popup popup = (Popup) ((Node) event.getSource()).getScene().getWindow();
        popup.hide();
        origin.setEffect();
    }

    @FXML
    void createChannelButtonPressed(ActionEvent event) {
        client.createChannel(serverIndex, channelNameTf.getText());
        cancelButtonPressed(event);
        origin.updateChannels();
    }

}
