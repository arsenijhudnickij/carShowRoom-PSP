package main.Services;

import main.DAO.UserDAO;
import main.Interfaces.Service;
import main.Models.Entities.Role;
import main.Models.Entities.User;
import main.exception.ShowException;
import org.hibernate.HibernateError;

import java.util.List;

public class UserService implements Service {
    public UserDAO userDAO = new UserDAO();
    @Override
    public Object findEntity(int id) {
        User user = null;
        try {
            user = userDAO.findById(id);
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
        return user;
    }

    @Override
    public void saveEntity(Object entity) {
        try {
            userDAO.save((User) entity);
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
    }

    @Override
    public void deleteEntity(Object entity) {
        try {
            userDAO.delete((User) entity);
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
    }

    @Override
    public void updateEntity(Object entity) {
        try {
            userDAO.update((User) entity);
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
    }

    @Override
    public List<User> findAllEntities() {
        List<User> users = null;
        try {
            users = userDAO.findAll();
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
        return users;
    }
}
