package com.example.finalproject.welcome;

import Client.Client;
import com.example.finalproject.DiscordApplication;
import com.example.finalproject.welcome.SignInController;
import com.example.finalproject.welcome.SignUpController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;


import java.io.IOException;
import java.net.Socket;

public class WelcomeController {

    private static Client client;
    private DiscordApplication discordApplication = new DiscordApplication();

    public void connectClient() {
        if (client == null) {
            Socket socket;
            try {
                socket = new Socket("localhost", 8765);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Client client = new Client(socket);
            this.client = client;
        }
    }

    public void signUpButtonPressed(ActionEvent event) throws IOException {
        connectClient();
        SignUpController.setClient(client);
        discordApplication.changeScene("SignUpView.fxml");
    }

    public void signInButtonPressed(ActionEvent event) throws IOException {
        connectClient();
        SignInController.setClient(client);
        discordApplication.changeScene("SignInView.fxml");
    }

    public void aboutButtonPressed(ActionEvent event) throws IOException {
    }


    public void exitButtonPressed(ActionEvent event) throws IOException {
        System.exit(0);
    }

}