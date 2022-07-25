package com.example.finalproject;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import static com.example.finalproject.welcome.SignInController.client;

public class ServerIconController implements Initializable {

    @FXML
    public Label serverIndex;

    @FXML
    public Label name;

    @FXML
    private Circle serverCircle;

    private static MainController origin;

    public static void setOrigin(MainController origin) {
        ServerIconController.origin = origin;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Random random = new Random();
        int red = random.nextInt(255);
        int green = random.nextInt(255);
        int blue = random.nextInt(255);
        serverCircle.setFill(Color.rgb(red, green, blue));
    }

    public void enterServer(MouseEvent mouseEvent) throws IOException {
        ServerController.setServerIndex(serverIndex.getText());
        ServerController.setName(name.getText());
        origin.openServer();
    }
}