package BDTable;

import java.io.Serializable;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Accessories implements Serializable {
    private int id;
    private String name;
    private int count;
    private float price;

    public Accessories() {
    }

    public Accessories(int id, String name, int count, float price) {
        this.id = id;
        this.name = name;
        this.count = count;
        this.price = price;
    }

    public Accessories(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getInt("id");
        this.name = resultSet.getString("name");
        this.count = resultSet.getInt("count");
        this.price = resultSet.getFloat("price");
    }

    public String[] toStringArray() {
        return new String[]{String.valueOf(id),name, String.valueOf(count),String.valueOf(price)};
    }

    @Override
    public String toString() {
        return "Accessories{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", count=" + count +
                ", price=" + price +
                '}';
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
