package main.Models.Entities;

import main.Enums.RequestCarStatus;

import javax.persistence.*;

@Entity
@Table(name = "car_requests")
public class CarRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_request")
    private int idRequest;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id")
    private Car car;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RequestCarStatus status;

    public CarRequest() {
    }

    public CarRequest(int idRequest, User user, Car car, RequestCarStatus status) {
        this.idRequest = idRequest;
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