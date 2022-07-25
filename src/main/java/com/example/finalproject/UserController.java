package com.example.finalproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
//import jdk.jfr.Event;

import java.io.File;
import java.io.IOException;

import static com.example.finalproject.ChatController.client;

public class UserController {

    @FXML
    private Label usernameLabel;

    private static HomeController origin;

    public static void setOrigin(HomeController origin) {
        UserController.origin = origin;
    }

    @FXML
    public void userSelected(javafx.scene.input.MouseEvent mouseEvent) throws IOException {

        String myUsername = client.getUsername();
        String chatUsername = usernameLabel.getText();
        File myPfp = client.getPFP(myUsername);
        File chatPfp = client.getPFP(chatUsername);
        ChatController.setMyPfp(myPfp);
        ChatController.setChatPfp(chatPfp);
        System.out.println(myUsername+chatUsername);
        origin.openChat(myUsername, chatUsername, myPfp, chatPfp);
        origin.pending();

    }

}