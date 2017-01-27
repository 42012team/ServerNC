package classes.processors;

import classes.request.RequestDTO;
import classes.response.ResponseDTO;

public interface RequestProcessor {

    public ResponseDTO process(RequestDTO request);

}
