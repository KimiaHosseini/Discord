package com.example.finalproject;

import Client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

import java.net.URISyntaxException;

public class ReceivedFriendRequest {

    @FXML
    private Label usernameLabel;

    private static Client client;
    private static FriendsController origin;

    public static void setOrigin(FriendsController origin) {
        ReceivedFriendRequest.origin = origin;
    }

    public static void setClient(Client client) {
        ReceivedFriendRequest.client = client;
    }

    @FXML
    void acceptFriendRequest(ActionEvent event) throws URISyntaxException {
        client.acceptFriendRequest(usernameLabel.getText());
        origin.pending();
    }

    @FXML
    void deleteFriendRequest(ActionEvent event) throws URISyntaxException {
        client.deleteFriendRequest(usernameLabel.getText());
        origin.pending();
    }


}
