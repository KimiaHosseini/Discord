package com.example.finalproject;

import Client.Client;
import com.example.finalproject.DiscordApplication;
import com.example.finalproject.MessageOptionsController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static com.example.finalproject.ChatController.*;

public class MessageController{

    public Label index;
    public Button laugh;
    public Button like;
    public Button dislike;

    @FXML
    private Label text;

    @FXML
    private Label usernameLabel;

    public void options(MouseEvent mouseEvent) {
        MessageOptionsController.setClient(client);
        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            MessageOptionsController.setMessageIndex(index.getText());
            DiscordApplication.showPopUp(getClass(), mouseEvent, "MessageOptionsView.fxml");
        }
    }

    public void showDownloadPopUp(MouseEvent mouseEvent) throws IOException {
        String msg = ((Label)mouseEvent.getSource()).getText();
        System.out.println(msg);

        if (!msg.endsWith("DOWNLOAD"))
            return;
        HBox hBox = FXMLLoader.load(getClass().getResource("downloadPopUpView.fxml"));
        RingProgressIndicator rpi = new RingProgressIndicator();

        long size = client.downloadFile(msg.substring(msg.indexOf("sends")+6,msg.lastIndexOf("DOWNLOAD")-1),msg.substring(0, msg.indexOf(' ')));

        download(mouseEvent, hBox, rpi, size);
    }

    public void download(MouseEvent mouseEvent, HBox hBox, RingProgressIndicator rpi, long size) {
        hBox.getChildren().add(rpi);
        Popup popup = new Popup();
        popup.getContent().setAll(hBox);
        popup.show(((Node) mouseEvent.getSource()).getScene().getWindow());

        double sec = 2.5*10e-7*size;
        new Thread(() ->{
            int progress = 0;
            do {
                try {
                    Thread.sleep((long) sec);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int finalProgress = progress;
                Platform.runLater(() -> {
                    rpi.setProgress(finalProgress);
                });

                progress += 1;
            } while (progress <= 100);

            Platform.runLater(() ->{

                popup.hide();
            });
        }).start();
    }
}
