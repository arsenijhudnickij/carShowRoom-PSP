package main.DAO;

import main.Interfaces.DAO;
import main.Models.Entities.Admin;
import main.Utility.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class AdminDAO implements DAO<Admin> {
    @Override
    public void save(Admin obj) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(obj);
            tx.commit();
        }
    }

    @Override
    public void update(Admin obj) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(obj);
            tx.commit();
        }
    }

    @Override
    public void delete(Admin obj) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.delete(obj);
            tx.commit();
        }
    }

    @Override
    public Admin findById(int id) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Admin> cr = cb.createQuery(Admin.class);
            Root<Admin> root = cr.from(Admin.class);
            cr.select(root).where(cb.equal(root.get("id"), id));
            return session.createQuery(cr).getSingleResult();
        }
    }

    @Override
    public List<Admin> findAll() {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            return session.createQuery("FROM Admin", Admin.class).list();
        }
    }
}