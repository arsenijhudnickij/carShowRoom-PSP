package main.DAO;

import main.Interfaces.DAO;
import main.Models.Entities.Role;
import main.Utility.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class RoleDAO implements DAO<Role>{
    @Override
    public void save(Role obj) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(obj);
            tx.commit();
        }
    }

    @Override
    public void update(Role obj) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(obj);
            tx.commit();
        }
    }

    @Override
    public void delete(Role obj) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.delete(obj);
            tx.commit();
        }
    }

    @Override
    public Role findById(int id) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Role> cq = cb.createQuery(Role.class);
            Root<Role> root = cq.from(Role.class);
            cq.select(root).where(cb.equal(root.get("carId"), id));
            return session.createQuery(cq).uniqueResult();
        }
    }

    @Override
    public List<Role> findAll() {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            return session.createQuery("FROM Role", Role.class).list();
        }
    }
}
