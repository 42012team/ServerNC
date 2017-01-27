package classes.model.behavior.storages.impl;

import classes.db.DBConnection;
import classes.model.User;
import classes.model.behavior.storages.UserStorage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBUserStorage implements UserStorage {

    private Connection connection = null;

    @Override
    public User getUser(String login, String password) {
        User user = null;
        try {
            connection = DBConnection.getInstance().getDataSourse().getConnection();
            String sql = "SELECT *FROM USER_ WHERE LOGIN=? AND PASSWORD=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, login);
            ps.setString(2, password);
            user = getUserByConditions(ps);
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBUserStorage.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBUserStorage.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return user;
    }

    @Override
    public void storeUser(User user) {
        try {
            connection = DBConnection.getInstance().getDataSourse().getConnection();

            String mergeSql = "MERGE INTO USER_ U USING("
                    + "SELECT *FROM dual)"
                    + " ON((select count(*) from user_ where user_id=?)!=0)"
                    + " WHEN MATCHED THEN "
                    + " UPDATE SET USER_NAME = ?, USER_SURNAME=?, EMAIL=?, PHONE=?,"
                    + " ADDRESS=?,LOGIN=?, PASSWORD=?, VERSION=?, PRIVILEGE=? WHERE USER_ID=? "
                    + " WHEN NOT MATCHED THEN "
                    + "INSERT VALUES(?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(mergeSql);
            ps.setInt(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getSurname());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getPhone());
            ps.setString(6, user.getAddress());
            ps.setString(7, user.getLogin());
            ps.setString(8, user.getPassword());
            ps.setInt(9, user.getVersion());
            ps.setString(10, user.getPrivilege());
            ps.setInt(11, user.getId());
            ps.setInt(12, user.getId());
            ps.setString(13, user.getName());
            ps.setString(14, user.getSurname());
            ps.setString(15, user.getEmail());
            ps.setString(16, user.getPhone());
            ps.setString(17, user.getAddress());
            ps.setString(18, user.getLogin());
            ps.setString(19, user.getPassword());
            ps.setInt(20, user.getVersion());
            ps.setString(21, user.getPrivilege());
            System.out.println("refer");
            ps.executeQuery();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBUserStorage.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBUserStorage.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    @Override
    public User getUserById(int id) {
        User user = null;
        try {
            connection = DBConnection.getInstance().getDataSourse().getConnection();

            String sql = "SELECT *FROM USER_ WHERE USER_ID=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            user = getUserByConditions(ps);
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBUserStorage.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBUserStorage.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return user;
    }

    @Override
    public User getUserByLogin(String login) {
        User user = null;
        try {
            connection = DBConnection.getInstance().getDataSourse().getConnection();
            String sql = "SELECT *FROM USER_ WHERE login=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, login);
            user = getUserByConditions(ps);
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBUserStorage.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBUserStorage.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return user;
    }

    private User getUserByConditions(PreparedStatement ps) {
        User user = null;
        try {
            connection = DBConnection.getInstance().getDataSourse().getConnection();
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                user = new User();
                user.setId(rs.getInt("USER_ID"));
                user.setName(rs.getString("USER_NAME"));
                user.setSurname(rs.getString("USER_SURNAME"));
                user.setEmail(rs.getString("EMAIL"));
                user.setPhone(rs.getString("PHONE"));
                user.setAddress(rs.getString("ADDRESS"));
                user.setLogin(rs.getString("LOGIN"));
                user.setPassword(rs.getString("PASSWORD"));
                user.setVersion(rs.getInt("VERSION"));
                user.setPrivilege(rs.getString("PRIVILEGE"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBUserStorage.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBUserStorage.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return user;
    }
}
