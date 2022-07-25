package com.example.finalproject;

import Client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.stage.Popup;

import java.io.IOException;
import java.util.Objects;

public class ServerSettingsController {
    private static ServerController origin;
    private static Client client;

    public static void setClient(Client client) {
        ServerSettingsController.client = client;
    }

    public static void setOrigin(ServerController origin) {
        ServerSettingsController.origin = origin;
    }

    @FXML
    void changeServerName(ActionEvent event) throws IOException {
        javafx.stage.Popup popup = (Popup) ((Node) event.getSource()).getScene().getWindow();
        popup.getContent().setAll((Node) FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ChangeServerName.fxml"))));
    }

    @FXML
    void createNewRole(ActionEvent event) {
        DiscordApplication.showPopUp(getClass(), event, "CreateRoleView.fxml");

    }

    @FXML
    void deleteServer(ActionEvent event) {

    }

    @FXML
    void goBack(ActionEvent event) {
        javafx.stage.Popup popup = (Popup) ((Node) event.getSource()).getScene().getWindow();
        popup.hide();
        origin.setEffect();
    }

    public void assignRole(ActionEvent actionEvent) {
    }
}
