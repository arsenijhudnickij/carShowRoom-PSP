package main.models.entities;

public class Admin {
    private int id;
    private Person person;

    public Admin() {
    }

    public Admin(int id, Person person) {
        this.id = id;
        this.person = person;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}