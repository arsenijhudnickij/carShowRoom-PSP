package main.models.entities;

public class Car {
    private int carId;
    private int highSpeed;
    private CarType carType;
    private String petrolType;
    private int power;
    private String brand;
    private double cost;
    private String imagePath;
    public Car() {
    }

    public Car(int carId, int highSpeed,
               CarType carType, String petrolType, int power,
               String brand, double cost, String imagePath) {
        this.carId = carId;
        this.highSpeed = highSpeed;
        this.carType = carType;
        this.petrolType = petrolType;
        this.power = power;
        this.brand = brand;
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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
