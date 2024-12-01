package main.DAO;

import main.Interfaces.DAO;
import main.Models.Entities.Admin;
import main.Models.Entities.Person;
import main.Models.Entities.User;
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
            root.fetch("role");

            cq.select(root).where(cb.and(
                    cb.equal(root.get("login"), login),
                    cb.equal(root.get("password"), password)
            ));

            return session.createQuery(cq).uniqueResult();
        }
    }

    public User findUserByPersonId(int personId) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<User> cq = cb.createQuery(User.class);
            Root<User> root = cq.from(User.class);
            cq.select(root).where(cb.equal(root.join("person").get("personId"), personId));
            return session.createQuery(cq).uniqueResult();
        }
    }

    public void updateRoles(List<Integer> personIds) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            for (Integer personId : personIds) {
                Person person = findById(personId);
                if (person != null && person.getRole() != null) {
                    person.getRole().setRoleId(2);
                    session.update(person);
                }
            }
            tx.commit();
        }
    }
    public void deleteWorkers(List<Integer> personIds) {
        Transaction tx = null;
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            UserDAO userDAO = new UserDAO();

            for (Integer personId : personIds) {
                Person person = findById(personId);
                if (person != null) {
                    User user = userDAO.findByPersonId(personId);
                    if (user != null) {
                        userDAO.delete(user);
                    }
                    session.delete(person);
                }
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }

    public Admin findAdminByPersonId(int personId) {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Admin> cq = cb.createQuery(Admin.class);
            Root<Admin> root = cq.from(Admin.class);
            cq.select(root).where(cb.equal(root.join("person").get("personId"), personId));
            return session.createQuery(cq).uniqueResult();
        }
    }
}
