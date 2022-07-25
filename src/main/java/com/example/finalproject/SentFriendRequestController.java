package com.example.finalproject;

import Client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

public class SentFriendRequestController {

    @FXML
    private Label usernameLabel;

    private static Client client;
    private static FriendsController origin;

    public static void setOrigin(FriendsController origin) {
            SentFriendRequestController.origin = origin;
    }

    public static void setClient(Client client) {
        SentFriendRequestController.client = client;
    }

    @FXML
    void deleteFriendRequest(ActionEvent event) {
        client.removeRequest(usernameLabel.getText());
        origin.pending();
    }
}
