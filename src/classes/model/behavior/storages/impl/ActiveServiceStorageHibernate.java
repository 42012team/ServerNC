package classes.model.behavior.storages.impl;

import classes.hibernateUtil.HibernateUtil;
import classes.model.ActiveService;
import classes.model.behavior.storages.ActiveServiceStorage;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;


public class ActiveServiceStorageHibernate implements ActiveServiceStorage {
    @Override
    public List<ActiveService> getActiveServicesByUserId(int userId) {

        List result = null;
        EntityManager em = null;
        try {
            CriteriaBuilder builder = HibernateUtil.getCriteriaBuilder();
            em = HibernateUtil.getEntityManager();
            CriteriaQuery<ActiveService> criteriaQuery = builder.createQuery(ActiveService.class);
            Root<ActiveService> activeServiceRoot = criteriaQuery.from(ActiveService.class);
            criteriaQuery.select(activeServiceRoot);
            criteriaQuery.where(builder.equal(activeServiceRoot.get("userId"), userId));
            result = em.createQuery(criteriaQuery).getResultList();
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

    @Override
    public void deleteActiveService(int activeServiceId) {
        EntityManager entityManager = null;
        try {
            entityManager = HibernateUtil.getEntityManager();
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaDelete<ActiveService> criteria = builder.createCriteriaDelete(ActiveService.class);
            Root<ActiveService> root = criteria.from(ActiveService.class);
            criteria.where(builder.equal(root.get("id"), activeServiceId));
            entityManager.getTransaction().begin();
            entityManager.createQuery(criteria).executeUpdate();
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
    public List<ActiveService> getAllActiveServices() {
        List<ActiveService> results = null;
        EntityManager entityManager = null;
        try {
            entityManager = HibernateUtil.getEntityManager();
            CriteriaBuilder builder = HibernateUtil.getCriteriaBuilder();
            CriteriaQuery<ActiveService> criteriaQuery = builder.createQuery(ActiveService.class);
            Root<ActiveService> activeServiceRoot = criteriaQuery.from(ActiveService.class);
            criteriaQuery.select(activeServiceRoot);
            results = entityManager.createQuery(criteriaQuery).getResultList();
        } catch (Exception ex) {
            System.out.println("Exception occured!");
            StackTraceElement[] stackTraceElements = ex.getStackTrace();
            for (int i = stackTraceElements.length - 1; i >= 0; i--) {
                System.out.println(stackTraceElements[i].toString());
            }
        } finally {
            entityManager.close();
        }
        return results;
    }

    @Override
    public ActiveService getActiveServiceById(int activeServiceId) {
        ActiveService result = null;
        EntityManager entityManager = null;
        try {
            entityManager = HibernateUtil.getEntityManager();
            result = (ActiveService) entityManager.find(ActiveService.class, activeServiceId);
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
    public void storeActiveServices(List<ActiveService> activeServicesList) {
        EntityManager entityManager = null;
        try {
            entityManager = HibernateUtil.getEntityManager();
            for (int i = 0; i < activeServicesList.size(); i++) {
                entityManager.getTransaction().begin();
                entityManager.merge(activeServicesList.get(i));
                entityManager.getTransaction().commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

}