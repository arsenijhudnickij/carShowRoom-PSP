package main.Models.Entities;

import main.Enums.TestDriveStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "test_drive")
public class TestDrive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "drive_id")
    private int testDriveId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "drive_date", nullable = false)
    private LocalDateTime driveDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TestDriveStatus status;
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

    public LocalDateTime getDriveDate() {
        return driveDate;
    }

    public void setDriveDate(LocalDateTime driveDate) {
        this.driveDate = driveDate;
    }
    public TestDriveStatus getStatus() {
        return status;
    }
    public void setStatus(TestDriveStatus status) {
        this.status = status;
    }
}