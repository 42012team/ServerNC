package general;


import DAO.ActiveServiceStorageHibernate;
import DAO.ServiceStorageHibernate;
import DAO.UserStorageHibernate;
import classes.model.behavior.storages.ActiveServiceStorage;
import classes.model.behavior.storages.ServiceStorage;
import classes.model.behavior.storages.UserStorage;

/**
 * Created by User on 26.01.2017.
 */
public class Factory {
    private static Factory instance = new Factory();
    private UserStorage userDao;
    private ServiceStorage serviceDao;
    private ActiveServiceStorage activeServiceDao;
    private Factory() {

    }

    public static Factory getInstance() {
        return Factory.instance;
    }

    public UserStorage getUserDao() {
        if (userDao == null)
            userDao = new UserStorageHibernate();
        return userDao;
    }
    public ServiceStorage getServiceDao() {
        if (serviceDao== null)
            serviceDao= new ServiceStorageHibernate();
        return serviceDao;
    }
    public ActiveServiceStorage getActiveServiceDao() {
        if (activeServiceDao== null)
            activeServiceDao= new ActiveServiceStorageHibernate();
        return activeServiceDao;
    }
}
