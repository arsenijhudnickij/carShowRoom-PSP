package main.Utility;

import main.Models.Entities.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateSessionFactory {
    private static SessionFactory sessionFactory;

    private HibernateSessionFactory() {}

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration().configure();
                configuration.addAnnotatedClass(Admin.class);
                configuration.addAnnotatedClass(Car.class);
                configuration.addAnnotatedClass(CarRequest.class);
                configuration.addAnnotatedClass(CarType.class);
                configuration.addAnnotatedClass(Favorite.class);
                configuration.addAnnotatedClass(Person.class);
                configuration.addAnnotatedClass(Role.class);
                configuration.addAnnotatedClass(User.class);

                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());

            } catch (Exception e) {
                System.out.println("Исключение!" + e);
            }
        }
        return sessionFactory;
    }
}