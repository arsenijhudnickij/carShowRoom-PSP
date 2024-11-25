package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.Node;

public class AdminPageController {
    @FXML
    private HBox addCar;
    @FXML
    private HBox giveRole;
    @FXML
    private HBox deleteWorker;

    @FXML
    public void initialize() {
        initTooltips();
    }

    private void initTooltips() {
        setTooltipHandler(addCar, "Добавить автомобиль");
        setTooltipHandler(giveRole, "Назначить роль");
        setTooltipHandler(deleteWorker, "Уволить сотрудника");
    }

    private void setTooltipHandler(Node node, String text) {
        Tooltip tooltip = new Tooltip(text);
        tooltip.setStyle("-fx-background-color: #282828; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 10, 0, 0, 0);");

        node.setOnMouseEntered(event -> Tooltip.install(node, tooltip));
        node.setOnMouseExited(event -> Tooltip.uninstall(node, tooltip));
    }
}