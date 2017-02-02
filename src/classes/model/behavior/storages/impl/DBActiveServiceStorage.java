package classes.model.behavior.storages.impl;

import classes.db.DBConnection;
import classes.model.ActiveService;
import classes.model.ActiveServiceStatus;
import classes.model.behavior.storages.ActiveServiceStorage;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBActiveServiceStorage implements ActiveServiceStorage {

    private Connection connection = null;

    @Override
    public List<ActiveService> getActiveServicesByUserId(int userId) {
        List<ActiveService> activeServiceList = null;
        try {
            connection = DBConnection.getInstance().getDataSourse().getConnection();
            String sql = "SELECT *FROM ACTIVESERVICE WHERE USER_ID=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            int i = 0;
            activeServiceList = new ArrayList<ActiveService>();
            while (rs.next()) {
                i++;
                ActiveService activeService = new ActiveService();
                activeService.setUserId(rs.getInt("USER_ID"));
                activeService.setServiceId(rs.getInt("SERVICE_ID"));
                activeService.setId(rs.getInt("ACTIVESERVICE_ID"));
                activeService.setVersion(rs.getInt("VERSION"));
                String currentStatus = rs.getString("CURRENT_STATUS");
                switch (currentStatus) {
                    case "ACTIVE":
                        activeService.setCurrentStatus(ActiveServiceStatus.ACTIVE);
                        break;
                    case "PLANNED":
                        activeService.setCurrentStatus(ActiveServiceStatus.PLANNED);
                        break;
                    case "SUSPENDED":
                        activeService.setCurrentStatus(ActiveServiceStatus.SUSPENDED);
                        break;
                    case "DISCONNECTED":
                        activeService.setCurrentStatus(ActiveServiceStatus.DISCONNECTED);
                        break;
                }
                String newStatus = rs.getString("NEW_STATUS");
                switch (newStatus) {
                    case "ACTIVE":
                        activeService.setNewStatus(ActiveServiceStatus.ACTIVE);
                        break;
                    case "PLANNED":
                        activeService.setNewStatus(ActiveServiceStatus.PLANNED);
                        break;
                    case "SUSPENDED":
                        activeService.setNewStatus(ActiveServiceStatus.SUSPENDED);
                        break;
                    case "DISCONNECTED":
                        activeService.setNewStatus(ActiveServiceStatus.DISCONNECTED);
                        break;
                    case " ":
                        activeService.setNewStatus(null);
                        break;
                }
                activeService.setDate(new Date(rs.getTimestamp("TDATE").getTime()));
                activeServiceList.add(activeService);
            }
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Exception occured!");
            StackTraceElement[] stackTraceElements = ex.getStackTrace();
            for (int i = stackTraceElements.length - 1; i >= 0; i--) {
                System.out.println(stackTraceElements[i].toString());
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                System.out.println("Exception occured!");
                StackTraceElement[] stackTraceElements = ex.getStackTrace();
                for (int i = stackTraceElements.length - 1; i >= 0; i--) {
                    System.out.println(stackTraceElements[i].toString());
                }
            }
        }
        return activeServiceList;
    }

    @Override
    public void deleteActiveService(int activeServiceId) {
        try {
            connection = DBConnection.getInstance().getDataSourse().getConnection();
            String sql = "DELETE FROM ACTIVESERVICE WHERE ACTIVESERVICE_ID=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, activeServiceId);
            ps.executeQuery();
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Exception occured!");
            StackTraceElement[] stackTraceElements = ex.getStackTrace();
            for (int i = stackTraceElements.length - 1; i >= 0; i--) {
                System.out.println(stackTraceElements[i].toString());
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                System.out.println("Exception occured!");
                StackTraceElement[] stackTraceElements = ex.getStackTrace();
                for (int i = stackTraceElements.length - 1; i >= 0; i--) {
                    System.out.println(stackTraceElements[i].toString());
                }
            }
        }
    }

    @Override
    public List<ActiveService> getAllActiveServices() {
        List<ActiveService> activeServiceList = null;
        try {
            connection = DBConnection.getInstance().getDataSourse().getConnection();
            String sql = "SELECT *FROM ACTIVESERVICE ";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            activeServiceList = new ArrayList<ActiveService>();
            while (rs.next()) {
                ActiveService activeService = new ActiveService();
                activeService.setUserId(rs.getInt("USER_ID"));
                activeService.setServiceId(rs.getInt("SERVICE_ID"));
                activeService.setId(rs.getInt("ACTIVESERVICE_ID"));
                activeService.setVersion(rs.getInt("VERSION"));
                String currentStatus = rs.getString("CURRENT_STATUS");
                switch (currentStatus) {
                    case "ACTIVE":
                        activeService.setCurrentStatus(ActiveServiceStatus.ACTIVE);
                        break;
                    case "PLANNED":
                        activeService.setCurrentStatus(ActiveServiceStatus.PLANNED);
                        break;
                    case "SUSPENDED":
                        activeService.setCurrentStatus(ActiveServiceStatus.SUSPENDED);
                        break;
                    case "DISCONNECTED":
                        activeService.setCurrentStatus(ActiveServiceStatus.DISCONNECTED);
                        break;
                }
                String newStatus = rs.getString("NEW_STATUS");
                switch (newStatus) {
                    case "ACTIVE":
                        activeService.setNewStatus(ActiveServiceStatus.ACTIVE);
                        break;
                    case "PLANNED":
                        activeService.setNewStatus(ActiveServiceStatus.PLANNED);
                        break;
                    case "SUSPENDED":
                        activeService.setNewStatus(ActiveServiceStatus.SUSPENDED);
                        break;
                    case "DISCONNECTED":
                        activeService.setNewStatus(ActiveServiceStatus.DISCONNECTED);
                        break;
                    case " ":
                        activeService.setNewStatus(null);
                        break;
                }
                activeService.setDate(new Date(rs.getTimestamp("TDATE").getTime()));
                activeServiceList.add(activeService);
            }
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Exception occured!");
            StackTraceElement[] stackTraceElements = ex.getStackTrace();
            for (int i = stackTraceElements.length - 1; i >= 0; i--) {
                System.out.println(stackTraceElements[i].toString());
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                System.out.println("Exception occured!");
                StackTraceElement[] stackTraceElements = ex.getStackTrace();
                for (int i = stackTraceElements.length - 1; i >= 0; i--) {
                    System.out.println(stackTraceElements[i].toString());
                }
            }
        }
        return activeServiceList;
    }

    @Override
    public ActiveService getActiveServiceById(int activeServiceId) {
        ActiveService activeService = null;
        try {
            connection = DBConnection.getInstance().getDataSourse().getConnection();
            String sql = "SELECT *FROM ACTIVESERVICE WHERE ACTIVESERVICE_ID=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, activeServiceId);
            ResultSet rs = ps.executeQuery();
            activeService = new ActiveService();
            while (rs.next()) {
                activeService.setUserId(rs.getInt("USER_ID"));
                activeService.setServiceId(rs.getInt("SERVICE_ID"));
                activeService.setId(rs.getInt("ACTIVESERVICE_ID"));
                activeService.setVersion(rs.getInt("VERSION"));
                String currentStatus = rs.getString("CURRENT_STATUS");
                switch (currentStatus) {
                    case "ACTIVE":
                        activeService.setCurrentStatus(ActiveServiceStatus.ACTIVE);
                        break;
                    case "PLANNED":
                        activeService.setCurrentStatus(ActiveServiceStatus.PLANNED);
                        break;
                    case "SUSPENDED":
                        activeService.setCurrentStatus(ActiveServiceStatus.SUSPENDED);
                        break;
                    case "DISCONNECTED":
                        activeService.setCurrentStatus(ActiveServiceStatus.DISCONNECTED);
                        break;
                }
                String newStatus = rs.getString("NEW_STATUS");
                switch (newStatus) {
                    case "ACTIVE":
                        activeService.setNewStatus(ActiveServiceStatus.ACTIVE);
                        break;
                    case "PLANNED":
                        activeService.setNewStatus(ActiveServiceStatus.PLANNED);
                        break;
                    case "SUSPENDED":
                        activeService.setNewStatus(ActiveServiceStatus.SUSPENDED);
                        break;
                    case "DISCONNECTED":
                        activeService.setNewStatus(ActiveServiceStatus.DISCONNECTED);
                        break;
                    case " ":
                        activeService.setNewStatus(null);
                        break;
                }
                activeService.setDate(new Date(rs.getTimestamp("TDATE").getTime()));
            }
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBUserStorage.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                System.out.println("Exception occured!");
                StackTraceElement[] stackTraceElements = ex.getStackTrace();
                for (int i = stackTraceElements.length - 1; i >= 0; i--) {
                    System.out.println(stackTraceElements[i].toString());
                }
            }
        }
        return activeService;
    }

    @Override
    public void storeActiveServices(List<ActiveService> activeServicesList) {
        try {
            PreparedStatement ps = null;
            connection = DBConnection.getInstance().getDataSourse().getConnection();
            for (int i = 0; i < activeServicesList.size(); i++) {
                String mergeSql = "MERGE INTO ACTIVESERVICE A USING("
                        + "SELECT *FROM dual)"
                        + " ON((select count(*) from ACTIVESERVICE where ACTIVESERVICE_ID=?)!=0)"
                        + " WHEN MATCHED THEN "
                        + " UPDATE SET USER_ID=?, SERVICE_ID=?, CURRENT_STATUS=?, "
                        + "NEW_STATUS=?, TDATE=?,VERSION=? WHERE ACTIVESERVICE_ID = ? "
                        + " WHEN NOT MATCHED THEN "
                        + "INSERT VALUES(?,?,?,?,?,?,?)";

                ps = connection.prepareStatement(mergeSql);
                ps.setInt(1, activeServicesList.get(i).getId());
                ps.setInt(2, activeServicesList.get(i).getUserId());
                ps.setInt(3, activeServicesList.get(i).getServiceId());
                ps.setString(4, activeServicesList.get(i).getCurrentStatus().toString());
                if (activeServicesList.get(i).getNewStatus() != null) {
                    ps.setString(5, activeServicesList.get(i).getNewStatus().toString());
                } else {
                    ps.setString(5, " ");
                }
                Timestamp timestamp = new Timestamp(activeServicesList.get(i).getDate().getTime());
                ps.setTimestamp(6, timestamp);
                ps.setInt(7, activeServicesList.get(i).getVersion());
                ps.setInt(8, activeServicesList.get(i).getId());
                ps.setInt(9, activeServicesList.get(i).getId());
                ps.setInt(10, activeServicesList.get(i).getUserId());
                ps.setInt(11, activeServicesList.get(i).getServiceId());
                ps.setString(12, activeServicesList.get(i).getCurrentStatus().toString());
                if (activeServicesList.get(i).getNewStatus() != null)
                    ps.setString(13, activeServicesList.get(i).getNewStatus().toString());
                else ps.setString(13, " ");
                ps.setTimestamp(14, timestamp);
                ps.setInt(15, activeServicesList.get(i).getVersion());
                ps.executeQuery();
            }
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Exception occured!");
            StackTraceElement[] stackTraceElements = ex.getStackTrace();
            for (int i = stackTraceElements.length - 1; i >= 0; i--) {
                System.out.println(stackTraceElements[i].toString());
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                System.out.println("Exception occured!");
                StackTraceElement[] stackTraceElements = ex.getStackTrace();
                for (int i = stackTraceElements.length - 1; i >= 0; i--) {
                    System.out.println(stackTraceElements[i].toString());
                }
            }
        }
    }

}
