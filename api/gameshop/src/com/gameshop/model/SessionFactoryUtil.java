package com.gameshop.model;

import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class SessionFactoryUtil {
    private static org.hibernate.SessionFactory sessionFactory = buildSessionFactory();

    private static org.hibernate.SessionFactory buildSessionFactory() {
        try
        {
            // Create configuration instance
            Configuration configuration = new Configuration();

            // Pass hibernate configuration file
            configuration.configure("hibernate.cfg.xml");

            // Add annotated class to allow hibernate to use the mapping
            // NB: The names used should be exactly the same (case sensitive) as defined in the config file of hibernate
            configuration.addAnnotatedClass(Game.class);
            configuration.addAnnotatedClass(Characterize.class);
            configuration.addAnnotatedClass(Customer.class);
            configuration.addAnnotatedClass(GameConsole.class);
            configuration.addAnnotatedClass(GameEntity.class);
            configuration.addAnnotatedClass(GameKeyword.class);
            configuration.addAnnotatedClass(PaymentMode.class);
            configuration.addAnnotatedClass(PaymentStatus.class);
            configuration.addAnnotatedClass(Purchase.class);

            // Since version 4.x, service registry is being used
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().
                    applySettings(configuration.getProperties()).build();

            // Create session factory instance
            org.hibernate.SessionFactory factory = configuration.buildSessionFactory(serviceRegistry);

            return factory;
        }
        catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            return null;
        }
    }

    public static org.hibernate.SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}
