
import Message.Message;
import org.junit.*;

import java.util.ArrayList;

public class TestMessage {

    private static Message message;
    private static ArrayList<Object> arrayList;

    @BeforeClass
    public static void createObject()  {
        message = new Message();
    }

    @AfterClass
    public static void clearObject() {
        message = null;
    }

    @Before
    public void setUp() {
        arrayList = new ArrayList<>();
    }

    @After
    public void clearData() {
        arrayList = null;
    }

    @Test (expected = IndexOutOfBoundsException.class)
    public void testSetOneObject() {
        arrayList.add("0");
        arrayList.add("1");
        arrayList.add("2");

        message.setMessageArray(arrayList);

        message.setArrayOneObject("test");

        arrayList = message.getMessageArray();

        arrayList.get(0);
        arrayList.get(1);
    }

    @Test
    public void testGetOneObject() {
        arrayList.add("0");
        arrayList.add("1");
        arrayList.add("2");

        message.setMessageArray(arrayList);

        Assert.assertEquals("0",message.getArrayOneObject());
    }

}
