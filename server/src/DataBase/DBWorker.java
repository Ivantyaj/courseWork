package DataBase;

import java.sql.*;

public class DBWorker {
    private static final String URL = "jdbc:mysql://localhost:3306/server?autoReconnect=true&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    private Connection connection;

    public DBWorker() {
        try {
            connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }


}
