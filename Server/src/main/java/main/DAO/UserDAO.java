package main.DAO;

import main.Interfaces.DAO;
import main.Models.Entities.User;
import main.Utility.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class UserDAO implements DAO<User>{
    @Override
    public void save(User obj) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(obj);
            tx.commit();
        }
    }

    @Override
    public void update(User obj) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(obj);
            tx.commit();
        }
    }

    @Override
    public void delete(User obj) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.delete(obj);
            tx.commit();
        }
    }

    @Override
    public User findById(int id) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<User> cq = cb.createQuery(User.class);
            Root<User> root = cq.from(User.class);
            cq.select(root).where(cb.equal(root.get("userId"), id));
            return session.createQuery(cq).uniqueResult();
        }
    }

    @Override
    public List<User> findAll() {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            return session.createQuery("FROM User", User.class).list();
        }
    }
}