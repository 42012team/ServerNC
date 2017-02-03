package classes.model.behavior.storages.impl;

import classes.hibernateUtil.HibernateUtil;
import classes.model.Service;
import classes.model.behavior.storages.ServiceStorage;
import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.List;


public class ServiceStorageHibernate implements ServiceStorage {
    @Override
    public List<Service> getAllServices() {
        List<Service> results = null;
        EntityManager em = null;
        try {
            CriteriaBuilder builder = HibernateUtil.getCriteriaBuilder();
            em = HibernateUtil.getEntityManager();
            CriteriaQuery<Service> criteriaQuery = builder.createQuery(Service.class);
            Root<Service> serviceRoot = criteriaQuery.from(Service.class);
            criteriaQuery.select(serviceRoot);
            results = em.createQuery(criteriaQuery).getResultList();
        } catch (Exception ex) {
            System.out.println("Exception occured!");
            StackTraceElement[] stackTraceElements = ex.getStackTrace();
            for (int i = stackTraceElements.length - 1; i >= 0; i--) {
                System.out.println(stackTraceElements[i].toString());
            }
        } finally {
            em.close();
        }
        return results;
    }

    @Override
    public Service getServiceById(int serviceId) {
        Service result = null;
        EntityManager entityManager = null;
        try {
            entityManager = HibernateUtil.getEntityManager();
            result = (Service) entityManager.find(Service.class, serviceId);
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
    public void storeService(Service service) {
        EntityManager entityManager = null;
        try {
            entityManager = HibernateUtil.getEntityManager();
            entityManager.getTransaction().begin();
            entityManager.merge(service);
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
    public void deleteService(int serviceId) {
        EntityManager entityManager = null;
        try {
            entityManager = HibernateUtil.getEntityManager();
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaDelete<Service> criteria = builder.createCriteriaDelete(Service.class);
            Root<Service> root = criteria.from(Service.class);
            criteria.where(builder.equal(root.get("id"), serviceId));
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
}