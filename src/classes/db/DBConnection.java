package classes.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.pool.OracleDataSource;

public class DBConnection {

    public static OracleDataSource ds;
    private static volatile DBConnection instance = null;

    /*  private Connection connection = null;
    final String driverName = "oracle.jdbc.driver.OracleDriver";//XML CONFIGURATIONS
    String url = "jdbc:oracle:thin:@Localhost:1521:orcldb1";
    String name = "sys as sysdba";*/
    /*
    ds = new OracleDataSource(); 
ds.setDriverType("thin"); 
ds.setURL("jdbc:oracle:thin:@Localhost:1521:orcldb1"); 
ds.setDatabaseName("orcl"); 
ds.setUser("sys as sysdba"); 
ds.setPassword("ada100986");
*/
    private DBConnection() {
        try {
            ds = new OracleDataSource();
            ds.setDriverType("thin");
            ds.setURL("jdbc:oracle:thin:@Localhost:1521:orcldb1");
            ds.setDatabaseName("orcl");
            ds.setUser("sys as sysdba");
            ds.setPassword("ada100986");
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static DBConnection getInstance() {
        if (instance == null) {
            synchronized (DBConnection.class) {
                if (instance == null) {
                    instance = new DBConnection();
                }
            }
        }
        return instance;
    }

    public OracleDataSource getDataSourse() {
        synchronized (instance) {
            return ds;
        }
    }

}
