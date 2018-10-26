package Message;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {
    public enum cmd{
        Start,
        LogIn,
        Stop
    }
    cmd command = cmd.Start;

    private ArrayList<String> messageArray;

    public cmd getCommand() {
        return command;
    }

    public void setCommand(cmd command) {
        this.command = command;
    }

    public void setMessageArray(ArrayList<String> messageArray) {
        this.messageArray = new ArrayList<>(messageArray);
    }

    public ArrayList<String> getMessageArray() {
        return messageArray;
    }

    @Override
    public String toString() {
        return command + messageArray.toString();
    }
}