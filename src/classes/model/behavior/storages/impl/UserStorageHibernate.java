package classes.model.behavior.storages.impl;

import classes.hibernateUtil.HibernateUtil;
import classes.model.User;
import classes.model.behavior.storages.UserStorage;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


public class UserStorageHibernate implements UserStorage {
    @Override
    public void storeUser(User user) {
        EntityManager entityManager = null;
        try {
            entityManager = HibernateUtil.getEntityManager();
            entityManager.getTransaction().begin();
            entityManager.merge(user);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println("Exception occured!");
            StackTraceElement[] stackTraceElements = ex.getStackTrace();
            for (int i = stackTraceElements.length - 1; i >= 0; i--) {
                System.out.println(stackTraceElements[i].toString());
            }
        } finally {
            entityManager.close();
        }
    }


    @Override
    public User getUserById(int id) {
        User result = null;
        EntityManager entityManager = null;
        try {
            entityManager = HibernateUtil.getEntityManager();
            result = (User) entityManager.find(User.class, id);
        } catch (Exception ex) {
            System.out.println("Exception occured!");
            StackTraceElement[] stackTraceElements = ex.getStackTrace();
            for (int i = stackTraceElements.length - 1; i >= 0; i--) {
                System.out.println(stackTraceElements[i].toString());
            }
        } finally {
            entityManager.close();
        }
        return result;
    }

    @Override
    public User getUserByLogin(String login) {
        User result = null;
        EntityManager em = null;
        try {
            CriteriaBuilder builder = HibernateUtil.getCriteriaBuilder();
            em = HibernateUtil.getEntityManager();
            CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
            Root<User> userRoot = criteriaQuery.from(User.class);
            criteriaQuery.select(userRoot);
            criteriaQuery.where(builder.equal(userRoot.get("login"), login));
            result = em.createQuery(criteriaQuery).getSingleResult();
        } catch (Exception ex) {
            System.out.println("Exception occured!");
            StackTraceElement[] stackTraceElements = ex.getStackTrace();
            for (int i = stackTraceElements.length - 1; i >= 0; i--) {
                System.out.println(stackTraceElements[i].toString());
            }
        } finally {
            em.close();
        }
        return result;
    }

    public User getUser(String login, String password) {
        User result = null;
        EntityManager em = null;
        try {

            CriteriaBuilder builder = HibernateUtil.getCriteriaBuilder();
            em = HibernateUtil.getEntityManager();
            CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
            Root<User> userRoot = criteriaQuery.from(User.class);
            Predicate userRestriction = builder.and(
                    builder.equal(userRoot.get("login"), login),
                    builder.equal(userRoot.get("password"), password)
            );
            criteriaQuery.select(userRoot);
            criteriaQuery.where(userRestriction);
            result = em.createQuery(criteriaQuery).getSingleResult();

        } catch (Exception ex) {
            System.out.println("Exception occured!");
            StackTraceElement[] stackTraceElements = ex.getStackTrace();
            for (int i = stackTraceElements.length - 1; i >= 0; i--) {
                System.out.println(stackTraceElements[i].toString());
            }
        } finally {
            em.close();
        }
        return result;
    }

}