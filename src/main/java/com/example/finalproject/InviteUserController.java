package com.example.finalproject;

import Model.Response;
import UserFeatures.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

import static com.example.finalproject.ServerController.serverIndex;
import static com.example.finalproject.welcome.SignInController.client;

public class InviteUserController {

    @FXML
    private Button inviteButton;

    @FXML
    private Circle profileCircle;

    @FXML
    private Label usernameLabel;

    @FXML
    void inviteButtonPressed(ActionEvent event) {
        Response response = client.inviteFriendToServer( serverIndex, usernameLabel.getText());
//        User invitedFriend = (User) response.getData();
//        invitedFriend.addServer(client.getUser().getServer(Integer.parseInt(serverIndex)));
    }

}
