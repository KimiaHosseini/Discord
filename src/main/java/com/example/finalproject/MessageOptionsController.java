package com.example.finalproject;

import Client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;

import java.io.IOException;

import static com.example.finalproject.ChatController.*;

public class MessageOptionsController {
    private static Client client;
    private static String messageIndex;
    private static HomeController origin;
    public static int serverIndex;

    public static void setOrigin(HomeController origin) {
        MessageOptionsController.origin = origin;
    }

    public static void setClient(Client client) {
        MessageOptionsController.client = client;
    }

    public static void setMessageIndex(String messageIndex) {
        MessageOptionsController.messageIndex = messageIndex;
    }

    @FXML
    public void laughMessage(ActionEvent actionEvent) throws IOException {
        client.reactToMessage("1", messageIndex, username);
        origin.openChat(client.getUsername(), username, myPfp, chatPfp);
        goBack(actionEvent);
    }

    @FXML
    public void dislikeMessage(ActionEvent actionEvent) throws IOException {
        client.reactToMessage("2", messageIndex, username);
        origin.openChat(client.getUsername(), username, myPfp, chatPfp);
        goBack(actionEvent);
    }

    @FXML
    public void likeMessage(ActionEvent actionEvent) throws IOException {
        client.reactToMessage("3", messageIndex, username);
        origin.openChat(client.getUsername(), username, myPfp, chatPfp);
        goBack(actionEvent);
    }

    @FXML
    private void goBack(ActionEvent actionEvent) {
        Popup popup = (Popup) ((Node) actionEvent.getSource()).getScene().getWindow();
        popup.hide();
        origin.setEffect();
    }
}