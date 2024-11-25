package main.models.entities;

public class Car {
    private int carId;
    private int highSpeed;
    private double acceleration;
    private double engineDisplacement;
    private CarType carType;
    private String petrolType;
    private boolean used;
    private int mileage;
    private int power;
    private String brand;
    private double cost;

    public Car() {
    }

    public Car(int carId, int highSpeed, double acceleration, double engineDisplacement,
               CarType carType, String petrolType, boolean used, int mileage, int power,
               String brand, double cost) {
        this.carId = carId;
        this.highSpeed = highSpeed;
        this.acceleration = acceleration;
        this.engineDisplacement = engineDisplacement;
        this.carType = carType;
        this.petrolType = petrolType;
        this.used = used;
        this.mileage = mileage;
        this.power = power;
        this.brand = brand;
        this.cost = cost;
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

    public double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    public double getEngineDisplacement() {
        return engineDisplacement;
    }

    public void setEngineDisplacement(double engineDisplacement) {
        this.engineDisplacement = engineDisplacement;
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

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
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
}
