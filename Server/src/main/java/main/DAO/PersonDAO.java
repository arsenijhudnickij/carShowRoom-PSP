package main.DAO;

import main.Interfaces.DAO;
import main.Models.Entities.Person;
import main.Utility.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class PersonDAO implements DAO<Person>{
    @Override
    public void save(Person obj) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(obj);
            tx.commit();
        }
    }

    @Override
    public void update(Person obj) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(obj);
            tx.commit();
        }
    }

    @Override
    public void delete(Person obj) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.delete(obj);
            tx.commit();
        }
    }

    @Override
    public Person findById(int id) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Person> cq = cb.createQuery(Person.class);
            Root<Person> root = cq.from(Person.class);
            cq.select(root).where(cb.equal(root.get("personId"), id));
            return session.createQuery(cq).uniqueResult();
        }
    }

    @Override
    public List<Person> findAll() {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            return session.createQuery("FROM Person", Person.class).list();
        }
    }
    public Person findByLoginAndPassword(String login, String password) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Person> cq = cb.createQuery(Person.class);
            Root<Person> root = cq.from(Person.class);

            cq.select(root).where(cb.and(
                    cb.equal(root.get("login"), login),
                    cb.equal(root.get("password"), password)
            ));

            return session.createQuery(cq).uniqueResult();
        }
    }
}
