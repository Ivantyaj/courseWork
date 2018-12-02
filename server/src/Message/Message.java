package Message;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {
    public enum cmd{
        Start,
        LogIn,
        UserRequest,
        Stop,
        UserDelete,
        UserAdd,
        UserRedact,

        StaffRequest,
        StaffAdd,
        StaffRedact,
        StaffDelete,

        ProdactionRequest,
        ProdactionAdd,
        ProdactionRedact,
        ProdactionDelete,

        RawRequest,
        RawAdd,
        RawRedact,
        RawDelete,

        ReportRequest,
        ReportDelete,
        Evaluate,
        RequestReportOne,
        SearchReports,
        FilterReports,
        FailSQL

    }
    cmd command = cmd.Start;

    private ArrayList<Object> messageArray;

    public cmd getCommand() {
        return command;
    }

    public void setCommand(cmd command) {
        this.command = command;
    }


    public void setMessageArray(ArrayList<Object> messageArray) {
        this.messageArray = new ArrayList<>(messageArray);
    }

    public void setArrayOneObject(Object messageObject){
        this.messageArray = new ArrayList<>();
        this.messageArray.add(messageObject);
    }

    public Object getArrayOneObject(){
        return messageArray.toArray()[0];
    }

    public ArrayList<Object> getMessageArray() {
        return messageArray;
    }

    @Override
    public String toString() {
        String str = new String();
        str += String.valueOf(command);
        if(messageArray != null)
            str += messageArray.toString();
        return str;
    }
}
