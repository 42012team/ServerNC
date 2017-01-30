package classes.model.behavior.storages.impl;

import classes.db.DBConnection;
import classes.model.Service;
import classes.model.ServiceStatus;
import classes.model.behavior.storages.ServiceStorage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBServiceStorage implements ServiceStorage {

    private Connection connection;

    @Override
    public List<Service> getAllServices() {
        List<Service> servicesList = null;
        try {
            servicesList = new ArrayList<Service>();
            connection = DBConnection.getInstance().getDataSourse().getConnection();
            String sql = "SELECT *FROM SERVICE";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Service service = new Service();
                service.setId(rs.getInt("SERVICE_ID"));
                service.setName(rs.getString("SERVICE_NAME"));
                service.setDescription(rs.getString("DESCRIPTION"));
                service.setVersion(rs.getInt("VERSION"));
                service.setType(rs.getString("SERVICE_TYPE"));
                String status = rs.getString("SERVICE_STATUS");
                switch (status) {
                    case "ALLOWED":
                        service.setStatus(ServiceStatus.ALLOWED);
                        break;
                    case "DEPRECATED":
                        service.setStatus(ServiceStatus.DEPRECATED);
                        break;
                }
                servicesList.add(service);

            }
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBServiceStorage.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBUserStorage.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return servicesList;
    }

    @Override
    public Service getServiceById(int serviceId) {
        Service service = null;
        try {
            connection = DBConnection.getInstance().getDataSourse().getConnection();
            String sql = "SELECT *FROM SERVICE WHERE SERVICE_ID=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, serviceId);
            service = getServiceByConditions(ps);
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

        return service;
    }

    @Override
    public void storeService(Service service) {
        try {
            connection = DBConnection.getInstance().getDataSourse().getConnection();
            String mergeSql = "MERGE INTO SERVICE S USING("
                    + "SELECT *FROM dual)"
                    + " ON((select count(*) from SERVICE where SERVICE_ID=?)!=0)"
                    + " WHEN MATCHED THEN "
                    + " UPDATE SERVICE_NAME = ?,DESCRIPTION=?,VERSION=?, SERVICE_TYPE=?, "
                    + "SERVICE_STATUS=? WHERE SERVICE_ID = ? "
                    + " WHEN NOT MATCHED THEN "
                    + "INSERT VALUES(?,?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(mergeSql);
            ps.setInt(1, service.getId());
            ps.setString(2, service.getName());
            ps.setString(3, service.getDescription());
            ps.setInt(4, service.getVersion());
            ps.setString(5, service.getType());
            ps.setString(6, service.getStatus().toString());
            ps.setInt(7, service.getId());
            ps.setInt(8, service.getId());
            ps.setString(9, service.getDescription());
            ps.setString(10, service.getName());
            ps.setString(11, service.getStatus().toString());
            ps.setString(12, service.getType());
            ps.setInt(13, service.getVersion());

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
    public void deleteService(int serviceId) {
        try {
            connection
                    = DBConnection.getInstance().getDataSourse().getConnection();
            String sql = "DELETE FROM SERVICE WHERE SERVICE_ID=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, serviceId);
            ps.executeQuery();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBServiceStorage.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBUserStorage.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    private Service getServiceByConditions(PreparedStatement ps) {
        Service service = null;
        try {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                service = new Service();
                service.setId(rs.getInt("SERVICE_ID"));
                service.setName(rs.getString("SERVICE_NAME"));
                service.setDescription(rs.getString("DESCRIPTION"));
                service.setVersion(rs.getInt("VERSION"));
                service.setType(rs.getString("SERVICE_TYPE"));
                String status = rs.getString("SERVICE_STATUS");
                switch (status) {
                    case "ALLOWED":
                        service.setStatus(ServiceStatus.ALLOWED);
                        break;
                    case "DEPRECATED":
                        service.setStatus(ServiceStatus.DEPRECATED);
                        break;
                }

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
        return service;
    }
}
