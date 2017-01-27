package classes.processors.impl;

import classes.exceptions.TransmittedException;
import classes.model.User;
import classes.processors.Initializer;
import classes.processors.RequestProcessor;
import classes.request.RequestDTO;
import classes.request.impl.TransmittedUserParams;
import classes.response.ResponseDTO;
import classes.response.impl.UserResponse;
import java.io.Serializable;

public class GetUserByIdProcessor implements RequestProcessor, Serializable {

    Initializer initializer;

    public GetUserByIdProcessor() {

    }

    public void setInitializer(Initializer initializer) {
        this.initializer = initializer;
    }

    @Override
    public ResponseDTO process(RequestDTO request) {
        try {
            TransmittedUserParams userRequestParams = (TransmittedUserParams) request;
            System.out.println("Поиск пользователя по Id " + userRequestParams.getUserId());
            User user = initializer.getUserManager().getUserById(userRequestParams.getUserId());
            UserResponse userr = null;
            if (user != null) {
                userr = UserResponse.create()
                        .withName(user.getName())
                        .withSurname(user.getSurname())
                        .withAdress(user.getAddress())
                        .withEmail(user.getEmail())
                        .withPhone(user.getPhone())
                        .withLogin(user.getLogin())
                        .withPassword(user.getPassword())
                        .withId(user.getId())
                        .withResponseType("user")
                        .withVersion(user.getVersion());
                return userr;
            }
        } catch (Exception ex) {
            System.out.println("Exception occured!");
            StackTraceElement[] stackTraceElements = ex.getStackTrace();
            for (int i = stackTraceElements.length - 1; i >= 0; i--) {
                System.out.println(stackTraceElements[i].toString());
            }
            return TransmittedException.create("ОШИБКА 404!").withExceptionType("exception");
        }
        return TransmittedException.create("ПОЛЬЗОВАТЕЛЯ С ВВЕДЕННЫМИ ДАННЫМИ НЕ СУЩЕСТВУЕТ!").
                withExceptionType("exception");
    }

}
