package jm.task.core.jdbc.dao;

/*
В рамках этой задачи необходимо реализовать взаимодействие
с базой данных с помощью Hibernate и дописать методы в классе UserHibernateDaoImpl,
проверить свои методы заранее написанными JUnit тестами из заготовки.

   Требования к классам приложения:

 1. UserHibernateDaoImpl должен реализовывать интерефейс UserDao
 2. В класс Util должна быть добавлена конфигурация для Hibernate (рядом с JDBC),
 без использования xml.
 3. Service на этот раз использует реализацию dao через Hibernate
 4. Методы создания и удаления таблицы пользователей в классе UserHibernateDaoImpl
 должны быть реализованы с помощью SQL.

  Алгоритм приложения и операции не меняются в сравнении с предыдущим заданием.
 */

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        //реализация с помощью SQL
        Transaction transaction = null;
        Session session = null;
        String sql = "create table if not exists test.users (id bigint not null auto_increment, name varchar(45) not null, firstName varchar(45) not null, age tinyint(3),primary key (id))";
        try {
            session = Util.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.createSQLQuery(sql).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void dropUsersTable() {
        //реализация с помощью SQL
        Transaction transaction = null;
        Session session = null;
        try {
            session = Util.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.createSQLQuery("drop table if exists test.users").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        User user = new User(name, lastName, age);

        Transaction transaction = null;
        Session session = null;

        try {
            session = Util.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        System.out.println("User с именем " + name + " добавлен в базу данных");
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        Session session = null;

        try {
            session = Util.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            User user;
            if ((user = (User) session.get(User.class, id)) != null) {
                session.delete(user);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> list = null;
        Transaction transaction = null;
        Session session = null;

        try {
            session = Util.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            list = session.createSQLQuery("select id, name, firstName, age from test.users").addEntity(User.class).list();
            list.forEach(System.out::println);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return list;
    }

    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;
        Session session = null;

        try {
            session = Util.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.createSQLQuery("delete from test.users").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}