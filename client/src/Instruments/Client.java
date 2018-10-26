package Instruments;

import Message.Message;
import ui.LogIN;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
    public static final int PORT = 1502;

    public static void main(String[] args) {
        int port = 0;
        if (args.length == 0) {
            port = PORT;
            System.out.println("first");
        } else if (Integer.parseInt(args[0]) != 0 && args[0].length() == 4) {
            port = Integer.parseInt(args[0]);
            System.out.println("second");
        } else {
            System.out.println("Invalid port!");
            System.exit(0);
        }
        //LogIN mainWindow = new LogIN(port);
        //mainWindow.setVisible(true);

        Message message = new Message();

        Socket clientSocket;
        try {
            clientSocket = new Socket("127.0.0.1",PORT);
            ObjectOutputStream clientSendStream =  new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream clientReadStream =  new ObjectInputStream(clientSocket.getInputStream());

            LogIN uiLogIN = new LogIN("Авторизация");
            uiLogIN.setVisible(true);
            uiLogIN.setResizable(false);
            uiLogIN.setLocationRelativeTo(null);

            while (!message.getCommand().equals(Message.cmd.Stop)){
                //System.out.println(message);

                uiLogIN.setClientSendStream(clientSendStream);
                uiLogIN.setMessage(message);

                //clientSendStream.writeObject(message); //вынести

                message = (Message) clientReadStream.readObject();
                System.out.println(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


}
