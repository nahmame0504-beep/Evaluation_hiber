package ma.projet.util;

import java.io.InputStream;
import java.util.Properties;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ma.projet.classes.Employe;
import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;

public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();

            Properties properties = new Properties();
            InputStream input = HibernateUtil.class.getClassLoader().getResourceAsStream("application.properties");
            if (input != null) {
                properties.load(input);
                for (String key : properties.stringPropertyNames()) {
                    String value = properties.getProperty(key);
                    configuration.setProperty(key, value);
                    if ("jdbc.driver".equals(key)) {
                        configuration.setProperty("hibernate.connection.driver_class", value);
                    } else if ("jdbc.url".equals(key)) {
                        configuration.setProperty("hibernate.connection.url", value);
                    } else if ("jdbc.user".equals(key)) {
                        configuration.setProperty("hibernate.connection.username", value);
                    } else if ("jdbc.password".equals(key)) {
                        configuration.setProperty("hibernate.connection.password", value);
                    }
                }
            } else {
                // Fallback default properties
                configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
                configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/gestion_projets?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
                configuration.setProperty("hibernate.connection.username", "root");
                configuration.setProperty("hibernate.connection.password", "");
                configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
                configuration.setProperty("hibernate.hbm2ddl.auto", "update");
            }

            configuration.addAnnotatedClass(Employe.class);
            configuration.addAnnotatedClass(Projet.class);
            configuration.addAnnotatedClass(Tache.class);
            configuration.addAnnotatedClass(EmployeTache.class);

            sessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
        try {
            com.mysql.cj.jdbc.AbandonedConnectionCleanupThread.checkedShutdown();
        } catch (Throwable ignored) {
        }
    }
}
