package main.Services;


import main.DAO.CarTypeDAO;
import main.Interfaces.Service;
import main.Models.Entities.Car;
import main.Models.Entities.CarType;
import main.exception.ShowException;
import org.hibernate.HibernateError;

import java.util.List;

public class CarTypeService implements Service {
    CarTypeDAO carTypeDAO = new CarTypeDAO();
    @Override
    public Object findEntity(int id) {
        CarType carType = null;
        try {
            carType = carTypeDAO.findById(id);
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
        return carType;
    }

    @Override
    public void saveEntity(Object entity) {
        try {
            carTypeDAO.save((CarType) entity);
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
    }

    @Override
    public void deleteEntity(Object entity) {
        try {
            carTypeDAO.delete((CarType) entity);
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
    }

    @Override
    public void updateEntity(Object entity) {
        try {
            carTypeDAO.update((CarType) entity);
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
    }

    @Override
    public List<CarType> findAllEntities() {
        List<CarType> carTypes = null;
        try {
            carTypes = carTypeDAO.findAll();
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
        return carTypes;
    }
}
