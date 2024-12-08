package main.models.entities;

import main.enums.RequestCarStatus;

public class CarRequest {
    private int idRequest;
    private User user;
    private Car car;
    private RequestCarStatus status;

    public CarRequest() {
    }

    public CarRequest(int idRequest, User user, Car car, RequestCarStatus status) {
        this.idRequest = idRequest;
        this.user = user;
        this.car = car;
        this.status = status;
    }
    public CarRequest(User user, Car car, RequestCarStatus status) {
        this.user = user;
        this.car = car;
        this.status = status;
    }

    public int getIdRequest() {
        return idRequest;
    }

    public void setIdRequest(int idRequest) {
        this.idRequest = idRequest;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public RequestCarStatus getStatus() {
        return status;
    }

    public void setStatus(RequestCarStatus status) {
        this.status = status;
    }
}