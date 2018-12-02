package Instruments;

import Message.Message;
import ui.*;
import BDTable.*;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
    private static final int PORT = 1502;

    private MainMenu uiAdminMain = null;
    private DB db;
    private EvaluateUI mrpUI;
    private LogIN uiLogIN;


    private User user;
    private JPanel panel;

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }

    private Client() {
        panel = new JPanel();
    }

    private void run() {


        Message message = new Message();

        Socket clientSocket;
        try {
            uiLogIN = new LogIN("Вход");

            clientSocket = new Socket("127.0.0.1", PORT);
            ObjectOutputStream clientSendStream = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream clientReadStream = new ObjectInputStream(clientSocket.getInputStream());


            uiLogIN.setVisible(true);
            uiLogIN.setResizable(false);
            uiLogIN.setLocationRelativeTo(null);

            uiLogIN.setClientSendStream(clientSendStream);
            uiLogIN.setMessage(message);

            uiAdminMain = new MainMenu(clientSendStream, message);
            uiAdminMain.setVisible(false);
            uiAdminMain.setResizable(false);
            uiAdminMain.setLocationRelativeTo(null);

            db = uiAdminMain.getDbUI();
            mrpUI = uiAdminMain.getEvaluateUI();

            while (!message.getCommand().equals(Message.cmd.Stop)) {

                uiLogIN.setClientSendStream(clientSendStream);
                uiLogIN.setMessage(message);

                message = (Message) clientReadStream.readObject();
                switch (message.getCommand()) {
                    case LogIn:
                        user = (User) message.getMessageArray().get(0);
                        switch (user.getRole()) {
                            case ADMIN:
                            case USER:
                                uiAdminMain.setVisible(true);
                                uiAdminMain.setUser(user);
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
                    case RawRequest: {
                        System.out.println(message.getMessageArray());

                        ArrayList<RawPackage> rawPackageArrayList = new ArrayList<>();
                        for (Object object : message.getMessageArray()) {
                            rawPackageArrayList.add((RawPackage) object);
                        }

                        String[][] stringDaRawPackage = new String[rawPackageArrayList.size()][];
                        int i = 0;
                        for (RawPackage rawPackage : rawPackageArrayList) {
                            stringDaRawPackage[i] = rawPackage.toStringArray();
                            i++;
                        }
                        db.setRawData(stringDaRawPackage);
                        mrpUI.setRawData(stringDaRawPackage);
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
                    case ReportRequest:
                    case SearchReports:
                    case FilterReports:
                        System.out.println(message.getMessageArray());

                        ArrayList<Report> reportArrayList = new ArrayList<>();
                        for (Object object : message.getMessageArray()) {
                            reportArrayList.add((Report) object);
                        }

                        String[][] stringDaReport = new String[reportArrayList.size()][];
                        int i = 0;
                        for (Report report : reportArrayList) {
                            stringDaReport[i] = report.toStringArray();
                            i++;
                        }
                        mrpUI.setReportData(stringDaReport);

                        break;
                    case RequestReportOne:
                        System.out.println(message.getMessageArray());

                        Report report = (Report) message.getArrayOneObject();

                        Float[] floatData = {
                                report.getTotalStaff(),
                                report.getTotalRawPackage(),
                                report.getTotalProdaction(),
                        };

                        mrpUI.setGraphicsData(floatData);
                        break;
                    case FailSQL:
                        JOptionPane.showMessageDialog(null, "Не удалось выполнить запрос к базе данных\r\n" +
                                "   Проверьте все введенные вами данные", "ОШИБКА!", JOptionPane.ERROR_MESSAGE);
                        break;
                    default:
                        break;
                }

                System.out.println(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();

            if (uiAdminMain != null)
                uiAdminMain.setVisible(false);
            if (mrpUI != null)
                mrpUI.setVisible(false);
            if (db != null)
                db.setVisible(false);
            if (uiLogIN != null)
                uiLogIN.setVisible(false);

            int dialogResult = JOptionPane.showConfirmDialog(panel,
                    "СЕРВЕР НЕ ДОСТУПЕН\r\nПереподлючиться?",
                    "Нет доступа",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.ERROR_MESSAGE);
            if (dialogResult != JOptionPane.YES_OPTION) {
                return;
            }
            run();
        }
    }

}
