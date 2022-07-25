package com.example.finalproject;

import Client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Popup;

import java.io.IOException;

import static com.example.finalproject.ChatController.*;
import static com.example.finalproject.ChatController.chatPfp;
import static com.example.finalproject.welcome.SignInController.client;

public class ServerMessageOptionsController {
    private static Client client;
    private static String messageIndex;
    private static ServerController origin;
    private static String serverIndex;
    private static String channelIndex;

    public static void setOrigin(ServerController origin) {
        ServerMessageOptionsController.origin = origin;
    }

    public static void setClient(Client client) {
        ServerMessageOptionsController.client = client;
    }

    public static void setMessageIndex(String messageIndex) {
        ServerMessageOptionsController.messageIndex = messageIndex;
    }

    public static void setServerIndex(String serverIndex) {
        ServerMessageOptionsController.serverIndex = serverIndex;
    }

    public static void setChannelIndex(String channelIndex) {
        ServerMessageOptionsController.channelIndex = channelIndex;
    }

    @FXML
    public void laughMessage(ActionEvent actionEvent) throws IOException {
        client.reactToServerMessage("1", messageIndex, serverIndex, channelIndex);
        origin.openChannel(channelIndex);
        goBack(actionEvent);
    }

    @FXML
    public void dislikeMessage(ActionEvent actionEvent) throws IOException {
        client.reactToServerMessage("3", messageIndex, serverIndex, channelIndex);
        origin.openChannel(channelIndex);
        goBack(actionEvent);
    }

    @FXML
    public void likeMessage(ActionEvent actionEvent) throws IOException {
        client.reactToServerMessage("2", messageIndex, serverIndex, channelIndex);
        origin.openChannel(channelIndex);
        goBack(actionEvent);
    }

    public void goBack(ActionEvent actionEvent) {
        Popup popup = (Popup) ((Node) actionEvent.getSource()).getScene().getWindow();
        popup.hide();
        origin.setEffect();
    }

    @FXML
    public void pinMessage(ActionEvent actionEvent) {
        client.pinMessage(serverIndex, channelIndex, messageIndex);
        goBack(actionEvent);
    }

}
