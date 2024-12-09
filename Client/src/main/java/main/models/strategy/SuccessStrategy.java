package main.models.strategy;

import javafx.scene.control.Alert;
import main.models.entities.TestDrive;

import java.util.List;

public class SuccessStrategy implements DeleteStrategy {
    private final Runnable action;

    public SuccessStrategy(Runnable action) {
        this.action = action;
    }

    @Override
    public void execute() {
        if (action != null) {
            action.run();
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Успех");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Тест-драйв успешно удален!");
            successAlert.showAndWait();
        }
    }
}


