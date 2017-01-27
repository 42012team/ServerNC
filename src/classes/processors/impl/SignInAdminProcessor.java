package classes.processors.impl;

import classes.exceptions.TransmittedException;
import classes.model.User;
import classes.model.UserParams;
import classes.model.behavior.managers.UserManager;
import classes.processors.Initializer;
import classes.processors.RequestProcessor;
import classes.request.RequestDTO;
import classes.request.impl.TransmittedUserParams;
import classes.response.ResponseDTO;
import classes.response.impl.UserResponse;
import java.io.Serializable;

public class SignInAdminProcessor implements RequestProcessor, Serializable {

    Initializer initializer;

    public SignInAdminProcessor() {

    }

    public void setInitializer(Initializer initializer) {
        this.initializer = initializer;
    }

    private User signIn(String login, String password, String privilege) {
        UserManager userManager = initializer.getUserManager();
        UserParams userParams = UserParams.create()
                .withLogin(login)
                .withPassword(password)
                .withPrivilege(privilege)
                .withVersion(0)
                .withAdress("")
                .withEmail("")
                .withName("")
                .withPhone("")
                .withSurname("");
        User user = userManager.createUser(userParams);
        return user;
    }

    @Override
    public ResponseDTO process(RequestDTO request) {
        try {
            TransmittedUserParams userRequestParams = (TransmittedUserParams) request;
            System.out.println("Создание администратора с параметрами " + userRequestParams.getLogin()
                    + " " + userRequestParams.getPassword() + userRequestParams.getPrivilege());
            if (initializer.getUserManager().getUserByLogin(userRequestParams.getLogin()) == null) {
                User user = signIn(userRequestParams.getLogin(), userRequestParams.getPassword(), userRequestParams.getPrivilege());
                System.out.println("Присвоенный Id:" + user.getId());
                return UserResponse.create()
                        .withName(user.getName())
                        .withSurname(user.getSurname())
                        .withAdress(user.getAddress())
                        .withEmail(user.getEmail())
                        .withPhone(user.getPhone())
                        .withLogin(user.getLogin())
                        .withPassword(user.getPassword())
                        .withId(user.getId())
                        .withVersion(user.getVersion())
                        .withResponseType("user");
            }
        } catch (Exception ex) {
            System.out.println("Exception occured!");
            StackTraceElement[] stackTraceElements = ex.getStackTrace();
            for (int i = stackTraceElements.length - 1; i >= 0; i--) {
                System.out.println(stackTraceElements[i].toString());
            }
            return TransmittedException.create("ОШИБКА 404!").withExceptionType("exception");
        }
        return TransmittedException.create("ПОЛЬЗОВАТЕЛЬ С ТАКИМ ЛОГИНОМ УЖЕ СУЩЕСТВУЕТ!").withExceptionType("exception");
    }

}
