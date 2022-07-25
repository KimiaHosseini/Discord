package com.example.finalproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Popup;

import java.io.File;
import java.io.IOException;

import static com.example.finalproject.welcome.SignInController.client;


public class ServerChatController {

    @FXML
    public Label serverIndex;

    @FXML
    public Label channelIndex;

    @FXML
    private TextField messageTf;

    private static ServerController origin;


    public static void setOrigin(ServerController origin) {
        ServerChatController.origin = origin;
    }

    @FXML
    void sendChannelFileButtonPressed(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Send File");
        File file = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
        if (file != null) {
            client.sendFile(channelIndex.getText(), serverIndex.getText(), file);
            client.sendChannelMessage(channelIndex.getText(), serverIndex.getText(), client.getUsername()+ ":" + client.getUsername()+ " sends " + file.getName() + " DOWNLOAD");
        }
    }

    @FXML
    void sendChannelMessageButtonPressed(ActionEvent event) throws IOException {
        client.sendChannelMessage(channelIndex.getText(), serverIndex.getText(), client.getUsername() + ":" +messageTf.getText());
        origin.openChannel( channelIndex.getText());
    }

    @FXML
    void showChannelPinnedMessages(ActionEvent event) {
        String pinnedMessages = client.viewPinnedMessages(serverIndex.getText(), channelIndex.getText());
        String[] pinnedMessagesArr = pinnedMessages.split("\n");
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("VBox.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ListView<Label> listView = new ListView<>();
        ObservableList<Label> list = FXCollections.observableArrayList();
        for (String str: pinnedMessagesArr) {
            list.add(new Label(str));
        }
        listView.setItems(list);
        ((ListView<Label>)((VBox)root.getChildrenUnmodifiable().get(0)).getChildren().get(0)).setItems(list);
        Popup popup = new Popup();
        popup.getContent().setAll(root);
        popup.show(((Node) event.getSource()).getScene().getWindow());
    }
}
