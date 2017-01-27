package classes.response.impl;

import classes.response.ResponseDTO;
import java.io.Serializable;

public class ActionResponse implements ResponseDTO, Serializable {

    private String responseType;
    private boolean isAllowed;

    private ActionResponse() {

    }

    public static ActionResponse create() {
        return new ActionResponse();
    }

    public ActionResponse withResponseType(String responseType) {
        this.responseType = responseType;
        return this;
    }

    public ActionResponse withIsAllowed(boolean isAllowed) {
        this.isAllowed = isAllowed;
        return this;
    }

    @Override
    public String getResponseType() {
        return responseType;
    }

    public boolean isAllowed() {
        return isAllowed;
    }

}
