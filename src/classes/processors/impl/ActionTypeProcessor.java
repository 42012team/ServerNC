package classes.processors.impl;

import classes.pessimisticLock.PessimisticLockingThread;
import classes.processors.Initializer;
import classes.processors.RequestProcessor;
import classes.request.RequestDTO;
import classes.request.impl.TransmittedActionType;
import classes.response.ResponseDTO;
import classes.response.impl.ActionResponse;
import java.io.Serializable;

public class ActionTypeProcessor implements RequestProcessor, Serializable {

    // его надо изменять при выполеннии действия
    private Initializer initializer;

    public ActionTypeProcessor() {

    }

    public void setInitializer(Initializer initializer) {
        this.initializer = initializer;
    }

    @Override
    public ResponseDTO process(RequestDTO request) {
        TransmittedActionType transmittedActionType = (TransmittedActionType) request;
        if (initializer.getTypeOfLock().equals("optimistic")) {
            return ActionResponse.create().withIsAllowed(true)
                    .withResponseType("actionType");
        }
        if (transmittedActionType.isCanceled()) {
            PessimisticLockingThread.unschedule(transmittedActionType.getId());
            return ActionResponse.create().withIsAllowed(false)
                    .withResponseType("actionType");
        }
        if (PessimisticLockingThread.contains(transmittedActionType.getId())) {
            return ActionResponse.create().withIsAllowed(false)
                    .withResponseType("actionType");
        }
        return ActionResponse.create().withIsAllowed(true)
                .withResponseType("actionType")
                .withUnlockingTime(PessimisticLockingThread.schedule(transmittedActionType.getId()));
    }

}