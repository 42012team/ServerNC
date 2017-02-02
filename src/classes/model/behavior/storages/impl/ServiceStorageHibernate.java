package classes.model.behavior.storages.impl;

import classes.model.Service;
import classes.model.behavior.storages.ServiceStorage;
import classes.hibernateUtil.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import java.util.List;


public class ServiceStorageHibernate implements ServiceStorage {
    @Override
    public List<Service> getAllServices() {
        List<Service> results = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Criteria cr = session.createCriteria(Service.class);
            results = cr.list();
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
        return results;
    }

    @Override
    public Service getServiceById(int ServiceId) {
        Service result = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            result = (Service) session.load(Service.class, ServiceId);
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
    public void storeService(Service service) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.merge(service);
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
    public void deleteService(int serviceId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            String hql = "delete from Service where id= ?";
            session.createQuery(hql).setInteger(0, serviceId).executeUpdate();
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

}
