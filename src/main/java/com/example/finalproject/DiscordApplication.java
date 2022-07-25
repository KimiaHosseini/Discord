package com.example.finalproject;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class DiscordApplication extends Application {

    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("welcomeView.fxml")));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Discord");
        primaryStage.setFullScreenExitHint("");
        primaryStage.show();
    }

    public void changeScene(String fxml) throws IOException {
        Parent pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxml)));
        stage.getScene().setRoot(pane);
    }


    public static void showPopUp(Class<?> c, Event event, String fxml){
        Scene scene = ((Node) event.getSource()).getScene();
        scene.getRoot().setEffect(new GaussianBlur());
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(c.getResource(fxml)));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Popup popup = new Popup();
        popup.getContent().setAll(root);
        popup.show(scene.getWindow());
    }

    public static void main(String[] args) {
        launch();
    }


}