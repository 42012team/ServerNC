package classes.model.behavior.managers;

import classes.idgenerator.IdGenerator;
import classes.model.behavior.storages.UserStorage;
import classes.model.User;
import classes.model.UserParams;

public class UserManager {

    UserStorage userStorage;
    IdGenerator idGenerator;

    public UserManager(UserStorage userStorage, IdGenerator idGenerator) {
        this.userStorage = userStorage;
        this.idGenerator = idGenerator;
    }

    public User createUser(UserParams userParams) {
        User user = new User();
        user.setId(idGenerator.generateId());
        user.setName(userParams.getName());
        user.setSurname(userParams.getSurname());
        user.setEmail(userParams.getEmail());
        user.setPhone(userParams.getPhone());
        user.setAddress(userParams.getAddress());
        user.setLogin(userParams.getLogin());
        user.setPassword(userParams.getPassword());
        user.setVersion(userParams.getVersion());
        user.setPrivilege(userParams.getPrivilege());
        storeUser(user);
        return user;
    }

    public void changeUser(User user, UserParams userParams) {
        user.setName(userParams.getName());
        user.setSurname(userParams.getSurname());
        user.setEmail(userParams.getEmail());
        user.setPhone(userParams.getPhone());
        user.setAddress(userParams.getAddress());
        user.setLogin(userParams.getLogin());
        user.setPassword(userParams.getPassword());
        user.setVersion(userParams.getVersion());
        storeUser(user);
    }

    public User getUserByLogin(String login) {
        return userStorage.getUserByLogin(login);
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);

    }

    public User getUser(String login, String password) {
        return userStorage.getUser(login, password);
    }

    private void storeUser(User user) {
        userStorage.storeUser(user);
    }
}
