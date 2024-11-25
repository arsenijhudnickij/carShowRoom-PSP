package main.models.entities;

public class Person {

    private int personId;
    private String login;
    private String password;
    private Role role;

    public Person() {
    }

    public Person(String login, String password, Role role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }
    public Person(String login, String password) {
        this.login = login;
        this.password = password;
    }
    public Person(int personId, String login, String password, Role role) {
        this.personId = personId;
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
