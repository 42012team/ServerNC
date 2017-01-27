package classes.model.behavior.managers;

import classes.idgenerator.IdGenerator;
import classes.model.ActiveService;
import classes.model.behavior.storages.ServiceStorage;
import classes.model.ServiceStatus;
import classes.model.Service;
import classes.model.ServiceParams;
import java.util.ArrayList;
import java.util.List;

public class ServiceManager {

    ServiceStorage serviceStorage;
    IdGenerator idGenerator;
    ActiveServiceManager activeServiceManager;

    public ServiceManager(ServiceStorage serviceStorage, IdGenerator idGenerator) {
        this.serviceStorage = serviceStorage;
        this.idGenerator = idGenerator;

    }

    public void setActiveServiceManager(ActiveServiceManager activeServiceManager) {
        this.activeServiceManager = activeServiceManager;
    }

    public Service createService(ServiceParams serviceParams) {
        Service service = new Service();
        service.setId(idGenerator.generateId());
        service.setName(serviceParams.getName());
        service.setDescription(serviceParams.getDescription());
        service.setStatus(serviceParams.getStatus());
        service.setType(serviceParams.getType());
        service.setVersion(0);
        storeService(service);
        return service;
    }

    public void changeService(int serviceId, ServiceParams serviceParams) {
        Service service = getServiceById(serviceId);
        service.setName(serviceParams.getName());
        service.setDescription(serviceParams.getDescription());
        service.setStatus(serviceParams.getStatus());
        service.setVersion(serviceParams.getVersion() + 1);
        storeService(service);

    }

    public List<Service> getAllServices() {
        return serviceStorage.getAllServices();
    }

    public List<Service> getAllowedToConnectServices(List<Service> activeServicesDescriptions) {
        List<Service> allServices = serviceStorage.getAllServices();
        List<Service> allowedServices = new ArrayList<Service>();
        for (Service service : allServices) {
            if (service.getStatus() != ServiceStatus.DEPRECATED) {
                boolean notFound = true;

                for (Service activeServiceDescription : activeServicesDescriptions) {
                    if (service.getType().equals(activeServiceDescription.getType())) {
                        notFound = false;
                        break;
                    }
                }
                if (notFound) {
                    allowedServices.add(service);
                }
            }
        }
        return allowedServices;
    }

    public Service getServiceById(int serviceId) {
        return serviceStorage.getServiceById(serviceId);
    }

    public boolean deleteService(int serviceId) {
        boolean isDeleted = false;
        List<ActiveService> activeServices = activeServiceManager.getAllActiveServices();
        boolean isConnecting = false;
        for (ActiveService activeService : activeServices) {
            if (activeService.getServiceId() == serviceId) {
                isConnecting = true;
                break;
            }
        }
        if (!isConnecting) {
            serviceStorage.deleteService(serviceId);
            isDeleted = true;
        } else {
            Service service = getServiceById(serviceId);
            ServiceParams serviceParams = ServiceParams.create()
                    .withName(service.getName())
                    .withDescription(service.getDescription())
                    .withType(service.getType())
                    .withStatus(ServiceStatus.DEPRECATED);
            changeService(serviceId, serviceParams);
        }
        return isDeleted;
    }

    private void storeService(Service service) {
        serviceStorage.storeService(service);
    }

    public List<Service> getActiveServicesDecriptions(List<ActiveService> activeServices) {
        List<Service> allServices = getAllServices();
        List<Service> descriptions = new ArrayList<Service>();
        for (ActiveService activeService : activeServices) {
            for (Service service : allServices) {
                if (activeService.getServiceId() == service.getId()) {
                    descriptions.add(service);
                    break;
                }
            }
        }
        return descriptions;
    }

}
