package main.DAO;

import main.Interfaces.DAO;
import main.Models.Entities.CarType;
import main.Utility.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class CarTypeDAO implements DAO<CarType>{
    @Override
    public void save(CarType obj) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(obj);
            tx.commit();
        }
    }

    @Override
    public void update(CarType obj) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(obj);
            tx.commit();
        }
    }

    @Override
    public void delete(CarType obj) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.delete(obj);
            tx.commit();
        }
    }

    @Override
    public CarType findById(int id) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<CarType> cq = cb.createQuery(CarType.class);
            Root<CarType> root = cq.from(CarType.class);
            cq.select(root).where(cb.equal(root.get("carId"), id));
            return session.createQuery(cq).uniqueResult();
        }
    }

    @Override
    public List<CarType> findAll() {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            return session.createQuery("FROM CarType", CarType.class).list();
        }
    }
}
