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
    private int id_rawpackage;
    private int id_prodaction;
    private float totalStaff;
    private float totalProdaction;
    private float totalRawPackage;

    private float persentDefect;
    private float transport;
    private float additional;


    public Report(ArrayList<Object> arrayList) {
        //this.id = id;
        this.date = Date.valueOf((String) arrayList.get(4));
        this.result = 0;
        this.id_user = Integer.parseInt((String) arrayList.get(3));
        this.id_staff = Integer.parseInt((String) arrayList.get(0));
        this.id_rawpackage = Integer.parseInt((String) arrayList.get(1));
        this.id_prodaction = Integer.parseInt((String) arrayList.get(2));
        this.persentDefect = Float.parseFloat((String) arrayList.get(5));
        this.transport = Float.parseFloat((String) arrayList.get(6));
        this.additional = Float.parseFloat((String) arrayList.get(7));
    }


    public Report(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getInt("id");
        this.date = resultSet.getDate("date");
        this.result = resultSet.getFloat("result");
        this.id_user = resultSet.getInt("id_user");
        this.id_staff = resultSet.getInt("id_staff");
        this.id_rawpackage =resultSet.getInt( "id_rawpackage");
        this.id_prodaction = resultSet.getInt("id_prodaction");
        this.totalStaff = resultSet.getFloat("total_staff");
        this.totalRawPackage = resultSet.getFloat("total_rawpackage");
        this.totalProdaction = resultSet.getFloat("total_prodaction");
    }

    public String[] toStringArray() {
        return new String[]{String.valueOf(id),String.valueOf(date),String.valueOf(result),
                String.valueOf(id_user),String.valueOf(id_staff),String.valueOf(id_rawpackage),
                String.valueOf(id_prodaction),String.valueOf(totalStaff),String.valueOf(totalRawPackage),
                String.valueOf(totalProdaction)};
    }

    public void evaluateResult(SQLRequest sql){
        try {
            ArrayList<Object> arrayList = sql.requestDataForEvaluate(id_staff, id_prodaction, id_rawpackage);
            Staff staff = (Staff)arrayList.get(0);
            Prodaction prodaction = (Prodaction)arrayList.get(1);
            RawPackage rawPackage = (RawPackage)arrayList.get(2);

            totalStaff += staff.getSalary()+staff.getSalary()*(staff.getGoverment()/100);

            totalProdaction += prodaction.getAmortisation() + prodaction.getEnergy()*prodaction.getTariff()/10 + transport;
            totalProdaction *= persentDefect;

            totalRawPackage += rawPackage.getCount()* rawPackage.getPrice();

            this.result += totalStaff + totalProdaction + totalRawPackage + additional;

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

    public float getTotalStaff() {
        return totalStaff;
    }

    public float getTotalProdaction() {
        return totalProdaction;
    }

    public float getTotalRawPackage() {
        return totalRawPackage;
    }

    public int getId_user() {
        return id_user;
    }

    public int getId_staff() {
        return id_staff;
    }

    public int getId_rawpackage() {
        return id_rawpackage;
    }

    public int getId_prodaction() {
        return id_prodaction;
    }
}


