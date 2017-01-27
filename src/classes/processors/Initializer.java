package classes.processors;

import classes.model.behavior.managers.ActiveServiceManager;
import classes.model.behavior.managers.ServiceManager;
import classes.model.behavior.managers.UserManager;

public class Initializer {

    private UserManager userManager;
    private ActiveServiceManager activeServiceManager;
    private ServiceManager serviceManager;
    private String typeOfLock;

    public Initializer(UserManager userManager, ActiveServiceManager activeServiceManager, ServiceManager serviceManager, String typeOfLock) {
        this.userManager = userManager;
        this.activeServiceManager = activeServiceManager;
        this.serviceManager = serviceManager;
        this.typeOfLock = typeOfLock;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public ServiceManager getServiceManager() {
        return serviceManager;
    }

    public ActiveServiceManager getActiveServiceManager() {
        return activeServiceManager;
    }

    public String getTypeOfLock() {
        return typeOfLock;
    }

}
