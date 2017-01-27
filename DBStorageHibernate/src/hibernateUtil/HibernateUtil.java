package hibernateUtil;




import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;


/**
 * Created by User on 26.01.2017.
 */
public class HibernateUtil {

    private static SessionFactory sessionFactory=null;

    private HibernateUtil() {
    }

    static {
        try {
            sessionFactory=new Configuration().configure().buildSessionFactory();
       //    sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
            // sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable e) {
            throw new ExceptionInInitializerError(e);


        }

    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }


}
