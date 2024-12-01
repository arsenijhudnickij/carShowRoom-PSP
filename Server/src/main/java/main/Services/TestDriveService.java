package main.Services;

import main.DAO.TestDriveDAO;
import main.Interfaces.Service;
import main.Models.Entities.TestDrive;
import main.exception.ShowException;
import org.hibernate.HibernateError;

import java.util.List;

public class TestDriveService implements Service {
    private TestDriveDAO testDriveDAO = new TestDriveDAO();

    @Override
    public Object findEntity(int id) {
        TestDrive testDrive = null;
        try {
            testDrive = testDriveDAO.findById(id);
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
        return testDrive;
    }

    @Override
    public void saveEntity(Object entity) {
        try {
            testDriveDAO.save((TestDrive) entity);
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
    }

    @Override
    public void deleteEntity(Object entity) {
        try {
            testDriveDAO.delete((TestDrive) entity);
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
    }

    @Override
    public void updateEntity(Object entity) {
        try {
            testDriveDAO.update((TestDrive) entity);
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
    }

    @Override
    public List<TestDrive> findAllEntities() {
        List<TestDrive> testDrives = null;
        try {
            testDrives = testDriveDAO.findAll();
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
        return testDrives;
    }
}