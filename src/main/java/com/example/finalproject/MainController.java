package com.example.finalproject;

import Client.Client;
import Server.Server;
import UserFeatures.Status;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainController implements Initializable{

    @FXML
    private Pane borderPane;

    @FXML
    private BorderPane pane;

    private Pane borderPane2;

    @FXML
    private ListView<Pane> serversListView;

    private ObservableList<Pane> servers = FXCollections.observableArrayList();

    private static Client client;

    public static void setClient(Client client) {
        MainController.client = client;
    }

    @FXML
    void homeButtonPressed(ActionEvent event) throws IOException {
        BorderPane.setAlignment(borderPane, Pos.TOP_LEFT);
        BorderPane newPane = FXMLLoader.load(getClass().getResource("HomeView.fxml"));
        borderPane.getChildren().setAll(newPane);
    }

    @FXML
    public void addServerButtonPressed(ActionEvent event) {
        CreateServerController.setOrigin(this);
        DiscordApplication.showPopUp(getClass(), event, "CreateServerView.fxml");
    }

    public void update() {
        serversListView.getItems().clear();
        String allServers = client.printServers();
        System.out.println(allServers);
        if (allServers.equals("Empty\n"))
            return;
        String[] allServersSplit = allServers.split("\n");
        for (String serverName : allServersSplit) {
            try {
                Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ServerIconView.fxml")));
                pane.setPrefSize(55, 65);
                Label letterLabel = (Label) pane.getChildren().get(1);
                Label label = (Label) pane.getChildren().get(2);
                letterLabel.setText((serverName.substring(serverName.indexOf("]") + 1, serverName.indexOf("]") + 2).toUpperCase()));
                String index = serverName.substring(0, serverName.indexOf("]"));
                String name = ((serverName.substring(serverName.indexOf("]") + 1)));
                ((Label)(pane.getChildren().get(3))).setText(name);
                label.setText(index);
                servers.add(pane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void openServer() throws IOException {
        borderPane2 = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ServerView.fxml")));
        ServerController.setOrigin(this);
        ChangeServerNameController.setOrigin(this);
        borderPane.getChildren().setAll(borderPane2);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ServerIconController.setOrigin(this);
        HomeController.setClient(client);
        HomeController.setOrigin(this);
        VBoxController.setOrigin(this);
        update();
        serversListView.setItems(servers);
    }

    public void setEffect(){
        pane.setEffect(null);
    }
}