package classes.processors.impl;

import classes.exceptions.TransmittedException;
import classes.processors.RequestProcessor;
import classes.response.impl.ActiveServiceResponse;
import classes.request.RequestDTO;
import classes.response.ResponseDTO;
import classes.request.impl.TransmittedActiveServiceParams;
import classes.processors.Initializer;
import java.io.Serializable;

public class GetAllActiveServicesProcessor implements RequestProcessor, Serializable {

    private Initializer initializer;

    public GetAllActiveServicesProcessor() {

    }

    public void setInitializer(Initializer initializer) {
        this.initializer = initializer;
    }

    @Override
    public ResponseDTO process(RequestDTO request) {
        try {
            TransmittedActiveServiceParams activeServicesRequestParams
                    = (TransmittedActiveServiceParams) request;
            System.out.println("Получение подключенных услуг пользователя с Id "
                    + activeServicesRequestParams.getUserId());
            return ActiveServiceResponse.create().withActiveServices(initializer.getActiveServiceManager()
                    .getActiveServicesByUserId(activeServicesRequestParams.getUserId()))
                    .withResponseType("activeServices");
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
