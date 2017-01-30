package classes.processors.impl;

import classes.exceptions.TransmittedException;
import classes.processors.RequestProcessor;
import classes.request.RequestDTO;
import classes.response.ResponseDTO;
import classes.request.impl.TransmittedUserParams;
import classes.response.impl.UserResponse;
import classes.processors.Initializer;
import classes.model.User;
import classes.model.behavior.managers.UserManager;
import java.io.Serializable;

public class LogInUserProcessor implements RequestProcessor, Serializable {

    Initializer initializer;

    public LogInUserProcessor() {

    }

    public void setInitializer(Initializer initializer) {
        this.initializer = initializer;
    }

   private User logIn(String login, String password) {
        UserManager userManager = initializer.getUserManager();
        User user = userManager.getUser(login, password);
        return user;
    }

    @Override
    public ResponseDTO process(RequestDTO request) {
        try {
            TransmittedUserParams userRequestParams = (TransmittedUserParams) request;
            System.out.println("Попытка входа в учетную запись с логином " + userRequestParams.getLogin() + " и паролем " + userRequestParams.getPassword());
            User user = logIn(userRequestParams.getLogin(), userRequestParams.getPassword());
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
                        .withVersion(user.getVersion())
                        .withPrivilege(user.getPrivilege());
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
