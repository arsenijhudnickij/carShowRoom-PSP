package main.Services;

import main.DAO.CarRequestDAO;
import main.Interfaces.Service;
import main.Models.Entities.CarRequest;
import main.exception.ShowException;
import org.hibernate.HibernateError;

import java.util.List;

public class CarRequestService  implements Service {
    CarRequestDAO carRequestDAO = new CarRequestDAO();
    @Override
    public Object findEntity(int id) {
        CarRequest carRequest = null;
        try {
            carRequest = carRequestDAO.findById(id);
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
        return carRequest;
    }

    @Override
    public void saveEntity(Object entity) {
        try {
            carRequestDAO.save((CarRequest) entity);
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
    }

    @Override
    public void deleteEntity(Object entity) {
        try {
            carRequestDAO.delete((CarRequest) entity);
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
    }

    @Override
    public void updateEntity(Object entity) {
        try {
            carRequestDAO.update((CarRequest) entity);
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
    }

    @Override
    public List<CarRequest> findAllEntities() {
        List<CarRequest> carRequests = null;
        try {
            carRequests = carRequestDAO.findAll();
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
        return carRequests;
    }
}
