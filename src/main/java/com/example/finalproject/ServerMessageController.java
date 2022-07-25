package com.example.finalproject;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Popup;

import java.io.IOException;

import static com.example.finalproject.welcome.SignInController.client;

public class ServerMessageController extends MessageController{
    @FXML
    public Label serverIndex;

    @FXML
    public Label channelIndex;

    @Override
    public void showDownloadPopUp(MouseEvent mouseEvent) throws IOException {

        String msg = ((Label)mouseEvent.getSource()).getText();
        System.out.println(msg);

        if (!msg.endsWith("DOWNLOAD"))
            return;
        HBox hBox = FXMLLoader.load(getClass().getResource("downloadPopUpView.fxml"));
        RingProgressIndicator rpi = new RingProgressIndicator();
        long size = client.downloadFile(msg.substring(msg.indexOf("sends")+6,msg.lastIndexOf("DOWNLOAD")-1),channelIndex.getText(),serverIndex.getText());
        download(mouseEvent, hBox, rpi, size);
    }

    @Override
    public void options(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            ServerMessageOptionsController.setMessageIndex(index.getText());
            ServerMessageOptionsController.setChannelIndex(channelIndex.getText());
            ServerMessageOptionsController.setServerIndex(serverIndex.getText());
            DiscordApplication.showPopUp(getClass(), mouseEvent, "ServerMessageOptionsView.fxml");
        }
    }
}
