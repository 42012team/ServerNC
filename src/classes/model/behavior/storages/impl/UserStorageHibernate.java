package classes.model.behavior.storages.impl;

import classes.model.User;
import classes.model.behavior.storages.UserStorage;
import classes.hibernateUtil.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;


public class UserStorageHibernate implements UserStorage {
    @Override
    public void storeUser(User user) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println("Exception occured!");
            StackTraceElement[] stackTraceElements = ex.getStackTrace();
            for (int i = stackTraceElements.length - 1; i >= 0; i--) {
                System.out.println(stackTraceElements[i].toString());
            }
        } finally {
            if ((session != null) && (session.isOpen()))
                session.close();
        }
    }


    @Override
    public User getUserById(int id) {
        User result = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            result = (User) session.load(User.class, id);
        } catch (Exception ex) {
            System.out.println("Exception occured!");
            StackTraceElement[] stackTraceElements = ex.getStackTrace();
            for (int i = stackTraceElements.length - 1; i >= 0; i--) {
                System.out.println(stackTraceElements[i].toString());
            }
        } finally {
            if ((session != null) && (session.isOpen()))
                session.close();
        }
        return result;
    }

    @Override
    public User getUserByLogin(String login) {
        User result = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Criteria cr = session.createCriteria(User.class);
            cr.add(Restrictions.eq("login", login));
            if (cr.list().size() > 0)
                result = (User) cr.list().get(0);
        } catch (Exception ex) {
            System.out.println("Exception occured!");
            StackTraceElement[] stackTraceElements = ex.getStackTrace();
            for (int i = stackTraceElements.length - 1; i >= 0; i--) {
                System.out.println(stackTraceElements[i].toString());
            }
        } finally {
            if ((session != null) && (session.isOpen()))
                session.close();
        }
        return result;
    }

    public User getUser(String login, String password) {
        User result = null;
        Session session = null;
        try {
            Criterion criterionLogin = Restrictions.eq("login", login);
            Criterion criterionPassword = Restrictions.eq("password", password);
            session = HibernateUtil.getSessionFactory().openSession();
            Criteria cr = session.createCriteria(User.class);
            LogicalExpression andExp = Restrictions.and(criterionLogin, criterionPassword);
            cr.add(andExp);
            if (cr.list().size() > 0)
                result = (User) cr.list().get(0);
        } catch (Exception ex) {
            System.out.println("Exception occured!");
            StackTraceElement[] stackTraceElements = ex.getStackTrace();
            for (int i = stackTraceElements.length - 1; i >= 0; i--) {
                System.out.println(stackTraceElements[i].toString());
            }
        } finally {
            if ((session != null) && (session.isOpen()))
                session.close();
        }
        return result;
    }

}
