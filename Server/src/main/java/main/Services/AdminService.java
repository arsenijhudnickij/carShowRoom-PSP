package main.Services;

import main.DAO.AdminDAO;
import main.Interfaces.Service;
import main.Models.Entities.Admin;
import main.exception.ShowException;
import org.hibernate.HibernateError;

import java.util.List;

public class AdminService implements Service {
    AdminDAO adminDAO = new AdminDAO();

    @Override
    public Object findEntity(int id) {
        Admin admin = null;
        try {
            admin = adminDAO.findById(id);
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
        return admin;
    }

    @Override
    public void saveEntity(Object entity) {
        try {
            adminDAO.save((Admin) entity);
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
    }

    @Override
    public void deleteEntity(Object entity) {
        try {
            adminDAO.delete((Admin) entity);
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
    }

    @Override
    public void updateEntity(Object entity) {
        try {
            adminDAO.update((Admin) entity);
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
    }

    @Override
    public List<Admin> findAllEntities() {
        List<Admin> admins = null;
        try {
            admins = adminDAO.findAll();
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
        return admins;
    }
}
