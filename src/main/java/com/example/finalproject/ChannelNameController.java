package com.example.finalproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.example.finalproject.ServerController.serverIndex;
import static com.example.finalproject.welcome.SignInController.client;

public class ChannelNameController implements Initializable {

    @FXML
    public Label channelIndex;
    public Button button;
    @FXML
    private Label channelNameLabel;

    private static ServerController origin;

    public static void setOrigin(ServerController origin) {
        ChannelNameController.origin = origin;
    }

    @FXML
    void channelSettingButtonPressed(ActionEvent event) {
        if (button.getOpacity() != 1)
            return;
        client.deleteChannel(channelIndex.getText(), serverIndex);
        origin.updateChannels();
    }

    public void channelChosen(MouseEvent mouseEvent) throws IOException {
        origin.openChannel(channelIndex.getText());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (client.canDeleteChannel(serverIndex))
            button.setOpacity(1);
    }
}