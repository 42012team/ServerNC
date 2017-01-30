package general;

import classes.model.behavior.storages.ActiveServiceStorage;
import classes.model.behavior.storages.ServiceStorage;
import classes.model.behavior.storages.UserStorage;

import java.sql.SQLException;

/**
 * Created by User on 26.01.2017.
 */
public class Main {
    public static void main(String[] args) throws SQLException {
        Factory factory=Factory.getInstance();
   ServiceStorage serviceDao=factory.getServiceDao();
        ActiveServiceStorage activeServiceDao=factory.getActiveServiceDao();
        //int id, String name, String description, String type, int version
     //   Service s1=new Service(20,"etr","erhgfgtte","rhgfthere",0);
     //  s1.setStatus(ServiceStatus.ALLOWED);
    //    serviceDao.storeService(s1);
  /*  System.out.println(serviceDao.getServiceById(3).getStatus().toString());
        System.out.println(serviceDao.getAllServices());
        for(Service s:serviceDao.getAllServices()){
            System.out.println(s.getName());

        }

        serviceDao.deleteService(20);*/
        System.out.println(activeServiceDao.getActiveServiceById(1).getNewStatus());
        UserStorage userDao= factory.getUserDao();
       // User user=new User(111,"ffrerere","r","r","rerrerr","erker","ter","erk",0,"y");
    //    userDao.storeUser(user);
       // System.out.println(userDao.getUser(111).getLogin()+"  "+userDao.getUser(111).getName());
   //    System.out.println(userDao.getUserByLogin("ada").getName()+"  "+userDao.getUserByLogin("ada").getPassword());
      System.out.println(userDao.getUser("ada","sasa").getName());
    }
}
