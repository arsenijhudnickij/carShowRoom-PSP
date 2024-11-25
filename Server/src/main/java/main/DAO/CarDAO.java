package main.DAO;

import main.Interfaces.DAO;
import main.Models.Entities.Car;
import main.Utility.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class CarDAO implements DAO<Car> {
    @Override
    public void save(Car obj) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(obj);
            tx.commit();
        }
    }

    @Override
    public void update(Car obj) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(obj);
            tx.commit();
        }
    }

    @Override
    public void delete(Car obj) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.delete(obj);
            tx.commit();
        }
    }

    @Override
    public Car findById(int id) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Car> cq = cb.createQuery(Car.class);
            Root<Car> root = cq.from(Car.class);
            cq.select(root).where(cb.equal(root.get("carId"), id));
            return session.createQuery(cq).uniqueResult();
        }
    }

    @Override
    public List<Car> findAll() {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            return session.createQuery("FROM Car", Car.class).list();
        }
    }
}