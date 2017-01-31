package classes.processors.impl;

import classes.exceptions.TransmittedException;
import classes.model.ServiceParams;
import classes.model.ServiceStatus;
import classes.model.behavior.managers.ServiceManager;
import classes.pessimisticLock.PessimisticLockingThread;
import classes.processors.Initializer;
import classes.processors.RequestProcessor;
import classes.request.RequestDTO;
import classes.request.impl.TransmittedServiceParams;
import classes.response.ResponseDTO;
import classes.response.impl.ServiceResponse;

import java.io.Serializable;
import java.util.Date;

public class ChangeServiceProcessor implements RequestProcessor, Serializable {

    private Initializer initializer;

    public ChangeServiceProcessor() {

    }

    public void setInitializer(Initializer initializer) {
        this.initializer = initializer;
    }

    private void changeService(int id, String name, String description, ServiceStatus serviceStatus, int version) {
        ServiceManager serviceManager = initializer.getServiceManager();
        ServiceParams serviceParams = ServiceParams.create()
                .withName(name)
                .withDescription(description)
                .withStatus(serviceStatus)
                .withVersion(version);
        serviceManager.changeService(id, serviceParams);

    }

    private ServiceResponse getResponse(TransmittedServiceParams serviceParams) {
        changeService(serviceParams.getServiceId(),
                serviceParams.getName(), serviceParams.getDescription(), serviceParams.getServiceStatus(),
                serviceParams.getVersion());
        System.out.println("Изменение услуги с Id " + serviceParams.getServiceId());
        return ServiceResponse.create().withResponseType("services").
                withServices(initializer.getServiceManager().getAllServices());
    }

    @Override
    public ResponseDTO process(RequestDTO request) {
        try {
            TransmittedServiceParams serviceParams = (TransmittedServiceParams) request;
            if (initializer.getTypeOfLock().equals("optimistic")) {
                if ((initializer.getServiceManager()
                        .getServiceById(serviceParams.getServiceId()) != null)
                        && (initializer.getServiceManager().getServiceById(serviceParams.getServiceId())
                        .getVersion() == serviceParams.getVersion())) {
                    return getResponse(serviceParams);
                }
            } else {
                if (serviceParams.getUnlockingTime() > new Date().getTime()) {
                    ServiceResponse result = getResponse(serviceParams);
                    PessimisticLockingThread.unschedule(serviceParams.getServiceId());
                    return result;
                }
                else {
                    return TransmittedException.create("НЕВОЗМОЖНО ИЗМЕНИТЬ ДАННЫЕ! ИСТЕКЛО ВРЕМЯ ОЖИДАНИЯ ЗАПРОСА!").withExceptionType("exception");
                }
            }
        } catch (Exception ex) {
            System.out.println("Exception occured!");
            StackTraceElement[] stackTraceElements = ex.getStackTrace();
            for (int i = stackTraceElements.length - 1; i >= 0; i--) {
                System.out.println(stackTraceElements[i].toString());
            }
            return TransmittedException.create("ОШИБКА 404!").withExceptionType("exception");
        }
        return TransmittedException.create("НЕВОЗМОЖНО ИЗМЕНИТЬ ДАННЫЕ!").withExceptionType("exception");
    }

}