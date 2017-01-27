package classes.model.behavior.storages;

import classes.model.ActiveService;
import java.util.List;

public interface ActiveServiceStorage {

    public List<ActiveService> getActiveServicesByUserId(int userId);

    public void deleteActiveService(int activeServiceId);

    public List<ActiveService> getAllActiveServices();

    public ActiveService getActiveServiceById(int activeServiceId);

    public void storeActiveServices(List<ActiveService> activeServicesList);

}
