package classes.processors.impl;

import classes.pessimisticLock.PessimisticLockingThread;
import classes.exceptions.TransmittedException;
import classes.processors.RequestProcessor;
import classes.request.RequestDTO;
import classes.response.ResponseDTO;
import classes.request.impl.TransmittedUserParams;
import classes.response.impl.UserResponse;
import classes.processors.Initializer;
import classes.model.User;
import classes.model.UserParams;
import classes.model.behavior.managers.UserManager;
import java.io.Serializable;

public class ChangeUserProcessor implements RequestProcessor, Serializable {

    Initializer initializer;

    public ChangeUserProcessor() {

    }

    public void setInitializer(Initializer initializer) {
        this.initializer = initializer;
    }

    private User changeUser(int id, String name, String surname, String email, String phone, String address,
            String login, String password, int version) {
        UserManager userManager = initializer.getUserManager();
        UserParams userParams = UserParams.create()
                .withName(name)
                .withSurname(surname)
                .withEmail(email)
                .withPhone(phone)
                .withAdress(address)
                .withLogin(login)
                .withPassword(password)
                .withVersion(version + 1);
        User user = userManager.getUserById(id);
        userManager.changeUser(user, userParams);
        return user;
    }

    public UserResponse getResponse(TransmittedUserParams userRequestParams) {
        User user = changeUser(userRequestParams.getUserId(), userRequestParams.getName(), userRequestParams.getSurname(), userRequestParams.getEmail(), userRequestParams.getPhone(),
                userRequestParams.getAddress(), userRequestParams.getLogin(), userRequestParams.getPassword(), userRequestParams.getVersion());
        System.out.println("Изменение пользователя с Id " + user.getId());
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
                .withPrivilege(user.getPrivilege())
                .withResponseType("user");
    }

    @Override
    public ResponseDTO process(RequestDTO request) {
        try {
            TransmittedUserParams userRequestParams = (TransmittedUserParams) request;
            if (initializer.getTypeOfLock().equals("optimistic")) {
                User userById = initializer.getUserManager().getUserById(userRequestParams.getUserId());
                if (userById.getVersion() == userRequestParams.getVersion()) {
                    synchronized (this) {
                        if (userById.getVersion() == userRequestParams.getVersion()) {
                            return getResponse(userRequestParams);
                        }
                    }
                }
            } else {
                UserResponse userResponse = getResponse(userRequestParams);
                PessimisticLockingThread.unschedule(userResponse.getUserId());
                return userResponse;
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
