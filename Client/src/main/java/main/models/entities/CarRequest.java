package main.models.entities;

public class CarRequest {
    private int idRequest;
    private User user;
    private Car car;
    private boolean isApproved;

    public CarRequest() {
    }

    public CarRequest(int idRequest, User user, Car car, boolean isApproved) {
        this.idRequest = idRequest;
        this.user = user;
        this.car = car;
        this.isApproved = isApproved;
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

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean isApproved) {
        this.isApproved = isApproved;
    }
}