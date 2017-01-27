package classes.model;

public class UserParams {

    private String name;
    private String surname;
    private String email;
    private String phone;
    private String address;
    private String login;
    private String password;
    private int version;
    private String privilege;

    private UserParams() {
    }

    public static UserParams create() {
        return new UserParams();
    }

    public UserParams withName(String name) {
        this.name = name;
        return this;
    }

    public UserParams withSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public UserParams withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserParams withPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public UserParams withAdress(String address) {
        this.address = address;
        return this;
    }

    public UserParams withLogin(String login) {
        this.login = login;
        return this;
    }

    public UserParams withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserParams withVersion(int version) {
        this.version = version;
        return this;
    }

    public UserParams withPrivilege(String privilege) {
        this.privilege = privilege;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public int getVersion() {
        return version;
    }

    public String getPrivilege() {
        return privilege;
    }
}
