package main.models.factories;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import main.models.entities.Car;

public class CarCellFactoryImpl implements CarCellFactory {

    @Override
    public SimpleStringProperty createNameProperty(Car car) {
        return new SimpleStringProperty(car.getName());
    }

    @Override
    public SimpleStringProperty createTypeProperty(Car car) {
        return new SimpleStringProperty(car.getCarType().getTypeName());
    }

    @Override
    public SimpleDoubleProperty createMaxSpeedProperty(Car car) {
        return new SimpleDoubleProperty(car.getHighSpeed());
    }

    @Override
    public SimpleStringProperty createPetrolTypeProperty(Car car) {
        return new SimpleStringProperty(car.getPetrolType());
    }

    @Override
    public SimpleIntegerProperty createPowerProperty(Car car) {
        return new SimpleIntegerProperty(car.getPower());
    }

    @Override
    public SimpleDoubleProperty createCostProperty(Car car) {
        return new SimpleDoubleProperty(car.getCost());
    }

}