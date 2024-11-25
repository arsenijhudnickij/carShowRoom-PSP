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
        window.setWidth(width);
        window.setHeight(height);

        window.centerOnScreen();

        window.setX(-10);
        window.setY(0);

        window.setScene(scene);
        window.setTitle(title);
        window.show();
    }
}