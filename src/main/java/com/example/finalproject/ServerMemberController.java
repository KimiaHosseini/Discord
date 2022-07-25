package com.example.finalproject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

public class ServerMemberController {

    private static ServerController origin;
    @FXML
    private Label usernameLabel;

    public static void setOrigin(ServerController origin) {
        ServerMemberController.origin = origin;
    }

    @FXML
    void showMemberPreview(MouseEvent event) {
        UserInServerController.setUsername(usernameLabel.getText());
        DiscordApplication.showPopUp(getClass(), event, "UserInServerView.fxml");
        UserInServerController.setOrigin(this);
    }

    public void setEffect() {
        origin.setEffect();
    }
}
