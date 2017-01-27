package classes.model.behavior.storages;

import classes.model.Service;
import java.util.List;

public interface ServiceStorage {

    public List<Service> getAllServices();

    public Service getServiceById(int ServiceId);

    public void storeService(Service service);

    public void deleteService(int serviceId);

}
