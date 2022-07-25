package com.example.finalproject;

import Client.Client;
import UserFeatures.Status;
import com.example.finalproject.account.ChangeEmailController;
import com.example.finalproject.account.ChangePasswordController;
import com.example.finalproject.account.ChangePhoneNumberController;
import com.example.finalproject.account.ChangeUsernameController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class MyAccountController implements Initializable {

    @FXML
    public BorderPane pane;

    @FXML
    private Circle statusCircle;

    @FXML
    private Circle circle;

    @FXML
    private Label emailLabel;

    @FXML
    private Label phoneNumberLabel;

    @FXML
    private Label profileUsernameLabel;

    @FXML
    private Label usernameLabel;

    private static Client client;

    private DiscordApplication discordApplication = new DiscordApplication();

    private String profilePhotoPath = "/src/main/resources/com/example/finalproject/discordLogo.png";

    private DiscordApplication application = new DiscordApplication();

    public static void setClient(Client client) {
        MyAccountController.client = client;
    }

    private void setStatusPhoto(){
        Status status = client.getStatus();
        String path = "";
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

    public void updateEmail(String changeEmail) {
        if (changeEmail != null)
            emailLabel.setText(changeEmail);
    }

    public void updatePhone(String phone) {
        if (phone != null)
            phoneNumberLabel.setText(phone);
    }

    public void updateUsername(String username) {
        if (username != null)
            usernameLabel.setText(username);
    }

    public void updatePFP(String imagePath){
        if (imagePath != null)
            profilePhotoPath = imagePath;
    }

    private void setPhoneNumberLabel(){
        String phoneNumber = client.getPhone();
        if (phoneNumber != null) {
            phoneNumberLabel.setText(phoneNumber);
        }
    }

    private void setEmail() {
        emailLabel.setText(client.getEmail());
    }

    private void setUsernameLabel(){
        usernameLabel.setText(client.getUsername());
    }

    private void setProfileUsernameLabel() {
        profileUsernameLabel.setText(client.getUsername());
    }

    private void setProfilePhoto() throws URISyntaxException {
        if (client.getPFP(client.getUsername()) == null) {
            Image image = new Image(new File(System.getProperty("user.dir") + profilePhotoPath).toURI().toString());
            circle.setFill(new ImagePattern(image));
            //client.getUser().setPfp(new File(profilePhotoPath));
        } else {
            setProfilePhoto(client.getPFP(client.getUsername()));
        }
    }

    private void setProfilePhoto(File file) throws URISyntaxException {
        Image image = new Image(file.toURI().toString());
        circle.setFill(new ImagePattern(image));
    }

    @FXML
    void editUsernameButtonPressed(ActionEvent event) {
        com.example.finalproject.account.ChangeUsernameController.setClient(client);
        ChangeUsernameController.setOrigin(this);
        DiscordApplication.showPopUp(getClass(), event, "ChangeUsernameView.fxml");
    }

    @FXML
    void editEmailButtonPressed(ActionEvent event) {
        com.example.finalproject.account.ChangeEmailController.setClient(client);
        ChangeEmailController.setOrigin(this);
        DiscordApplication.showPopUp(getClass(), event, "ChangeEmailView.fxml");
    }

    @FXML
    void PhoneNumberButtonPressed(ActionEvent event) {
        com.example.finalproject.account.ChangePhoneNumberController.setClient(client);
        ChangePhoneNumberController.setOrigin(this);
       DiscordApplication.showPopUp(getClass(), event, "ChangePhoneNumberView.fxml");
    }

    @FXML
    void changePasswordButtonPressed(ActionEvent event) {
        com.example.finalproject.account.ChangePasswordController.setClient(client);
        ChangePasswordController.setOrigin(this);
        DiscordApplication.showPopUp(getClass(), event, "changePasswordView.fxml");
    }

    @FXML
    void editProfileButtonPressed(ActionEvent event) throws URISyntaxException {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("image", "*.png", "*.jpg");
        fileChooser.getExtensionFilters().add(extensionFilter);
        fileChooser.setTitle("Profile Photo");
        File file = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
        if (file != null) {
            client.setPFP(file);
            setProfilePhoto(file);
        }
    }

    @FXML
    void escButtonPressed(ActionEvent event) throws IOException {
        discordApplication.changeScene("mainView.fxml");
    }

    @FXML
    void logOutButtonPressed(ActionEvent event) throws IOException{
       discordApplication.changeScene("SignInView.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setPhoneNumberLabel();
        setUsernameLabel();
        setProfileUsernameLabel();
        setEmail();
        setStatusPhoto();
        try {
            setProfilePhoto();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void setEffect(){
        pane.setEffect(null);
    }
}
