package main.Services;

import main.DAO.AdminDAO;
import main.DAO.CarDAO;
import main.Interfaces.Service;
import main.Models.Entities.Admin;
import main.Models.Entities.Car;
import main.exception.ShowException;
import org.hibernate.HibernateError;

import java.util.List;

public class CarService implements Service {
    CarDAO carDAO = new CarDAO();

    @Override
    public Object findEntity(int id) {
        Car car = null;
        try {
            car = carDAO.findById(id);
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
        return car;
    }

    @Override
    public void saveEntity(Object entity) {
        try {
            carDAO.save((Car) entity);
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
    }

    @Override
    public void deleteEntity(Object entity) {
        try {
            carDAO.delete((Car) entity);
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
    }

    @Override
    public void updateEntity(Object entity) {
        try {
            carDAO.update((Car) entity);
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
    }

    @Override
    public List<Car> findAllEntities() {
        List<Car> cars = null;
        try {
            cars = carDAO.findAll();
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
        return cars;
    }
}
