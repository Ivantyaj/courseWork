package ui;

import Message.Message;

import java.io.ObjectOutputStream;

public interface SocketGuiInterface {

    void setClientSendStream(ObjectOutputStream clientSendStream);
    void setMessage(Message message);
}
