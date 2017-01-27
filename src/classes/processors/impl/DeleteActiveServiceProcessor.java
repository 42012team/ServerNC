package classes.processors.impl;

import classes.pessimisticLock.PessimisticLockingThread;
import classes.exceptions.TransmittedException;
import classes.processors.RequestProcessor;
import classes.response.impl.ActiveServiceResponse;
import classes.request.RequestDTO;
import classes.response.ResponseDTO;
import classes.request.impl.TransmittedActiveServiceParams;
import classes.processors.Initializer;
import classes.model.behavior.managers.ActiveServiceManager;
import java.io.Serializable;

public class DeleteActiveServiceProcessor implements RequestProcessor, Serializable {

    private Initializer initializer;

    public DeleteActiveServiceProcessor() {

    }

    public void setInitializer(Initializer initializer) {
        this.initializer = initializer;
    }

    @Override
    public ResponseDTO process(RequestDTO request) {
        try {
            TransmittedActiveServiceParams activeServiceParams = (TransmittedActiveServiceParams) request;
            if (initializer.getTypeOfLock().equals("optimistic")) {
                if (initializer.getActiveServiceManager()
                        .getActiveServiceById(activeServiceParams.getId()) != null) {
                    ActiveServiceManager activeServiceManager = initializer.getActiveServiceManager();
                    System.out.println("Удаление подключенной услуги с Id " + activeServiceParams.getId());
                    activeServiceManager.deleteActiveService(activeServiceParams.getId());
                    return ActiveServiceResponse.create().withResponseType("activeServices").withActiveServices(initializer.getActiveServiceManager().getActiveServicesByUserId(activeServiceParams.getUserId()));
                }
            } else {
                ActiveServiceManager activeServiceManager = initializer.getActiveServiceManager();
                System.out.println("Удаление подключенной услуги с Id " + activeServiceParams.getId());
                activeServiceManager.deleteActiveService(activeServiceParams.getId());
                PessimisticLockingThread.unschedule(activeServiceParams.getId());
                return ActiveServiceResponse.create().withResponseType("activeServices").withActiveServices(initializer.getActiveServiceManager().getActiveServicesByUserId(activeServiceParams.getUserId()));
            }
        } catch (Exception ex) {
            System.out.println("Exception occured!");
            StackTraceElement[] stackTraceElements = ex.getStackTrace();
            for (int i = stackTraceElements.length - 1; i >= 0; i--) {
                System.out.println(stackTraceElements[i].toString());
            }
            return TransmittedException.create("ОШИБКА 404!").withExceptionType("exception");
        }
        return TransmittedException.create("УСЛУГА УЖЕ УДАЛЕНА!").withExceptionType("exception");
    }

}
