package main.models.entities;

public class Favorite {
    private int idFavorite;
    private User user;
    private Car car;

    public Favorite() {
    }

    public Favorite(int idFavorite, User user, Car car) {
        this.idFavorite = idFavorite;
        this.user = user;
        this.car = car;
    }
    public Favorite(User user, Car car) {
        this.user = user;
        this.car = car;
    }

    public int getIdFavorite() {
        return idFavorite;
    }

    public void setIdFavorite(int idFavorite) {
        this.idFavorite = idFavorite;
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
}
