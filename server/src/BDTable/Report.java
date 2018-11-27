package BDTable;

import DataBase.SQLRequest;

import java.io.Serializable;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Report implements Serializable {
    private int id;
    private Date date;
    private float result;
    private int id_user;
    private int id_staff;
    private int id_accessories;
    private int id_prodaction;

    public Report(int id, Date date, float result, int id_user, int id_staff, int id_accessories, int id_prodaction) {
        this.id = id;
        this.date = date;
        this.result = result;
        this.id_user = id_user;
        this.id_staff = id_staff;
        this.id_accessories = id_accessories;
        this.id_prodaction = id_prodaction;
    }

    public Report(ArrayList<Object> arrayList) {  //TODO /////???
        //this.id = id;
        this.date = Date.valueOf((String) arrayList.get(4));
        this.result = 0;
        this.id_user = Integer.parseInt((String) arrayList.get(3));
        this.id_staff = Integer.parseInt((String) arrayList.get(0));
        this.id_accessories = Integer.parseInt((String) arrayList.get(1));
        this.id_prodaction = Integer.parseInt((String) arrayList.get(2));
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

    public String[] toStringArray() {
        return new String[]{String.valueOf(id),String.valueOf(date),String.valueOf(result),String.valueOf(id_user),String.valueOf(id_staff),String.valueOf(id_accessories),String.valueOf(id_prodaction)};
    }

    public void evaluateResult(SQLRequest sql){
        try {
            ArrayList<Object> arrayList = sql.requestDataForEvaluate(id_staff, id_prodaction, id_accessories);
            Staff staff = (Staff)arrayList.get(0);
            Prodaction prodaction = (Prodaction)arrayList.get(1);
            Accessories accessories = (Accessories)arrayList.get(2);

            this.result += staff.getSalary()*staff.getGoverment() +
                    prodaction.getAmortisation() + prodaction.getEnergy()*prodaction.getTariff() +
                    accessories.getCount()*accessories.getPrice();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Date getDate() {
        return date;
    }

    public float getResult() {
        return result;
    }

    public int getId_user() {
        return id_user;
    }

    public int getId_staff() {
        return id_staff;
    }

    public int getId_accessories() {
        return id_accessories;
    }

    public int getId_prodaction() {
        return id_prodaction;
    }
}


