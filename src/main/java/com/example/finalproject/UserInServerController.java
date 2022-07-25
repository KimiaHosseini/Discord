package com.example.finalproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.example.finalproject.ServerController.serverIndex;
import static com.example.finalproject.welcome.SignInController.client;

public class UserInServerController implements Initializable {

    @FXML
    private Button blockUnblockButton;

    @FXML
    private Circle profileCircle;

    @FXML
    private Label profileUsernameLabel;

    @FXML
    private Label rolesLabel;

    @FXML
    private Circle statusCircle;

    private static String username;
    private static Circle profileCirc;
    private static Circle statusCirc;

    private static ServerMemberController origin;

    public static void setOrigin(ServerMemberController origin) {
        UserInServerController.origin = origin;
    }

    public static void setUsername(String username) {
        UserInServerController.username = username;
    }

    @FXML
    void blockUnblockButtonPressed(ActionEvent event) {
        if (blockUnblockButton.getText().equals("Unblock")){
            client.unblockUser(username);
        }else{
            client.blockFriend(username);
        }
        goBack(event);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (username.equals(client.getUsername()))
            blockUnblockButton.setDisable(true);
        try {
            HomeController.setProfilePhoto(profileCircle, username);
            HomeController.setStatusPhoto(statusCircle, username);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        profileUsernameLabel.setText(username);
        rolesLabel.setText(client.getRoles(serverIndex, username));
        if (client.isBlocked(username))
            blockUnblockButton.setText("Unblock");
    }

    @FXML
    private void goBack(ActionEvent event){
        javafx.stage.Popup popup = (Popup) ((Node) event.getSource()).getScene().getWindow();
        popup.hide();
        origin.setEffect();
    }
}
