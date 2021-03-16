package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;


public class Util {
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "lN?ekDMrA2yX";
    private static final String URL = "jdbc:mysql://localhost:3306/test?serverTimezone=UTC";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static SessionFactory sessionFactory;

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Properties properties = new Properties();

                properties.setProperty(Environment.DRIVER, DRIVER);
                properties.setProperty(Environment.URL, URL);
                properties.setProperty(Environment.USER, USER_NAME);
                properties.setProperty(Environment.PASS, PASSWORD);
                properties.setProperty(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
                properties.setProperty(Environment.SHOW_SQL, "true");
                properties.setProperty(Environment.HBM2DDL_AUTO, "create");
                properties.setProperty(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");

                Configuration configuration = new Configuration();

                configuration.addAnnotatedClass(User.class);
                configuration.setProperties(properties);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}