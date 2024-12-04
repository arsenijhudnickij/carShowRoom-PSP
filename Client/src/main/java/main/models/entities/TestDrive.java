package main.models.entities;

import main.enums.TestDriveStatus;
import main.models.entities.Car;
import main.models.entities.User;

import java.time.LocalDate;
import java.util.Set;

public class TestDrive {
    private int testDriveId;

    private Car car;

    private User user;

    private LocalDate driveDate;
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

    public LocalDate getDriveDate() {
        return driveDate;
    }
    public void setDriveDate(LocalDate driveDate) {
        this.driveDate = driveDate;
    }
    public TestDriveStatus getStatus() {
        return status;
    }

    public void setStatus(TestDriveStatus status) {
        this.status = status;
    }
}