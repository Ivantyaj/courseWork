package DataBase;

import BDTable.Accessories;
import BDTable.Prodaction;
import BDTable.Report;
import BDTable.Staff;
import Message.Message;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

public class SQLRequest {

    DBWorker dbWorker = new DBWorker();

    //TODO объединить case
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
                updateData(message);
                break;
             ////////////////////////////////
            case StaffRequest:
                resultSet = selectFrom("staff");
                break;
            case StaffAdd:
                insertInto(message);
                break;
            case StaffDelete:
                deleteFrom("staff", message);
                break;
            case StaffRedact:
                updateData(message);
                break;
            //////////////////////////////////
            case AccessoriesRequest:
                resultSet = selectFrom("accessories");
                break;
            case AccessoriesAdd:
                insertInto(message);
                break;
            case AccessoriesDelete:
                deleteFrom("accessories", message);
                break;
            case AccessoriesRedact:
                updateData(message);
                break;
            //////////////////////////////////
            case ProdactionRequest:
                resultSet = selectFrom("prodaction");
                break;
            case ProdactionAdd:
                insertInto(message);
                break;
            case ProdactionDelete:
                deleteFrom("prodaction", message);
                break;
            case ReportRequest:
                resultSet = selectFrom("report");
                break;
            case ProdactionRedact:
                updateData(message);
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
            if(table == "users" && Integer.parseInt((String) id) == 1)
                continue;
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
                query = "insert into users(login, password, role, name, lastname, phone) values (?,?,?,?,?,?)";
                preparedStatement = dbWorker.getConnection().prepareStatement(query);
                preparedStatement.setString(1,(String) message.getMessageArray().get(1));
                preparedStatement.setString(2,(String) message.getMessageArray().get(2));
                preparedStatement.setString(3,(String) message.getMessageArray().get(3));
                preparedStatement.setString(4,(String) message.getMessageArray().get(4));
                preparedStatement.setString(5,(String) message.getMessageArray().get(5));
                preparedStatement.setString(6,(String) message.getMessageArray().get(6));
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
    }

    //TODO вынести preparedStatement
    private void updateData(Message message) throws SQLException {
        String query = null;
        PreparedStatement preparedStatement = null;
        switch (message.getCommand()){
            case UserRedact:
                query = "UPDATE users set login = ?, password = ?, role = ?, name = ?, lastname = ?, phone = ? WHERE id = ?";
                preparedStatement = dbWorker.getConnection().prepareStatement(query);
                preparedStatement.setString(1,(String) message.getMessageArray().get(1));
                preparedStatement.setString(2,(String) message.getMessageArray().get(2));
                preparedStatement.setString(3,(String) message.getMessageArray().get(3));
                preparedStatement.setString(4,(String) message.getMessageArray().get(4));
                preparedStatement.setString(5,(String) message.getMessageArray().get(5));
                preparedStatement.setString(6,(String) message.getMessageArray().get(6));
                preparedStatement.setInt(7,Integer.parseInt((String) message.getMessageArray().get(0)));
                break;
            case StaffRedact:
                query = "UPDATE staff set salary = ?,goverment = ?,date = ?";
                preparedStatement = dbWorker.getConnection().prepareStatement(query);
                preparedStatement.setFloat(1, Float.valueOf((String)message.getMessageArray().get(0)));
                preparedStatement.setFloat(2, Float.valueOf((String)message.getMessageArray().get(1)));
                preparedStatement.setString(3, (String) message.getMessageArray().get(2));
                break;
            case ProdactionRedact:
                query = "UPDATE prodaction set energy = ?, tariff = ?, amortisation = ?, date = ?";
                preparedStatement = dbWorker.getConnection().prepareStatement(query);
                preparedStatement.setFloat(1, Integer.valueOf((String)message.getMessageArray().get(0)));
                preparedStatement.setFloat(2, Float.valueOf((String)message.getMessageArray().get(1)));
                preparedStatement.setFloat(3, Float.valueOf((String)message.getMessageArray().get(2)));
                preparedStatement.setString(4, (String) message.getMessageArray().get(3));
                break;
            case AccessoriesRedact:
                query = "UPDATE accessories set name = ?, count = ?, price = ?";
                preparedStatement = dbWorker.getConnection().prepareStatement(query);
                preparedStatement.setString(1, (String) message.getMessageArray().get(0));
                preparedStatement.setInt(2, Integer.valueOf((String)message.getMessageArray().get(1)));
                preparedStatement.setFloat(3, Float.valueOf((String)message.getMessageArray().get(2)));
                break;
        }

        preparedStatement.execute();
    }

    //TODO divide
    public ArrayList<Object> requestDataForEvaluate(int idStaff, int idProdaction, int idAccessories) throws SQLException {
        String query = "select * from %s where id=?";
        PreparedStatement preparedStatement;

        String queryTable;

        queryTable = String.format(query, "staff");
        preparedStatement = dbWorker.getConnection().prepareStatement(queryTable);
        preparedStatement.setInt(1,idStaff);
        ResultSet resultSet = preparedStatement.executeQuery();
        Staff staff = null;
        if(resultSet.next()) {
            staff = new Staff(resultSet);
        }

        queryTable = String.format(query, "prodaction");
        preparedStatement = dbWorker.getConnection().prepareStatement(queryTable);
        preparedStatement.setInt(1,idProdaction);
        resultSet = preparedStatement.executeQuery();
        Prodaction prodaction = null;
        if(resultSet.next()) {
            prodaction = new Prodaction(resultSet);
        }

        queryTable = String.format(query, "accessories");
        preparedStatement = dbWorker.getConnection().prepareStatement(queryTable);
        preparedStatement.setInt(1,idAccessories);
        resultSet = preparedStatement.executeQuery();
        Accessories accessories = null;
        if(resultSet.next()) {
            accessories = new Accessories(resultSet);
        }

        ArrayList<Object> arrayList = new ArrayList<>();

        arrayList.add(staff);
        arrayList.add(prodaction);
        arrayList.add(accessories);

        return arrayList;
    }

    public void insertIntoReport(Report report) throws SQLException {
        String query = "insert into report(date, result, id_user, id_staff, id_accessories, id_prodaction) values(?,?,?,?,?,?)";
        PreparedStatement preparedStatement = dbWorker.getConnection().prepareStatement(query);
        preparedStatement.setDate(1, report.getDate());
        preparedStatement.setFloat(2, report.getResult());
        preparedStatement.setInt(3, report.getId_user());
        preparedStatement.setInt(4, report.getId_staff());
        preparedStatement.setInt(5, report.getId_accessories());
        preparedStatement.setInt(6, report.getId_prodaction());

        preparedStatement.execute();
    }
}
