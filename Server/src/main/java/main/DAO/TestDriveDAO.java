package main.DAO;

import main.Interfaces.DAO;
import main.Models.Entities.Car;
import main.Models.Entities.Person;
import main.Models.Entities.TestDrive;
import main.Utility.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class TestDriveDAO implements DAO<TestDrive> {
    @Override
    public void save(TestDrive obj) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(obj);
            tx.commit();
        }
    }

    @Override
    public void update(TestDrive obj) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(obj);
            tx.commit();
        }
    }

    @Override
    public void delete(TestDrive obj) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.delete(obj);
            tx.commit();
        }
    }


    @Override
    public TestDrive findById(int id) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<TestDrive> cq = cb.createQuery(TestDrive.class);
            Root<TestDrive> root = cq.from(TestDrive.class);
            cq.select(root).where(cb.equal(root.get("testDriveId"), id));
            return session.createQuery(cq).uniqueResult();
        }
    }

    @Override
    public List<TestDrive> findAll() {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            return session.createQuery("FROM TestDrive", TestDrive.class).list();
        }
    }
}