package com.example.finalproject.welcome;

import Client.Client;
import com.example.finalproject.DiscordApplication;
import com.example.finalproject.MainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignInController implements Initializable {

    @FXML private PasswordField passTf;
    @FXML private TextField usernameTf;
    @FXML private Label statusMessage;

    public static Client client;

    public static String usernameAuto;

    public static void setUsernameAuto(String usernameAuto) {
        SignInController.usernameAuto = usernameAuto;
    }

    public static void setClient(Client generalClient) {
        client = generalClient;
    }

    private DiscordApplication discordApplication = new DiscordApplication();
    @FXML
    void loginButtonPressed() throws IOException {
        if (client.logIn(usernameTf.getText(), passTf.getText())) {
            statusMessage.setText("Sign in successful ! We missed you...");
        } else {
            statusMessage.setText("username or password is incorrect.");
            return;
        }

        client.setStatus(1);
        MainController.setClient(client);
       discordApplication.changeScene("mainView.fxml");
    }

    @FXML
    void registerHLinkClicked(ActionEvent event) throws IOException {
        SignUpController.setClient(client);
        discordApplication.changeScene("SignUpView.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (usernameAuto != null) {
            usernameTf.setText(usernameAuto);
        }
    }
}
