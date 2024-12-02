package main.Models.Entities;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "test_drive")
public class TestDrive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_drive_id")
    private int testDriveId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "drive_date", nullable = false)
    private LocalDate driveDate;

    // Геттеры и сеттеры
    public int getTestDriveId() {
        return testDriveId;
    }

    public void setTestDriveId(int testDriveId) {
        this.testDriveId = testDriveId;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getDriveDate() {
        return driveDate;
    }

    public void setDriveDate(LocalDate driveDate) {
        this.driveDate = driveDate;
    }
}