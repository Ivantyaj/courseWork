package Users;

public class User {
    enum Role {
        ADMIN,
        USER,
        FAIL
    }

    Role role = Role.FAIL;
    String login;
    String password;

    public User() {
    }

    public User(Role role, String login, String password) {
        this.role = role;
        this.login = login;
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setRole(String role) {
        switch (role){
            case "ADMIN":
                setRole(Role.ADMIN);
                break;
            case "USER":
                setRole(Role.USER);
                break;
            default:
                setRole(Role.FAIL);
                break;
        }
    }

    public String getRoleName(){
        return role.name();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "role=" + role +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
