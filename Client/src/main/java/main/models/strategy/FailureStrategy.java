package main.models.strategy;

import javafx.scene.control.Alert;

public class FailureStrategy implements DeleteStrategy {
    @Override
    public void execute() {
        Alert failureAlert = new Alert(Alert.AlertType.ERROR);
        failureAlert.setTitle("Ошибка");
        failureAlert.setHeaderText(null);
        failureAlert.setContentText("Не удалось удалить тест-драйв. Повторите попытку позже.");
        failureAlert.showAndWait();
    }
}
