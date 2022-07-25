package com.example.finalproject.welcome;

import Client.Client;
import Model.ResponseStatus;
import com.example.finalproject.DiscordApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;

import java.io.IOException;

public class SignUpController {

    @FXML
    private Label usernameStatus;
    @FXML
    private TextField username;
    @FXML
    private Label passwordCharStatus;
    @FXML
    private Label passwordCapStatus;
    @FXML
    private Label passwordNumStatus;
    @FXML
    private TextField password;
    @FXML
    private Label emailStatus;
    @FXML
    private TextField email;
    @FXML
    private Button signUpStatus;
    @FXML
    private CheckBox enableContinueButton;

    private static Client client;

    private DiscordApplication discordApplication = new DiscordApplication();

    public static void setClient(Client client) {
        SignUpController.client = client;
    }

    @FXML
    public void signUp() throws IOException {
        checkLogin();
    }

    private void checkLogin() throws IOException {

        if (!(checkUsername() & checkEmail() & checkPassword())) {
            return;
        }

        if (!enableContinueButton.isSelected()) {
            enableContinueButton.textFillProperty().set(Paint.valueOf("#c94141"));
            enableContinueButton.setText("I have read and agree to Discord's Terms of Service and Privacy Policy");
            return;
        }

        if (client.completeSignUp(username.getText(), password.getText(), email.getText(), null)) {
            signUpStatus.setText("Sign up completed successfully!");
            SignInController.setUsernameAuto(username.getText());
            logIn();
        }

    }

    /**
     * this method checks the validity of the password each time the user enters a new character in order to have
     * real time updates when the user is entering their password. The message displayed changes with changed user input
     */
    @FXML
    public boolean checkPassword() {
        boolean validPass = true;
        if ((password.getText().length() < 8)) {
            passwordCharStatus.textFillProperty().set(Paint.valueOf("#c94141"));
            passwordCharStatus.setText("Your password must be at least 8 characters.");
            validPass = false;
        } else {
            passwordCharStatus.textFillProperty().set(Paint.valueOf("#2f9e35"));
            passwordCharStatus.setText("password contains at least 8 characters ✅");
        }

        if (password.getText().toLowerCase().equals(password.getText())) {
            passwordCapStatus.textFillProperty().set(Paint.valueOf("#c94141"));
            passwordCapStatus.setText("Your password must include at least one upper case character.");
            validPass = false;
        } else {
            passwordCapStatus.textFillProperty().set(Paint.valueOf("#2f9e35"));
            passwordCapStatus.setText("password contains an upper case letter ✅");
        }
        if (password.getText().matches("[a-zA-Z]+") || password.getText().isEmpty()) {
            passwordNumStatus.textFillProperty().set(Paint.valueOf("#c94141"));
            passwordNumStatus.setText("Your password must include at least one number.");
            validPass = false;
        } else {
            passwordNumStatus.textFillProperty().set(Paint.valueOf("#2f9e35"));
            passwordNumStatus.setText("password contains a number ✅");
        }

        if ((password.getText().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$"))) {
            passwordNumStatus.textFillProperty().set(Paint.valueOf("#2f9e35"));
            passwordCharStatus.setText("Password valid ✅");
            passwordCapStatus.setText("");
            passwordNumStatus.setText("");
        }

        return validPass;

    }

    @FXML
    public void logIn() throws IOException {
        SignInController.setClient(client);
        discordApplication.changeScene("SignInView.fxml");
    }

    @FXML
    public boolean checkUsername() {
        if (username.getText().equals("")) {
            usernameStatus.textFillProperty().set(Paint.valueOf("#c94141"));
            usernameStatus.setText("You must enter a username.");
            return false;
        }
        if (!username.getText().matches("^[a-zA-Z0-9].{5,}$")) {
            usernameStatus.textFillProperty().set(Paint.valueOf("#c94141"));
            usernameStatus.setText("Username must be at least 6 characters.");
            return false;
        }
        if (client.checkUsername(username.getText()).getResponseStatus().equals(ResponseStatus.INVALID_USERNAME)) {
            usernameStatus.textFillProperty().set(Paint.valueOf("#c94141"));
            usernameStatus.setText("The username you've entered already exists. Please try again.");
            return false;
        } else {
            usernameStatus.textFillProperty().set(Paint.valueOf("#2f9e35"));
            usernameStatus.setText("Username valid ✅");
            return true;
        }
    }

    @FXML
    public boolean checkEmail() {
        if (email.getText().equals("")) {
            emailStatus.textFillProperty().set(Paint.valueOf("#c94141"));
            emailStatus.setText("You must enter you email.");
            return false;
        }
        if (!email.getText().matches("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")) {
            emailStatus.textFillProperty().set(Paint.valueOf("#c94141"));
            emailStatus.setText("Invalid Email.");
            return false;
        } else {
            emailStatus.textFillProperty().set(Paint.valueOf("#2f9e35"));
            emailStatus.setText("Email valid ✅");
            return true;
        }
    }

    @FXML
    void signInHLinkPressed() throws IOException {
        SignInController.setClient(client);
        discordApplication.changeScene("SignInView.fxml");
    }

}