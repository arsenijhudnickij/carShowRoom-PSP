package main.Services;

import main.DAO.PersonDAO;
import main.Interfaces.Service;
import main.Models.Entities.Admin;
import main.Models.Entities.CarType;
import main.Models.Entities.Person;
import main.Models.Entities.User;
import main.exception.ShowException;
import org.hibernate.HibernateError;

import java.util.List;

public class PersonService implements Service {
    PersonDAO personDAO = new PersonDAO();
    @Override
    public Object findEntity(int id) {
        Person person = null;
        try {
            person = personDAO.findById(id);
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
        return person;
    }

    @Override
    public void saveEntity(Object entity) {
        try {
            personDAO.save((Person) entity);
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
    }

    @Override
    public void deleteEntity(Object entity) {
        try {
            personDAO.delete((Person) entity);
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
    }

    @Override
    public void updateEntity(Object entity) {
        try {
            personDAO.update((Person) entity);
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
    }

    @Override
    public List<Person> findAllEntities() {
        List<Person> persons = null;
        try {
            persons = personDAO.findAll();
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
        return persons;
    }
    public Person findByLoginAndPassword(Person person1) {
        String login = person1.getLogin();
        String password = person1.getPassword();
        return personDAO.findByLoginAndPassword(login, password);
    }

    public Object findUserOrAdmin(Person person) {
        User user = personDAO.findUserByPersonId(person.getPersonId());
        if (user != null) {
            return user;
        }
        Admin admin = personDAO.findAdminByPersonId(person.getPersonId());
        if (admin != null) {
            return admin;
        }
        return null;
    }
    public void updateRoles(List<Integer> personIds) {
        try {
            personDAO.updateRoles(personIds);
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
    }
    public void deleteWorkers(List<Integer> personIds) {
        try {
            personDAO.deleteWorkers(personIds);
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
    }
}
