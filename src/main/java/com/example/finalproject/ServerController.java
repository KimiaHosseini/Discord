package com.example.finalproject;

import Client.Client;
import DiscordFeatures.DiscordServer;
import DiscordFeatures.Permissions;
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
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Popup;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.example.finalproject.welcome.SignInController.client;

public class ServerController implements Initializable {

    private ObservableList<Pane> channels = FXCollections.observableArrayList();
    private ObservableList<Pane> members = FXCollections.observableArrayList();
    private MediaPlayer music;
    private boolean musicIsPlaying;

    @FXML
    public ListView<Pane> UsersListView;

    @FXML
    private ListView<Pane> channelsListView;

    @FXML
    private Pane contentPane;

    @FXML
    private Circle profileCircle;

    @FXML
    private Label serverName;

    @FXML
    private Circle statusCircle;

    @FXML
    private Label usernameLabel;

    @FXML
    private Button playButton;

    public static String serverIndex;
    public static String name;

    private static MainController origin;

    public static void setName(String name) {
        ServerController.name = name;
    }

    public static void setOrigin(MainController origin) {
        ServerController.origin = origin;
    }

    public static void setServerIndex(String serverIndex) {
        ServerController.serverIndex = serverIndex;
    }

    public void setEffect(){
        origin.setEffect();
    }

    @FXML
    void addChannel(ActionEvent event) {
        DiscordServer discordServer = client.getServer(serverIndex);

         if (discordServer.haveThisAccessibility(client.getUser(), Permissions.ADD_CHANNEL))
            DiscordApplication.showPopUp(getClass(), event, "CreateChannelView.fxml");
    }

    @FXML
    void settingButtonPressed(ActionEvent event) {
        DiscordApplication.showPopUp(getClass(), event, "serverSettingsView.fxml");
        ServerSettingsController.setOrigin(this);
    }

    public void openChannel(String channelIndex) throws IOException {
        Pane chatPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ServerChatView.fxml")));

        ((Label)chatPane.getChildren().get(1)).setText(serverIndex);
        ((Label)chatPane.getChildren().get(2)).setText(channelIndex);

        String allMessages = client.printChannelMessages(serverIndex, channelIndex);
        System.out.println(allMessages);
        if (allMessages.equals("Empty")) {
            contentPane.getChildren().setAll(chatPane);
            return;
        }
        String[] messages = allMessages.split("\n");
        ListView<Pane> panes = (ListView<Pane>)((VBox)chatPane.getChildren().get(0)).getChildren().get(0);
        ObservableList<Pane> observableList = FXCollections.observableArrayList();

        panes.setItems(null);
        for (String message : messages) {
            String userString = message.substring(0, message.indexOf(":"));

            Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MessageServerView.fxml")));

            Circle profileCircle = (Circle) pane.getChildren().get(0);
            String messageIndex = userString.substring(0, userString.indexOf("]"));
            String chatUsername = userString.substring(userString.indexOf("]") + 1);
            ((Label)((VBox)pane.getChildren().get(1)).getChildren().get(0)).setText(messageIndex);

            String laughs = client.getServerLaughs(messageIndex, serverIndex, channelIndex);
            String likes = client.getServerLikes(messageIndex, serverIndex, channelIndex);
            String dislikes = client.getServerDislikes(messageIndex, serverIndex, channelIndex);

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

            File chatPFP = client.getPFP(chatUsername);

            HomeController.setPfp(chatPFP, profileCircle);
            Label usernamel = (Label)(((VBox)pane.getChildren().get(1)).getChildren().get(0));
            usernamel.setText(chatUsername);


            message = message.substring(message.indexOf(":") + 1);
            Label text = ((Label)((VBox)pane.getChildren().get(1)).getChildren().get(1));
            ((Label)((VBox)pane.getChildren().get(1)).getChildren().get(2)).setText(messageIndex);
            text.setText(message);
            ((Label)(pane.getChildren().get(3))).setText(serverIndex);
            ((Label)(pane.getChildren().get(4))).setText(channelIndex);
            observableList.add(0,pane);
        }
        panes.setItems(observableList);
        contentPane.getChildren().setAll(chatPane);
    }

    private void reactPopUp(Button button, String[] arr){
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Parent root = null;
                try {
                    root = FXMLLoader.load(getClass().getResource("VBox.fxml"));
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

    public void updateChannels() {
        channelsListView.getItems().clear();
        String allChannels = client.viewChannels(serverIndex);
        System.out.println(allChannels);
        if (allChannels.equals("This server has been deleted.") || allChannels.equals("Empty\n"))
            return;
        String[] allChannelsSplit = allChannels.split("\n");
        for (String serverName : allChannelsSplit) {
            try {
                Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ChannelNameView.fxml")));
                Label channelName = (Label)((HBox) pane.getChildren().get(0)).getChildren().get(0);
                Label label = (Label) pane.getChildren().get(1);
                channelName.setText("#" + (serverName.substring(serverName.indexOf("]") + 1)));
                String index = serverName.substring(0, serverName.indexOf("]"));
                label.setText(index);
                channels.add(pane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateMembers() {
        UsersListView.getItems().clear();
        String allMembers = client.getMembers(serverIndex);
        System.out.println(allMembers);
        String[] allMembersSplit = allMembers.split("\n");
        for (String username : allMembersSplit) {
            username = username.stripTrailing();
            try {
                Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ServerMemberView.fxml")));
                ServerMemberController.setOrigin(this);
                Circle profileCircle = (Circle) pane.getChildren().get(0);
                HomeController.setList(username, pane, profileCircle, client, members);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            HomeController.setProfilePhoto(profileCircle, client.getUsername());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        ChannelNameController.setOrigin(this);
        HomeController.setStatusPhoto(statusCircle, client.getUsername());
        HomeController.setUsernameLabel(usernameLabel);
        ServerChatController.setOrigin(this);
        ServerMessageOptionsController.setClient(client);
        ServerMessageOptionsController.setOrigin(this);
        CreateChannelController.setOrigin(this);
        serverName.setText(name);
        updateChannels();
        updateMembers();

        channelsListView.setItems(channels);
        UsersListView.setItems(members);
    }

    public void serverSettingButtonPressed(ActionEvent actionEvent) {
        DiscordApplication.showPopUp(getClass(), actionEvent, "InvitePeopleView.fxml");
        InvitePeopleController.setOrigin(this);
    }

    public void previousMusic(ActionEvent event) {
    }

    public void playMusic(ActionEvent event) {
        if (music == null)
            return;
        if (musicIsPlaying){
            musicIsPlaying = false;
            music.pause();
            playButton.setText("▷");
        }
        else{
            musicIsPlaying = true;
            music.play();
            playButton.setText("⏸");
        }
    }

    public void nextMusic(ActionEvent event) {
    }

    public void chooseMusic(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("music", "*.mp3");
        fileChooser.getExtensionFilters().add(extensionFilter);
        fileChooser.setTitle("Music");
        File file = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
        music = new MediaPlayer(new Media(Paths.get(file.getAbsolutePath()).toUri().toString()));
    }
}