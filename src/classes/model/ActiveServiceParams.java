package classes.model;

import java.util.Date;

public class ActiveServiceParams {

    private int serviceId;
    private int userId;
    private ActiveServiceStatus currentStatus;
    private ActiveServiceStatus newStatus;
    private Date date;
    private int version;

    private ActiveServiceParams() {

    }

    public static ActiveServiceParams create() {
        return new ActiveServiceParams();
    }

    public ActiveServiceParams withServiceId(int serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    public ActiveServiceParams withUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public ActiveServiceParams withCurrentStatus(ActiveServiceStatus currentStatus) {
        this.currentStatus = currentStatus;
        return this;
    }

    public ActiveServiceParams withNewStatus(ActiveServiceStatus newStatus) {
        this.newStatus = newStatus;
        return this;
    }

    public ActiveServiceParams withDate(Date date) {
        this.date = date;
        return this;
    }

    public ActiveServiceParams withVersion(int version) {
        this.version = version;
        return this;
    }

    public ActiveServiceStatus getCurrentStatus() {
        return currentStatus;
    }

    public ActiveServiceStatus getNewStatus() {
        return newStatus;
    }

    public Date getDate() {
        return date;
    }

    public int getServiceId() {
        return serviceId;
    }

    public int getUserId() {
        return userId;
    }

    public int getVersion() {
        return version;
    }

}
