package com.example.finalproject;

import Client.Client;
import Model.Response;
import Model.ResponseStatus;
import UserFeatures.Status;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private Circle profileCircle;

    @FXML
    private Circle statusCircle;

    @FXML
    private Label usernameLabel;

    @FXML
    private Pane contentPane;

    @FXML
    public ListView<Pane> privateChatsListView;

    private ObservableList<Pane> privateChats = FXCollections.observableArrayList();

    private static Client client;
    private static MainController origin;

    public static void setOrigin(MainController mainController){
        origin = mainController;
    }

    public static void setClient(Client client) {
        HomeController.client = client;
    }

    public void pending(){
        privateChatsListView.getItems().clear();
        String requests = client.PrivateChatUsernames();
        if (requests.equals("Empty\n"))
            return;

        String[] usernames = requests.split("\n");

        for (String username : usernames) {
            username = username.stripTrailing();
            try {
                Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("UserView.fxml")));
                Circle profileCircle = (Circle) pane.getChildren().get(0);
                setList(username, pane, profileCircle, client, privateChats);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    static void setList(String username, Pane pane, Circle profileCircle, Client client, ObservableList<Pane> privateChats) {
        if (client.getPFP(username) == null) {
            Image image = new Image(new File(System.getProperty("user.dir") + "/src/main/resources/com/example/finalproject/discordLogo.png").toURI().toString());
            profileCircle.setFill(new ImagePattern(image));
        } else {
            Image image = new Image(client.getPFP(username).toURI().toString());
            profileCircle.setFill(new ImagePattern(image));
        }

        setUserInfo(username, pane, client, privateChats);
    }

    static void setUserInfo(String username, Pane pane, Client client, ObservableList<Pane> privateChats) {
        Circle statusCircle = (Circle) pane.getChildren().get(1);
        Status status = client.getStatus(username);
        String path = "";
        if (status == Status.ONLINE) {
            statusCircle.setFill(Color.GREEN);
        }
        else if (status == Status.OFFLINE)
            path = "/src/main/resources/com/example/finalproject/invisibleStatus.png";
        else if (status == Status.IDLE)
            path = "/src/main/resources/com/example/finalproject/idleStatus.png";
        else if (status == Status.DND)
            path = "/src/main/resources/com/example/finalproject/dndStatus.png";

        if (!path.equals("")) {
            Image image = new Image(new File(System.getProperty("user.dir") + path).toURI().toString());
            statusCircle.setFill(new ImagePattern(image));
        }

        Label usernameL = (Label) pane.getChildren().get(2);
        usernameL.setText(username);
        privateChats.add(pane);
    }

    @FXML
    void addPrivateChatPressed(ActionEvent event) {
        SelectUserController.setClient(client);
        SelectUserController.setOrigin(this);
        DiscordApplication.showPopUp(getClass(), event, "SelectUserView.fxml");
    }

    @FXML
    void changeStatus(ActionEvent event) {
        StatusController.setClient(client);
        StatusController.setOrigin(this);
        DiscordApplication.showPopUp(getClass(), event, "StatusView.fxml");
    }

    @FXML
    void settingButtonPressed(ActionEvent event) throws IOException {
        MyAccountController.setClient(client);
        new DiscordApplication().changeScene("MyAccountView.fxml");
    }

    @FXML
    void friendsButtonPressed(ActionEvent event) {
        Pane newPane = null;
        FriendsController.setOrigin(this);
        FriendsController.setClient(client);
        try {
            newPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("FriendsView.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        contentPane.getChildren().setAll(newPane);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUsernameLabel(usernameLabel);
        setStatusPhoto(statusCircle, client.getUsername());
        pending();
        UserController.setOrigin(this);
        ChatController.setClient(client);
        ChatController.setOrigin(this);
        MessageOptionsController.setClient(client);

        privateChatsListView.setItems(privateChats);
        ReceivedFriendRequest.setClient(client);
        try {
            setProfilePhoto(profileCircle, client.getUsername());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void openChat(String myUsername, String chatUsername, File myPFP, File chatPFP) throws IOException {
        Response response = client.getPrivateChat(chatUsername);
        Pane chatPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ChatView.fxml")));

        if (response.getResponseStatus() == ResponseStatus.BLOCKED_USERNAME){
            TextField textField = (TextField) ((HBox)((VBox)chatPane.getChildren().get(0)).getChildren().get(1)).getChildren().get(0);
            textField.setText("YOU CAN NOT SEND MESSAGE");
            chatPane.setDisable(true);
        }


        String allMessages = (String) client.enterPrivateChat(chatUsername).getData();
        ChatController.setUsername(chatUsername);
        if (allMessages.equals("Empty")) {
            contentPane.getChildren().setAll(chatPane);
            return;
        }

        String[] messages = allMessages.split("\n");
        ListView<Pane> panes = (ListView<Pane>)((VBox)chatPane.getChildren().get(0)).getChildren().get(0);
        ObservableList<Pane> observableList = FXCollections.observableArrayList();

        panes.setItems(null);
        for (String message : messages) {
            Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MessageView.fxml")));
            Circle profileCircle = (Circle) pane.getChildren().get(0);
            String userString = message.substring(0, message.indexOf(":"));
            String messageIndex = userString.substring(0, userString.indexOf("]"));
            ((Label)((VBox)pane.getChildren().get(1)).getChildren().get(0)).setText(messageIndex);

            String laughs = client.getLaughs(messageIndex, chatUsername);
            String likes = client.getLikes(messageIndex, chatUsername);
            String dislikes = client.getDislikes(messageIndex, chatUsername);

            MessageOptionsController.setOrigin(this);

            String[] laughsArr = laughs.split("\n");
            String[] likesArr = likes.split("\n");
            String[] dislikeArr = dislikes.split("\n");
            int numLaugh = laughsArr.length;
            int numLike = likesArr.length;
            int numDislike = dislikeArr.length;

            if (laughs.equals(""))
                numLaugh--;
            if (likes.equals(""))
                numLike--;
            if (dislikes.equals(""))
                numDislike--;

            Button laughButton = ((Button)((HBox)pane.getChildren().get(2)).getChildren().get(0));
            Button likeButton = ((Button)((HBox)pane.getChildren().get(2)).getChildren().get(1));
            Button dislikeButton = ((Button)((HBox)pane.getChildren().get(2)).getChildren().get(2));

            reactPopUp(laughButton, laughsArr);
            reactPopUp(likeButton, likesArr);
            reactPopUp(dislikeButton, dislikeArr);
            laughButton.setText("😂 " + numLaugh);
            likeButton.setText("👍 " + numLike);
            dislikeButton.setText("👎 " + numDislike);

            Label usernameL = (Label) ((VBox)pane.getChildren().get(1)).getChildren().get(0);

            if (userString.contains(chatUsername))  {
                setPfp(chatPFP, profileCircle);
                usernameL.setText(chatUsername);
            } else {
                setPfp(myPFP, profileCircle);
                usernameL.setText(myUsername);
            }
            message = message.substring(message.indexOf(":") + 1);
            Label text = ((Label)((VBox)pane.getChildren().get(1)).getChildren().get(1));
            ((Label)((VBox)pane.getChildren().get(1)).getChildren().get(2)).setText(messageIndex);
            text.setText(message);
            observableList.add(0, pane);
        }
        panes.setItems(observableList);
        contentPane.getChildren().setAll(chatPane);
    }

    static void setPfp(File chatPFP, Circle profileCircle) {
        Image image;
        if (chatPFP == null){
            image = new Image(new File(System.getProperty("user.dir") + "/src/main/resources/com/example/finalproject/discordLogo.png").toURI().toString());
        } else {
            image = new Image(chatPFP.toURI().toString());
        }
        profileCircle.setFill(new ImagePattern(image));
    }

    private void reactPopUp(Button button, String[] arr){
        button.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                Parent root = null;
                try {
                    root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("VBox.fxml")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ListView<Label> listView = new ListView<>();
                ObservableList<Label> list = FXCollections.observableArrayList();
                for (String str: arr) {
                    list.add(new Label(str));
                }
                listView.setItems(list);
                ((ListView<Label>)((VBox)root.getChildrenUnmodifiable().get(0)).getChildren().get(0)).setItems(list);
                Popup popup = new Popup();
                popup.getContent().setAll(root);
                popup.show(((Node) event.getSource()).getScene().getWindow());
            }
        });
    }

    public static void setUsernameLabel(Label usernameLabel){
        usernameLabel.setText(client.getUsername());
    }


    public static void setProfilePhoto(Circle profileCircle, String username) throws URISyntaxException {
        if (client.getPFP(username) == null) {
            Image image = new Image(new File(System.getProperty("user.dir") + "/src/main/resources/com/example/finalproject/discordLogo.png").toURI().toString());
            profileCircle.setFill(new ImagePattern(image));
        } else {
            setProfilePhoto(client.getPFP(username), profileCircle);
        }
    }

    public void setStatusPhoto(){
        Status status = client.getStatus();
        String path = "/src/main/resources/com/example/finalproject/invisibleStatus.png";
        if (status == Status.ONLINE) {
            statusCircle.setFill(Color.GREEN);
            return;
        }
        else if (status == Status.OFFLINE)
            path = "/src/main/resources/com/example/finalproject/invisibleStatus.png";
        else if (status == Status.IDLE)
            path = "/src/main/resources/com/example/finalproject/idleStatus.png";
        else if (status == Status.DND)
            path = "/src/main/resources/com/example/finalproject/dndStatus.png";

        Image image = new Image(new File(System.getProperty("user.dir") + path).toURI().toString());
        statusCircle.setFill(new ImagePattern(image));
    }

    public static void setStatusPhoto(Circle statusCircle, String username){
        Status status = client.getStatus(username);
        String path = "/src/main/resources/com/example/finalproject/invisibleStatus.png";
        if (status == Status.ONLINE) {
            statusCircle.setFill(Color.GREEN);
            return;
        }
        else if (status == Status.OFFLINE)
            path = "/src/main/resources/com/example/finalproject/invisibleStatus.png";
        else if (status == Status.IDLE)
            path = "/src/main/resources/com/example/finalproject/idleStatus.png";
        else if (status == Status.DND)
            path = "/src/main/resources/com/example/finalproject/dndStatus.png";

        Image image = new Image(new File(System.getProperty("user.dir") + path).toURI().toString());
        statusCircle.setFill(new ImagePattern(image));
    }

    public static void setProfilePhoto(File file, Circle profileCircle){
        Image image = new Image(file.toURI().toString());
        profileCircle.setFill(new ImagePattern(image));
    }

    public void setEffect(){
        origin.setEffect();
    }
}
