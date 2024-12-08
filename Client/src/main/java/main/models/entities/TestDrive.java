package main.models.entities;

import main.enums.TestDriveStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TestDrive {
    private int testDriveId;

    private Car car;

    private User user;

    private LocalDateTime driveDate;

    private TestDriveStatus status;
    public TestDrive(int testDriveId, Car car, User user, LocalDateTime driveDate, TestDriveStatus status) {
        this.testDriveId = testDriveId;
        this.car = car;
        this.user = user;
        this.driveDate = driveDate;
        this.status = status;
    }
    public TestDrive( Car car, User user, LocalDateTime driveDate, TestDriveStatus status) {
        this.car = car;
        this.user = user;
        this.driveDate = driveDate;
        this.status = status;
    }
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