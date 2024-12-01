package main.utility;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class SceneSwitcher {
    public static void switchScene(String fxmlFileName, Node eventSource, int width, int height, String title) throws IOException {
        Parent root = FXMLLoader.load(SceneSwitcher.class.getResource("/main/" + fxmlFileName));
        Scene scene = new Scene(root, width, height);

        Stage window = (Stage) eventSource.getScene().getWindow();
        window.setScene(scene);
        window.setTitle(title);

        window.setWidth(width);
        window.setHeight(height);
        window.setX(-10);
        window.setY(0);
        window.show();
    }


    public static void switchSceneStart(String fxmlFileName, Node eventSource, String title) throws IOException {
        Parent root = FXMLLoader.load(SceneSwitcher.class.getResource("/main/" + fxmlFileName));
        Scene scene = new Scene(root);

        Stage window = (Stage) eventSource.getScene().getWindow();
        window.setScene(scene);
        window.setTitle(title);
        window.sizeToScene();
        window.centerOnScreen();

        window.show();
    }


}