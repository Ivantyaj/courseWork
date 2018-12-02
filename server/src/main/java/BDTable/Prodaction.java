package BDTable;

import java.io.Serializable;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Prodaction implements Serializable {
    private int id;
    private int energy;
    private float tariff;
    private float amortisation;
    private Date date;

    public Prodaction() {
    }

    public Prodaction(int id, int energy, float tariff, float amortisation, Date date) {
        this.id = id;
        this.energy = energy;
        this.tariff = tariff;
        this.amortisation = amortisation;
        this.date = date;
    }

    public Prodaction(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getInt("id");
        this.energy = resultSet.getInt("energy");
        this.tariff = resultSet.getFloat("tariff");
        this.amortisation = resultSet.getFloat("amortisation");
        this.date = resultSet.getDate("date");
    }

    public String[] toStringArray() {
        return new String[]{String.valueOf(id),String.valueOf(energy),String.valueOf(tariff),String.valueOf(amortisation),String.valueOf(date)};
    }

    @Override
    public String toString() {
        return "Prodaction{" +
                "id=" + id +
                ", energy=" + energy +
                ", tariff=" + tariff +
                ", amortisation=" + amortisation +
                ", date=" + date +
                '}';
    }

    int getEnergy() {
        return energy;
    }

    float getTariff() {
        return tariff;
    }

    float getAmortisation() {
        return amortisation;
    }
}
