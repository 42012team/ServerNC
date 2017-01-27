package classes.idgenerator;

import classes.db.DBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IdGeneratorSingletonDB implements IdGenerator {

    private static volatile IdGeneratorSingletonDB instance = null;
    Connection connection = null;

    private IdGeneratorSingletonDB() {

    }

    public static IdGeneratorSingletonDB getInstance() {
        if (instance == null) {
            synchronized (IdGeneratorSingletonDB.class) {
                if (instance == null) {
                    instance = new IdGeneratorSingletonDB();
                }
            }
        }
        return instance;
    }

    @Override
    public int generateId() {
        int nextvalue = 0;
        try {
            connection = DBConnection.getInstance().getDataSourse().getConnection();
            String sql = "select id_generator.NEXTVAL from dual";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            rs.next();
            nextvalue = rs.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(IdGeneratorSingletonDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nextvalue;
    }

}
