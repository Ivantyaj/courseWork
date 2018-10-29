package ui;

import Message.Message;

import java.io.ObjectOutputStream;

public interface SocketGuiInterface {

    public void setClientSendStream(ObjectOutputStream clientSendStream);
    public void setMessage(Message message);
}
