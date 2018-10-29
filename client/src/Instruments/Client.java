package Instruments;

import Message.Message;
import Users.User;
import ui.AdminMainMenu;
import ui.LogIN;
import ui.UsersDB;

import javax.swing.*;
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

    public void run() {


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
            UsersDB usersDB = null;
            while (!message.getCommand().equals(Message.cmd.Stop)) {
                //System.out.println(message);

                uiLogIN.setClientSendStream(clientSendStream);
                uiLogIN.setMessage(message);

                //clientSendStream.writeObject(message); //вынести

                message = (Message) clientReadStream.readObject();
                switch (message.getCommand()) {
                    case LogIn:
                        User user = (User) message.getMessageArray().get(0);
                        switch (user.getRole()) {
                            case ADMIN:
                                workAdmin(clientSendStream, message);
                                break;
                            case USER:
                                break;
                            case FAIL:
                                JOptionPane.showMessageDialog(null, "Данные не верны");
                                break;
                        }
                        break;
                    case UserRequest:
                        System.out.println(message.getMessageArray());
                        
                        ArrayList<User> userArrayList = new ArrayList<>();
                        for (Object object: message.getMessageArray()) {
                            userArrayList.add((User) object);
                        }

//                        ArrayList<String[]> strings = new ArrayList<>();
//                        for (User user1: userArrayList){
//                            strings.add(user1.toStringArray());
//                        }

                        String [][] stringDa = new String[userArrayList.size()][];
                        int i = 0;
                        for (User user1: userArrayList){
                            stringDa[i] = user1.toStringArray();
                            i++;
                        }

                        if(usersDB != null)
                            usersDB.setVisible(false);

                        usersDB = new UsersDB(stringDa);
                        usersDB.setVisible(true);
                        usersDB.setLocationRelativeTo(null);
                        usersDB.setClientSendStream(clientSendStream);
                        usersDB.setMessage(message);



                        break;
                    default:
                        break;
                }

                System.out.println(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void workAdmin(ObjectOutputStream clientSendStream, Message message) {
        AdminMainMenu uiAdminMain = new AdminMainMenu();
        uiAdminMain.setVisible(true);
        uiAdminMain.setResizable(false);
        uiAdminMain.setLocationRelativeTo(null);
        uiAdminMain.setClientSendStream(clientSendStream);
        uiAdminMain.setMessage(message);
    }

}
