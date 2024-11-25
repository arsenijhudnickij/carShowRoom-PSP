package main.Services;

import main.DAO.RoleDAO;
import main.Interfaces.Service;
import main.Models.Entities.Role;
import main.exception.ShowException;
import org.hibernate.HibernateError;

import java.util.List;

public class RoleService implements Service {
    RoleDAO roleDAO = new RoleDAO();
    @Override
    public Object findEntity(int id) {
        Role role = null;
        try {
            role = roleDAO.findById(id);
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
        return role;
    }

    @Override
    public void saveEntity(Object entity) {
        try {
            roleDAO.save((Role) entity);
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
    }

    @Override
    public void deleteEntity(Object entity) {
        try {
            roleDAO.delete((Role) entity);
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
    }

    @Override
    public void updateEntity(Object entity) {
        try {
            roleDAO.update((Role) entity);
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
    }

    @Override
    public List<Role> findAllEntities() {
        List<Role> roles = null;
        try {
            roles = roleDAO.findAll();
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
        return roles;
    }
}
