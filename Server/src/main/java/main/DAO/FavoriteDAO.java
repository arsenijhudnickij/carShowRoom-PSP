package main.DAO;

import main.Interfaces.DAO;
import main.Models.Entities.Favorite;
import main.Utility.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class FavoriteDAO implements DAO<Favorite>{
    @Override
    public void save(Favorite obj) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(obj);
            tx.commit();
        }
    }

    @Override
    public void update(Favorite obj) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(obj);
            tx.commit();
        }
    }

    @Override
    public void delete(Favorite obj) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.delete(obj);
            tx.commit();
        }
    }
    public void deleteByCarId(int carId) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.createQuery("DELETE FROM Favorite WHERE car.carId = :carId")
                    .setParameter("carId", carId)
                    .executeUpdate();
            tx.commit();
        }
    }

    @Override
    public Favorite findById(int id) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Favorite> cq = cb.createQuery(Favorite.class);
            Root<Favorite> root = cq.from(Favorite.class);
            cq.select(root).where(cb.equal(root.get("carId"), id));
            return session.createQuery(cq).uniqueResult();
        }
    }

    @Override
    public List<Favorite> findAll() {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            return session.createQuery("FROM Favorite", Favorite.class).list();
        }
    }
}
