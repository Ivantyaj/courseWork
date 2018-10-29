package Instruments;

import Message.Message;
import Users.User;
import ui.AdminMainMenu;
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
        Client client = new Client();
        client.run();
    }

    public void run(){


        Message message = new Message();

        Socket clientSocket;
        try {
            clientSocket = new Socket("127.0.0.1", PORT);
            ObjectOutputStream clientSendStream = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream clientReadStream = new ObjectInputStream(clientSocket.getInputStream());

            LogIN uiLogIN = new LogIN("Авторизация");
            uiLogIN.setVisible(true);
            uiLogIN.setResizable(false);
            uiLogIN.setLocationRelativeTo(null);

            while (!message.getCommand().equals(Message.cmd.Stop)) {
                //System.out.println(message);

                uiLogIN.setClientSendStream(clientSendStream);
                uiLogIN.setMessage(message);

                //clientSendStream.writeObject(message); //вынести

                message = (Message) clientReadStream.readObject();

                switch (message.getCommand()) {
                    case LogInSucsessAdmin:
                        workAdmin();
                        break;
                    case LogInSucsessUser:
                        break;
                }

                System.out.println(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void workAdmin(){
        AdminMainMenu uiAdminMain = new AdminMainMenu();
        uiAdminMain.setVisible(true);
        uiAdminMain.setResizable(false);
        uiAdminMain.setLocationRelativeTo(null);
    }

}
