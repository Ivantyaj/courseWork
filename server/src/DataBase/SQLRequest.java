package DataBase;

import Message.Message;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLRequest {

    DBWorker dbWorker = new DBWorker();


    public ResultSet executeSqlQuery(Message message) throws SQLException {
        ResultSet resultSet = null;
        switch (message.getCommand()){
            case UserDelete:
                deleteFrom("users", message);
                break;
            case UserRequest:
                resultSet = selectFrom("users");
                break;
            case UserAdd:
                insertInto(message);
                break;
            case UserRedact:
                break;
        }
        return resultSet;
    }

    private ResultSet selectFrom(String table) throws SQLException {
        String query;
        query = "select * from %s";
        query = String.format(query, table);
        PreparedStatement preparedStatement;
        preparedStatement = dbWorker.getConnection().prepareStatement(query);

        return preparedStatement.executeQuery();
    }

    private void deleteFrom(String table, Message message) throws SQLException {
        String query;
        query = "delete from %s where id = ?";
        query = String.format(query, table);
        PreparedStatement preparedStatement;
        preparedStatement = dbWorker.getConnection().prepareStatement(query);
        for (Object id: message.getMessageArray()) {
            if(id != null) {
                preparedStatement.setString(1, (String) id);
                preparedStatement.execute();
            }
        }
    }

    private void insertInto(Message message) throws SQLException {
        String query = null;
        PreparedStatement preparedStatement = null;
        switch (message.getCommand()){
            case UserAdd:
                query = "insert into users(login, password, role) values (?,?,?)";
                preparedStatement = dbWorker.getConnection().prepareStatement(query);
                preparedStatement.setString(1,(String) message.getMessageArray().get(0));
                preparedStatement.setString(2,(String) message.getMessageArray().get(1));
                preparedStatement.setString(3,(String) message.getMessageArray().get(2));
                break;
        }

        preparedStatement.execute();
        //query = "insert into %s where id = ?";
        //query = String.format(query, table);
    }
}
