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
            case StaffRequest:
                resultSet = selectFrom("staff");
                break;
            case StaffAdd:
                insertInto(message);
                break;
            case StaffDelete:
                deleteFrom("staff", message);
                break;

            case AccessoriesRequest:
                resultSet = selectFrom("accessories");
                break;
            case AccessoriesAdd:
                insertInto(message);
                break;
            case AccessoriesDelete:
                deleteFrom("accessories", message);
                break;

            case ProdactionRequest:
                resultSet = selectFrom("prodaction");
                break;
            case ProdactionAdd:
                insertInto(message);
                break;
            case ProdactionDelete:
                deleteFrom("prodaction", message);
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
            case StaffAdd:
                query = "insert into staff(salary,goverment,date) values (?,?,?)";
                preparedStatement = dbWorker.getConnection().prepareStatement(query);
                preparedStatement.setFloat(1, Float.valueOf((String)message.getMessageArray().get(0)));
                preparedStatement.setFloat(2, Float.valueOf((String)message.getMessageArray().get(1)));
                preparedStatement.setString(3, (String) message.getMessageArray().get(2));
                break;
            case ProdactionAdd:
                query = "insert into prodaction(energy, tariff, amortisation, date) values(?,?,?,?)";
                preparedStatement = dbWorker.getConnection().prepareStatement(query);
                preparedStatement.setFloat(1, Integer.valueOf((String)message.getMessageArray().get(0)));
                preparedStatement.setFloat(2, Float.valueOf((String)message.getMessageArray().get(1)));
                preparedStatement.setFloat(3, Float.valueOf((String)message.getMessageArray().get(2)));
                preparedStatement.setString(4, (String) message.getMessageArray().get(3));
                break;
            case AccessoriesAdd:
                query = "insert into accessories(name, count, price) values (?,?,?)";
                preparedStatement = dbWorker.getConnection().prepareStatement(query);
                preparedStatement.setString(1, (String) message.getMessageArray().get(0));
                preparedStatement.setInt(2, Integer.valueOf((String)message.getMessageArray().get(1)));
                preparedStatement.setFloat(3, Float.valueOf((String)message.getMessageArray().get(2)));
                break;
        }

        preparedStatement.execute();
        //query = "insert into %s where id = ?";
        //query = String.format(query, table);
    }
}
