package BDTable;

import java.io.Serializable;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Accessories implements Serializable {
    int id;
    String name;
    int count;
    float price;

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
}
