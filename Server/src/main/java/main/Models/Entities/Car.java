package main.Models.Entities;

import javax.persistence.*;

@Entity
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_id", nullable = false)
    private int carId;

    @Column(name = "high_speed", nullable = false)
    private int highSpeed;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_type", nullable = false)
    private CarType carType;

    @Column(name = "petrol_type", nullable = false)
    private String petrolType;

    @Column(name = "power", nullable = false)
    private int power;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "cost", nullable = false)
    private double cost;

    @Column(name = "image_path", nullable = false)
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

    public Car(int carId) {
        this.carId =carId;
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

    public void setName(String name) {
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