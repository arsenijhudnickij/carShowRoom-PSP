package main.Models.Entities;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "passport_number", length = 100)
    private String passportNum;

    @Column(name = "birth_date")
    private int birthDate;

    @Column(name = "birth_month")
    private int birthMonth;

    @Column(name = "birth_year")
    private int birthYear;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id" , nullable = false)
    private Person person;

    public User() {
    }

    public User(String name, String passportNum, int birthDate, int birthMonth, int birthYear, Person person) {
        this.name = name;
        this.passportNum = passportNum;
        this.birthDate = birthDate;
        this.birthMonth = birthMonth;
        this.birthYear = birthYear;
        this.person = person;
    }

    public User(int userId, String name, String passportNum, int birthDate, int birthMonth, int birthYear, Person person) {
        this.userId = userId;
        this.name = name;
        this.passportNum = passportNum;
        this.birthDate = birthDate;
        this.birthMonth = birthMonth;
        this.birthYear = birthYear;
        this.person = person;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassportNum() {
        return passportNum;
    }

    public void setPassportNum(String passportNum) {
        this.passportNum = passportNum;
    }

    public int getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(int birthDate) {
        this.birthDate = birthDate;
    }

    public int getBirthMonth() {
        return birthMonth;
    }

    public void setBirthMonth(int birthMonth) {
        this.birthMonth = birthMonth;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}