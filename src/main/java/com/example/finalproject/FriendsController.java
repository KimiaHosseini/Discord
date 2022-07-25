package com.example.finalproject;

import Client.Client;
import Model.ResponseStatus;
import UserFeatures.Status;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class FriendsController implements Initializable {

    @FXML
    private ListView<Pane> blockedListView;

    @FXML
    private ListView<Pane> onlineListView;

    @FXML
    private ListView<Pane> pendingListView;

    @FXML
    private ListView<Pane> allListView;

    @FXML
    private TextField usernameTf;

    private static Client client;

    private ObservableList<Pane> friendRequests = FXCollections.observableArrayList();
    private ObservableList<Pane> allFriends = FXCollections.observableArrayList();
    private ObservableList<Pane> onlineFriends = FXCollections.observableArrayList();
    private ObservableList<Pane> blockedFriends = FXCollections.observableArrayList();

    private static HomeController origin;
    //default pfp
    private String profilePhotoPath = "/src/main/resources/com/example/finalproject/discordLogo.png";

    public static void setClient(Client client) {
        FriendsController.client = client;
    }

    public static void setOrigin(HomeController origin) {
        FriendsController.origin = origin;
    }

    @FXML
    void sendFriendRequestButtonPressed(ActionEvent event) {
        PopUpAlertController.setOrigin(this);
        ResponseStatus response = client.sendFriendRequest(usernameTf.getText());
        if (response == ResponseStatus.FRIEND_REQUEST_TO_FRIEND)
            DiscordApplication.showPopUp(getClass(), event, "DuplicateFriendView.fxml");
        else if (response == ResponseStatus.INVALID_USERNAME)
            DiscordApplication.showPopUp(getClass() ,event, "FriendRequestFailedView.fxml");
        else if (response == ResponseStatus.DUPLICATE_FRIEND_REQUEST)
            DiscordApplication.showPopUp(getClass() ,event, "DuplicateRequestView.fxml");
        else if (response == ResponseStatus.VALID_FRIEND_REQUEST) {
            DiscordApplication.showPopUp(getClass(), event, "FriendRequestSuccessfulView.fxml");
        }
    }

    public void setEffect(){
        origin.setEffect();
    }

    public void pending(){
        System.out.println("pending");
        pendingListView.getItems().clear();
        String requests = client.getFriendRequests();
        if (!requests.equals("Empty\n")) {
            String[] usernames = requests.split(" ");

            for (String username : usernames) {
                username = username.stripTrailing();

                try {
                    Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ReceivedFriendRequestView.fxml")));
                    Circle profileCircle = (Circle) pane.getChildren().get(0);
                    if (client.getPFP(username) == null) {
                        Image image = new Image(new File(System.getProperty("user.dir") + profilePhotoPath).toURI().toString());
                        profileCircle.setFill(new ImagePattern(image));
                    } else {
                        Image image = new Image(client.getPFP(username).toURI().toString());
                        profileCircle.setFill(new ImagePattern(image));
                    }

                    HomeController.setUserInfo(username, pane, client, friendRequests);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        String sentRequests = client.getSentRequests();
        if (!sentRequests.equals("Empty\n")) {
            String[] sent = sentRequests.split(" ");

            for (String username : sent) {
                username = username.stripTrailing();

                try {
                    Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("SentFriendRequestView.fxml")));
                    Circle profileCircle = (Circle) pane.getChildren().get(0);
                    if (client.getPFP(username) == null) {
                        Image image = new Image(new File(System.getProperty("user.dir") + profilePhotoPath).toURI().toString());
                        profileCircle.setFill(new ImagePattern(image));
                    } else {
                        Image image = new Image(client.getPFP(username).toURI().toString());
                        profileCircle.setFill(new ImagePattern(image));
                    }

                    HomeController.setUserInfo(username, pane, client, friendRequests);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void friends(){
        System.out.println("friends");
        allListView.getItems().clear();
        onlineListView.getItems().clear();

        String friendsList = client.printFriends();
        if (friendsList.equals("Empty\n")) {
            return;
        }
        String[] friends = friendsList.split("\n");

        for (String username : friends ) {
            boolean isOnline = false;
            try {
                Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("UserView.fxml")));
                pane.setPrefWidth(640);
                Circle profileCircle = (Circle) pane.getChildren().get(0);
                Image image;
                if (client.getPFP(username) == null) {
                    image = new Image(new File(System.getProperty("user.dir") + profilePhotoPath).toURI().toString());
                    profileCircle.setFill(new ImagePattern(image));
                } else {
                    image = new Image(client.getPFP(username).toURI().toString());
                    profileCircle.setFill(new ImagePattern(image));
                }

                Circle statusCircle = (Circle) pane.getChildren().get(1);
                Status status = client.getStatus(username);
                String path = "";
                Paint paint = null;
                if (status == Status.ONLINE) {
                    paint = Color.GREEN;
                    isOnline = true;
                } else if (status == Status.OFFLINE) {
                    path = "/src/main/resources/com/example/finalproject/invisibleStatus.png";
                } else if (status == Status.IDLE) {
                    path = "/src/main/resources/com/example/finalproject/idleStatus.png";
                    isOnline = true;
                }else if (status == Status.DND) {
                    path = "/src/main/resources/com/example/finalproject/dndStatus.png";
                    isOnline = true;
                }

                if (!path.equals("")) {
                    paint = new ImagePattern(new Image(new File(System.getProperty("user.dir") + path).toURI().toString()));
                }

                statusCircle.setFill(paint);

                Label usernameL = (Label) pane.getChildren().get(2);
                usernameL.setText(username);
                allFriends.add(pane);
                if (isOnline) {
                    Pane pane2 =  FXMLLoader.load(Objects.requireNonNull(getClass().getResource("UserView.fxml")));
                    pane2.setPrefWidth(640);
                    ((Circle)pane2.getChildren().get(0)).setFill(new ImagePattern(image));
                    ((Circle) pane2.getChildren().get(1)).setFill(paint);
                    ((Label) pane2.getChildren().get(2)).setText(username);
                    onlineFriends.add(pane2);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void blocked(){
        System.out.println("blocked");
        blockedListView.getItems().clear();

        String allBlocked = client.printBlockedUsers();
        System.out.println(allBlocked);
        if (allBlocked.equals("Empty\n")) {
            return;
        }
        String[] blockedusers = allBlocked.split("\n");

        for (String username : blockedusers) {
            try {
                Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("UserView.fxml")));
                Circle profileCircle = (Circle) pane.getChildren().get(0);
                if (client.getPFP(username) == null) {
                    Image image = new Image(new File(System.getProperty("user.dir") + profilePhotoPath).toURI().toString());
                    profileCircle.setFill(new ImagePattern(image));
                    //client.getUser().setPfp(new File(profilePhotoPath));
                } else {
                    Image image = new Image(client.getPFP(username).toURI().toString());
                    profileCircle.setFill(new ImagePattern(image));
                }

                HomeController.setUserInfo(username, pane, client, blockedFriends);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ReceivedFriendRequest.setClient(client);
        ReceivedFriendRequest.setOrigin(this);
        SentFriendRequestController.setOrigin(this);
        SentFriendRequestController.setClient(client);

        pending();
        friends();
        blocked();


        pendingListView.setItems(friendRequests);
        allListView.setItems(allFriends);
        onlineListView.setItems(onlineFriends);
        blockedListView.setItems(blockedFriends);
    }
}
