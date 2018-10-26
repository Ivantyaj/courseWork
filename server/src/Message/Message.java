package Message;

import java.io.Serializable;

public class Message implements Serializable {
    public enum cmd{
        Start,
        LogIn,
        Stop
    }
    cmd command = cmd.Start;

    private Object messageObject;

    public cmd getCommand() {
        return command;
    }
    public void setMessageObject(Object messageObject) {
        this.messageObject = messageObject;
    }

    public Object getMessageObject() {
        return messageObject;
    }

    @Override
    public String toString() {
        return super.toString() + messageObject.toString();
    }
}
