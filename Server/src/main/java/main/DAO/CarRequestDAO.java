package main.DAO;

import main.Interfaces.DAO;
import main.Models.Entities.CarRequest;
import main.Utility.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class CarRequestDAO implements DAO<CarRequest>{
    @Override
    public void save(CarRequest obj) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(obj);
            tx.commit();
        }
    }

    @Override
    public void update(CarRequest obj) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(obj);
            tx.commit();
        }
    }

    @Override
    public void delete(CarRequest obj) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.delete(obj);
            tx.commit();
        }
    }
    public void deleteByCarId(int carId) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.createQuery("DELETE FROM CarRequest WHERE car.carId = :carId")
                    .setParameter("carId", carId)
                    .executeUpdate();
            tx.commit();
        }
    }

    @Override
    public CarRequest findById(int id) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<CarRequest> cq = cb.createQuery(CarRequest.class);
            Root<CarRequest> root = cq.from(CarRequest.class);
            cq.select(root).where(cb.equal(root.get("carId"), id));
            return session.createQuery(cq).uniqueResult();
        }
    }

    @Override
    public List<CarRequest> findAll() {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            return session.createQuery("FROM CarRequest", CarRequest.class).list();
        }
    }
}
