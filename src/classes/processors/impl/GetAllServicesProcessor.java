package classes.processors.impl;

import classes.exceptions.TransmittedException;
import classes.processors.RequestProcessor;
import classes.response.impl.ServiceResponse;
import classes.request.RequestDTO;
import classes.response.ResponseDTO;
import classes.processors.Initializer;
import classes.model.Service;
import java.io.Serializable;
import java.util.List;

public class GetAllServicesProcessor implements RequestProcessor, Serializable {

    private Initializer initializer;

    public GetAllServicesProcessor() {

    }

    public void setInitializer(Initializer initializer) {
        this.initializer = initializer;
    }

    @Override
    public ResponseDTO process(RequestDTO request) {
        try {
            List<Service> servicesList = initializer.getServiceManager().getAllServices();
            System.out.println("Возврат всех сервисов");
            return ServiceResponse.create()
                    .withServices(servicesList)
                    .withResponseType("services");
        } catch (Exception ex) {
            System.out.println("Exception occured!");
            StackTraceElement[] stackTraceElements = ex.getStackTrace();
            for (int i = stackTraceElements.length - 1; i >= 0; i--) {
                System.out.println(stackTraceElements[i].toString());
            }
            return TransmittedException.create("ОШИБКА 404!").withExceptionType("exception");
        }
    }

}
