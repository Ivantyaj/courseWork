package Instruments;

import BDTable.Accessories;
import BDTable.Prodaction;
import BDTable.Staff;
import Message.Message;
import BDTable.User;
import ui.AdminMainMenu;
import ui.EvaluateUI;
import ui.LogIN;
import ui.DB;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
    public static final int PORT = 1502;

    AdminMainMenu uiAdminMain = null;
    User user;

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

            LogIN uiLogIN = new LogIN("Вход");
            uiLogIN.setVisible(true);
            uiLogIN.setResizable(false);
            uiLogIN.setLocationRelativeTo(null);
            uiLogIN.setClientSendStream(clientSendStream);
            uiLogIN.setMessage(message);

            uiAdminMain = new AdminMainMenu(clientSendStream, message);
            uiAdminMain.setVisible(false);
            uiAdminMain.setResizable(false);
            uiAdminMain.setLocationRelativeTo(null);

            DB db = uiAdminMain.getDbUI();
            EvaluateUI mrpUI = uiAdminMain.getEvaluateUI();

            while (!message.getCommand().equals(Message.cmd.Stop)) {
                //System.out.println(message);

                uiLogIN.setClientSendStream(clientSendStream);
                uiLogIN.setMessage(message);

                //clientSendStream.writeObject(message); //вынести

                message = (Message) clientReadStream.readObject();
                switch (message.getCommand()) {
                    case LogIn:
                        user = (User) message.getMessageArray().get(0);
                        switch (user.getRole()) {
                            case ADMIN:
                                uiAdminMain.setVisible(true);
                                uiAdminMain.setUser(user);
                                break;
                            case USER:
                                break;
                            case FAIL:
                                JOptionPane.showMessageDialog(null, "Данные не верны");
                                break;
                        }
                        break;
                    case UserRequest: {
                        System.out.println(message.getMessageArray());

                        ArrayList<User> userArrayList = new ArrayList<>();
                        for (Object object : message.getMessageArray()) {
                            userArrayList.add((User) object);
                        }

                        String[][] stringDa = new String[userArrayList.size()][];
                        int i = 0;
                        for (User user1 : userArrayList) {
                            stringDa[i] = user1.toStringArray();
                            i++;
                        }
                        db.setUserData(stringDa);
                    }
                        break;
                    case StaffRequest: {
                        System.out.println(message.getMessageArray());

                        ArrayList<Staff> staffArrayList = new ArrayList<>();
                        for (Object object : message.getMessageArray()) {
                            staffArrayList.add((Staff) object);
                        }

                        String[][] stringDaStaff = new String[staffArrayList.size()][];
                        int i = 0;
                        for (Staff staff : staffArrayList) {
                            stringDaStaff[i] = staff.toStringArray();
                            i++;
                        }
                        db.setStaffData(stringDaStaff);
                        mrpUI.setStaffData(stringDaStaff);
                    }
                        break;
                    case AccessoriesRequest: {
                        System.out.println(message.getMessageArray());

                        ArrayList<Accessories> accessoriesArrayList = new ArrayList<>();
                        for (Object object : message.getMessageArray()) {
                            accessoriesArrayList.add((Accessories) object);
                        }

                        String[][] stringDaAccessories = new String[accessoriesArrayList.size()][];
                        int i = 0;
                        for (Accessories accessories : accessoriesArrayList) {
                            stringDaAccessories[i] = accessories.toStringArray();
                            i++;
                        }
                        db.setAccessoriesData(stringDaAccessories);
                        mrpUI.setAccessoriesData(stringDaAccessories);
                    }
                    break;
                    case ProdactionRequest: {
                        System.out.println(message.getMessageArray());

                        ArrayList<Prodaction> prodactionArrayList = new ArrayList<>();
                        for (Object object : message.getMessageArray()) {
                            prodactionArrayList.add((Prodaction) object);
                        }

                        String[][] stringDaProdaction = new String[prodactionArrayList.size()][];
                        int i = 0;
                        for (Prodaction prodaction : prodactionArrayList) {
                            stringDaProdaction[i] = prodaction.toStringArray();
                            i++;
                        }
                        db.setProdactionData(stringDaProdaction);
                        mrpUI.setProdactionData(stringDaProdaction);
                    }
                    break;
                    default:
                        break;
                }

                System.out.println(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "СЕРВЕР НЕ ДОСТУПЕН", "ОШИБКА!", JOptionPane.ERROR_MESSAGE);
        }
    }

    protected void workAdmin(ObjectOutputStream clientSendStream, Message message) {
        uiAdminMain = new AdminMainMenu(clientSendStream, message);
        uiAdminMain.setVisible(true);
        uiAdminMain.setResizable(false);
        uiAdminMain.setLocationRelativeTo(null);
        //uiAdminMain.setClientSendStream(clientSendStream);
        //uiAdminMain.setMessage(message);
    }

}
