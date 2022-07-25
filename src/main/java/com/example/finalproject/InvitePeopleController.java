package com.example.finalproject;

import Model.ResponseStatus;
import Model.Response;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.example.finalproject.ServerController.serverIndex;
import static com.example.finalproject.welcome.SignInController.client;

public class InvitePeopleController implements Initializable {

    @FXML
    private ListView<Pane> friendsListView;

    private ObservableList<Pane> friends = FXCollections.observableArrayList();

    private static ServerController origin;

    public static void setOrigin(ServerController origin) {
        InvitePeopleController.origin = origin;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pending();
        friendsListView.setItems(friends);
    }

    public void pending() {
        friendsListView.getItems().clear();
        String friendsList = client.printFriends();
        System.out.println(friendsList);

        if (friendsList.equals("Empty\n")) {
            return;
        }

        String[] users = friendsList.split("\n");

        for (String username : users) {
            try {
                Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("InviteUserView.fxml")));
                pane.setPrefWidth(640);
                Circle profileCircle = (Circle) pane.getChildren().get(0);
                Image image;
                if (client.getPFP(username) == null) {
                    image = new Image(new File(System.getProperty("user.dir") + "/src/main/resources/com/example/finalproject/discordLogo.png").toURI().toString());
                    profileCircle.setFill(new ImagePattern(image));
                } else {
                    image = new Image(client.getPFP(username).toURI().toString());
                    profileCircle.setFill(new ImagePattern(image));
                }
                Response response = client.inviteFriendToServer(serverIndex, username);
                if (response.getResponseStatus() == ResponseStatus.INVALID_STATUS) {
                    (pane.getChildren().get(2)).setDisable(true);
                } else if (response.getResponseStatus() == ResponseStatus.BANNED_USER) {
                    (pane.getChildren().get(2)).setDisable(true);
                }

                Label usernameL = (Label) pane.getChildren().get(1);
                usernameL.setText(username);
                friends.add(pane);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void goBack(ActionEvent actionEvent) {
        javafx.stage.Popup popup = (Popup) ((Node) actionEvent.getSource()).getScene().getWindow();
        popup.hide();
        origin.setEffect();
    }

}
