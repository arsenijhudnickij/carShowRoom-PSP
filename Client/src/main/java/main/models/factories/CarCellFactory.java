package main.models.factories;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;
import main.models.entities.Car;

import java.util.Map;

public interface CarCellFactory {
        SimpleStringProperty createNameProperty(Car car);
        SimpleStringProperty createTypeProperty(Car car);
        SimpleDoubleProperty createMaxSpeedProperty(Car car);
        SimpleStringProperty createPetrolTypeProperty(Car car);
        SimpleIntegerProperty createPowerProperty(Car car);
        SimpleDoubleProperty createCostProperty(Car car);
}
