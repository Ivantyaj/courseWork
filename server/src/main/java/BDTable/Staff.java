package BDTable;

import java.io.Serializable;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Staff implements Serializable {
    private int id;
    private float salary;
    private float goverment;
    private Date date;

    public Staff() {
    }

    public Staff(int id, float salary, float goverment, Date date) {
        this.id = id;
        this.salary = salary;
        this.goverment = goverment;
        this.date = date;
    }

    public Staff(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getInt("id");
        this.salary = resultSet.getFloat("salary");
        this.goverment = resultSet.getFloat("goverment");
        this.date = resultSet.getDate("date");
    }

    public String[] toStringArray() {
        return new String[]{String.valueOf(id),String.valueOf(salary), String.valueOf(goverment),String.valueOf(date)};
    }

    @Override
    public String toString() {
        return "Staff{" +
                "id=" + id +
                ", salary=" + salary +
                ", goverment=" + goverment +
                ", date=" + date +
                '}';
    }

    public float getSalary() {
        return salary;
    }

    public float getGoverment() {
        return goverment;
    }
}
