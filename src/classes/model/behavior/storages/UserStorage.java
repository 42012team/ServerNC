package classes.model.behavior.storages;

import classes.model.User;

public interface UserStorage {

    public User getUser(String login, String password);

    public void storeUser(User user);

    public User getUserById(int id);

    public User getUserByLogin(String login);
}
