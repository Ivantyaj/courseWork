import BDTable.Prodaction;
import BDTable.RawPackage;
import BDTable.Report;
import BDTable.Staff;
import DataBase.DBWorker;
import DataBase.SQLRequest;
import org.junit.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TestSql {


    private static SQLRequest sql;
    private static DBWorker db;
    private static ArrayList<Object> arrayList;

    @BeforeClass
    public static void createObject() throws SQLException {
        sql = new SQLRequest();
        db = new DBWorker();
        arrayList = new ArrayList<>();

        Statement statement = db.getConnection().createStatement();

        String query = "insert into staff(id, salary,goverment,date) values (-1,1,2,'1111-11-11');";
        statement.execute(query);
        query = "insert into prodaction(id, energy, tariff, amortisation, date) values(-1,3,4,5,'1111-11-11');";
        statement.execute(query);
        query = "insert into rawpackage(id, name, count, price) values (-1,'6',7,8);";
        statement.execute(query);
        query = "insert into users(id, login, password, role, name, lastname, phone) values (-1,'9','10','FAIL','11','12','13');";
        statement.execute(query);
        query = "insert into report(id, date, result, id_user, id_staff, id_rawpackage, id_prodaction, total_staff, total_rawpackage,total_prodaction) values(-1, '1111-11-11',-1,-1,-1,-1,-1,-1,-1,-1);";
        statement.execute(query);
    }

    @AfterClass
    public static void clearObject() throws SQLException {

        Statement statement = db.getConnection().createStatement();

        String query = "delete from report where id = -1;";
        statement.execute(query);
        query = "delete from staff where id = -1;";
        statement.execute(query);
        query = "delete from prodaction where id = -1;";
        statement.execute(query);
        query = "delete from rawpackage where id = -1;";
        statement.execute(query);
        query = "delete from users where id = -1;";
        statement.execute(query);


        sql = null;
        db = null;
        arrayList = null;
    }

    @Test
    public void testRequestOneReport() throws SQLException {
        boolean answer = sql.requestOneReport(99).next();
        Assert.assertFalse(answer);

        answer = sql.requestOneReport(-1).next();
        Assert.assertTrue(answer);
    }

    @Test
    public void testSetGetResultSet() throws SQLException {
        ResultSet resultSetReport = sql.requestOneReport(-1);

        resultSetReport.next();

        Report report = new Report(resultSetReport);

        Assert.assertEquals(-1, report.getId_prodaction());
        Assert.assertEquals(-1, report.getId_rawpackage());
        Assert.assertEquals(-1, report.getId_staff());
        Assert.assertEquals(-1, report.getId_user());

    }

    @Test
    public void testRequestDataForEvaluateArray() throws SQLException {
        arrayList = sql.requestDataForEvaluate(-1, -1, -1);

        Staff staff = (Staff) arrayList.get(0);
        Prodaction prodaction = (Prodaction) arrayList.get(1);
        RawPackage rawPackage = (RawPackage) arrayList.get(2);

        Assert.assertEquals(1, staff.getSalary(), 0.01);
        Assert.assertEquals(2, staff.getGoverment(), 0.01);

        Assert.assertEquals(String.valueOf(-1), prodaction.toStringArray()[0]);
        Assert.assertEquals(String.valueOf(3), prodaction.toStringArray()[1]);
        Assert.assertEquals(Float.valueOf(4), Float.valueOf(prodaction.toStringArray()[2]));
        Assert.assertEquals(Float.valueOf(5), Float.valueOf(prodaction.toStringArray()[3]));
        Assert.assertEquals("1111-11-11", prodaction.toStringArray()[4]);

        Assert.assertEquals(7, rawPackage.getCount());
        Assert.assertEquals(8, rawPackage.getPrice(), 0.01);
    }

    @Test
    public void testRequestDataForEvaluateExeption() throws SQLException {
        arrayList = sql.requestDataForEvaluate(-3, -3, -3);
        Object[] expect = {null, null, null};
        Assert.assertArrayEquals(expect, arrayList.toArray());

    }

    @Test
    public void testEvaluate() throws SQLException {
        ResultSet resultSetReport = sql.requestOneReport(-1);
        resultSetReport.next();
        Report report = new Report(resultSetReport);

        Assert.assertEquals(-1,report.getResult(),0.01);

        report.evaluateResult(sql);

        Assert.assertEquals(54.02,report.getResult(),0.01);
    }
}
