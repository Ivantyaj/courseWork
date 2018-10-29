package Message;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {
    public enum cmd{
        Start,
        LogIn,
        LogInRefuse,
        LogInSucsessAdmin,
        LogInSucsessUser,
        Stop,
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


//    public void setMessageArray(ArrayList<String> messageArray) {
//        this.messageArray = new ArrayList<>(messageArray);
//    }



    public void setMessageArray(String messageString) {
        this.messageArray = new ArrayList<>();
        this.messageArray.add(messageString);
    }

    public ArrayList<Object> getMessageArray() {
        return messageArray;
    }

    @Override
    public String toString() {
        return command + messageArray.toString();
    }
}
