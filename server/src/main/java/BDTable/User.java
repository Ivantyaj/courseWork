package BDTable;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User implements Serializable {
    public enum Role {
        ADMIN,
        USER,
        FAIL
    }

    private Role role = Role.FAIL;
    private String login;
    private String password;
    private String name;
    private String lastname;
    private String phone;
    private int id;

    public User() {
    }

    public User(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getInt("id");
        this.role = setRole(resultSet.getString("role"));
        this.login = resultSet.getString("login");
        this.password = resultSet.getString("password");
        this.name = resultSet.getString("name");
        this.lastname = resultSet.getString("lastname");
        this.phone = resultSet.getString("phone");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    private Role setRole(String role) {
        Role roleToSet;
        switch (role){
            case "ADMIN":
                setRole(Role.ADMIN);
                roleToSet = Role.ADMIN;
                break;
            case "USER":
                setRole(Role.USER);
                roleToSet = Role.USER;
                break;
            default:
                setRole(Role.FAIL);
                roleToSet = Role.FAIL;
                break;
        }
        return roleToSet;
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

    public String[] toStringArray() {
        return new String[]{String.valueOf(id),login,password,String.valueOf(role),name,lastname,phone};
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
