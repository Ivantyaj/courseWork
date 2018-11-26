package BDTable;

import Message.Message;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Report {
    int id;
    Date date;
    float result;
    int id_user;
    int id_staff;
    int id_accessories;
    int id_prodaction;

    public Report(int id, Date date, float result, int id_user, int id_staff, int id_accessories, int id_prodaction) {
        this.id = id;
        this.date = date;
        this.result = result;
        this.id_user = id_user;
        this.id_staff = id_staff;
        this.id_accessories = id_accessories;
        this.id_prodaction = id_prodaction;
    }

    public Report(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getInt("id");
        this.date = resultSet.getDate("date");
        this.result = resultSet.getFloat("result");
        this.id_user = resultSet.getInt("id_user");
        this.id_staff = resultSet.getInt("id_staff");
        this.id_accessories =resultSet.getInt( "id_accessories");
        this.id_prodaction = resultSet.getInt("id_prodaction");
    }


}


