package main.models.entities;

public class Car {
    private int carId;
    private int highSpeed;
    private CarType carType;
    private String petrolType;
    private int power;
    private String name;
    private double cost;
    private String imagePath;
    public Car() {
    }

    public Car(int carId, int highSpeed,
               CarType carType, String petrolType, int power,
               String name, double cost, String imagePath) {
        this.carId = carId;
        this.highSpeed = highSpeed;
        this.carType = carType;
        this.petrolType = petrolType;
        this.power = power;
        this.name = name;
        this.cost = cost;
        this.imagePath = imagePath;
    }
    public Car(int highSpeed,
               CarType carType, String petrolType, int power,
               String name, double cost, String imagePath) {
        this.highSpeed = highSpeed;
        this.carType = carType;
        this.petrolType = petrolType;
        this.power = power;
        this.name = name;
        this.cost = cost;
        this.imagePath = imagePath;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public int getHighSpeed() {
        return highSpeed;
    }

    public void setHighSpeed(int highSpeed) {
        this.highSpeed = highSpeed;
    }

    public CarType getCarType() {
        return carType;
    }

    public void setCarType(CarType carType) {
        this.carType = carType;
    }

    public String getPetrolType() {
        return petrolType;
    }

    public void setPetrolType(String petrolType) {
        this.petrolType = petrolType;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public String getName() {
        return name;
    }

    public void setBrand(String name) {
        this.name = name;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

}
