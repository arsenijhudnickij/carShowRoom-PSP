package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import main.utility.ClientSocket;

import java.io.IOException;

public class StartApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        if (!connectToServer()) {
            System.out.println("Can't connect to server.");
            Platform.exit();
            return;
        }
        System.out.println("successful connection to the server");
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("authorization.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 650, 400);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/emblem.jpg")));
        stage.setTitle("Authorization");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private boolean connectToServer() {
        try {
            ClientSocket clientSocket = ClientSocket.getInstance();
            return clientSocket.getSocket() != null && clientSocket.getSocket().isConnected();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        launch();
    }
}