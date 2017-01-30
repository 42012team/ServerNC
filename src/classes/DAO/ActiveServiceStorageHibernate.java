package classes.DAO;

import classes.model.ActiveService;
import classes.model.behavior.storages.ActiveServiceStorage;
import classes.hibernateUtil.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class ActiveServiceStorageHibernate implements ActiveServiceStorage {
    @Override
    public List<ActiveService> getActiveServicesByUserId(int userId) {

       List result = null;
        Session session = null;
        try {

            session = HibernateUtil.getSessionFactory().openSession();
            Criteria cr = session.createCriteria(ActiveService.class);
             cr.add(Restrictions.eq("userId", userId));
            result =  cr.list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if ((session != null) && (session.isOpen()))

                session.close();
        }
        return result;
    }

    @Override
    public void deleteActiveService(int activeServiceId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            String hql = "delete from ActiveService where id= ?";
            session.createQuery(hql).setParameter(0, activeServiceId).executeUpdate();
            session.createQuery(hql).setInteger(0, activeServiceId).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if ((session != null) && (session.isOpen()))

                session.close();
        }


    }

    @Override
    public List<ActiveService> getAllActiveServices() {
        List<ActiveService> results = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Criteria cr = session.createCriteria(ActiveService.class);
            results = cr.list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if ((session != null) && (session.isOpen()))

                session.close();
        }
        return results;

    }

    @Override
    public ActiveService getActiveServiceById(int activeServiceId) {
        ActiveService result = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            result = (ActiveService) session.load(ActiveService.class,activeServiceId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if ((session != null) && (session.isOpen()))

                session.close();
        }
        return result;
    }

    @Override
    public void storeActiveServices(List<ActiveService> activeServicesList) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();

            for(int i=0;i<activeServicesList.size();i++) {
                session.beginTransaction();
                session.merge(activeServicesList.get(i));
                session.getTransaction().commit();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if ((session != null) && (session.isOpen()))

                session.close();
        }
    }
}
