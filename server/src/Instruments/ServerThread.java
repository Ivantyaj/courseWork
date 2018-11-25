package Instruments;

import BDTable.Accessories;
import BDTable.Prodaction;
import BDTable.Staff;
import DataBase.DBWorker;
import DataBase.SQLRequest;
import Message.Message;
import Users.User;
import ui.ServerGUI;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ServerThread extends Thread {

    private PrintStream printStream;
    private BufferedReader bufferedReader;
    private ObjectOutput objectOutput;
    private ObjectInput objectInput;
    private Socket socket;
    private DBWorker dbWorker;
    private SQLRequest sqlRequest;

    ServerGUI serverGui;
//    private SqlDaoFactory   daoFactory;


    ObjectInputStream serverReadStream;
    ObjectOutputStream serverSendStream;

    Message message = new Message();

    public ServerThread(Socket socket) {
        this.socket = socket;
        //  daoFactory = new SqlDaoFactory();
        // connection = daoFactory.getConnection();
    }

    @Override
    public void run() {

        serverGui = ServerGUI.INSTANCE;
        serverGui.setVisible(true);
        serverGui.setResizable(false);
        serverGui.setLocationRelativeTo(null);
        serverGui.addClient();

        //ServerGUI a = new ServerGUI("Сервер");
        //a.setVisible(true);

        System.out.println(socket.getInetAddress().getHostName() +
                " " + socket.getInetAddress() + "connected");

        dbWorker = new DBWorker();
        sqlRequest = new SQLRequest();

        try {
            serverReadStream = new ObjectInputStream(socket.getInputStream());
            serverSendStream = new ObjectOutputStream(socket.getOutputStream());

            while (!message.getCommand().equals(Message.cmd.Stop)) {
                message = (Message) serverReadStream.readObject();
                System.out.println("incoming: " + message);
                serverGui.setMessage(message.toString());

                switch (message.getCommand()) {
                    case LogIn: {

                        String login = (String) message.getMessageArray().get(0);
                        String password = (String) message.getMessageArray().get(1);
                        User user = new User();
                        message.setCommand(Message.cmd.LogIn);
                        if (!(login.isEmpty() || password.isEmpty())) {

                            String query = "select * from users where " +
                                    "login = " + login + " and" + " password = " + password;
                            Statement statement = dbWorker.getConnection().createStatement();
                            ResultSet resultSet = statement.executeQuery(query);

                            if (resultSet.next()) {
//                                user.setId(resultSet.getInt("id"));
//                                user.setRole(resultSet.getString("role"));
//                                user.setLogin(resultSet.getString("login"));
//                                user.setPassword(resultSet.getString("password"));
                                user = new User(resultSet);
                            }
                        }
                        message.setArrayOneObject(user);
                        serverSendStream.writeObject(message);
                    }
                        break;
                    case UserRequest: {
                        ArrayList<Object> userArrayList = new ArrayList<>();
                        //String query = "select * from users";
                        //Statement statement = dbWorker.getConnection().createStatement();
//                        ResultSet resultSet = statement.executeQuery(query);
                        ResultSet resultSet = sqlRequest.executeSqlQuery(message);
                        while (resultSet.next()) {
                            userArrayList.add(new User(resultSet));
                        }
                        message.setMessageArray(userArrayList);
                        serverSendStream.writeObject(message);
                    }
                        break;
                    case UserDelete: {
                         sqlRequest.executeSqlQuery(message);
//                        Statement statement = dbWorker.getConnection().createStatement();
//
//                        for (Object id: message.getMessageArray()) {
//                            if(!id.equals(null)) {
//                                String query = "delete from users where id = " + id;
//                                statement.execute(query);
//                            }
//                        }
//                        message.setMessageArray(new ArrayList<>());
//                        serverSendStream.writeObject(message);
                    }
                    case UserAdd:
                        sqlRequest.executeSqlQuery(message);
                        break;
                    case StaffRequest:{
                        ArrayList<Object> staffArrayList = new ArrayList<>();
                        ResultSet resultSet = sqlRequest.executeSqlQuery(message);

                        //if(resultSet != null) {
                            while (resultSet.next()) {
                                staffArrayList.add(new Staff(resultSet));
                            }
                        //}
                        message.setMessageArray(staffArrayList);
                        serverSendStream.writeObject(message);
                    }break;
                    case StaffDelete:{
                        sqlRequest.executeSqlQuery(message);
                    }break;
                    case StaffAdd:{
                        sqlRequest.executeSqlQuery(message);
                    }break;
                    case AccessoriesRequest:{
                        ArrayList<Object> staffArrayList = new ArrayList<>();
                        ResultSet resultSet = sqlRequest.executeSqlQuery(message);

                        //if(resultSet != null) {
                        while (resultSet.next()) {
                            staffArrayList.add(new Accessories(resultSet));
                        }
                        //}
                        message.setMessageArray(staffArrayList);
                        serverSendStream.writeObject(message);
                    }break;
                    case AccessoriesDelete:{
                        sqlRequest.executeSqlQuery(message);
                    }break;
                    case AccessoriesAdd:{
                        sqlRequest.executeSqlQuery(message);
                    }break;
                    case ProdactionRequest:{
                        ArrayList<Object> staffArrayList = new ArrayList<>();
                        ResultSet resultSet = sqlRequest.executeSqlQuery(message);

                        //if(resultSet != null) {
                        while (resultSet.next()) {
                            staffArrayList.add(new Prodaction(resultSet));
                        }
                        //}
                        message.setMessageArray(staffArrayList);
                        serverSendStream.writeObject(message);
                    }break;
                    case ProdactionDelete:{
                        sqlRequest.executeSqlQuery(message);
                    }break;
                    case ProdactionAdd:{
                        sqlRequest.executeSqlQuery(message);
                    }break;
                    default:
                        System.out.println("Неизвестная комманда");
                        break;
                }
            }

        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
//            message = new Message(); ///хзхзхзххзхззхз
//            message.setCommand(Message.cmd.Fail);
//            try {
//                serverSendStream.writeObject(message);
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
        } finally {
            serverGui.removeClient();
        }

    }


//
//        try {
//            printStream = new PrintStream(socket.getOutputStream());
//            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            objectInput = new ObjectInputStream(socket.getInputStream());
//            objectOutput = new ObjectOutputStream(socket.getOutputStream());
//
////            daoFactory = new SqlDaoFactory();
////            GenericDao dao = null;
////            Message message = null;
////            Message replyMessage = null;
////            User user;
////            MessageType operationType;
////            MessageType messageType;
////
////            while (true) {
////ser                message = (Message) objectInput.readObject();
////                operationType = message.getOperationType();
////                messageType = message.getMessageType();
////                switch (operationType) {
////                    case SIGN:
////                        System.out.println("SIGN operation :");
////                        switch (messageType) {
////                            case UP:
////                                System.out.println("SIGN UP");
////                                user = (User) message.getMessage();
////
////                                /**
////                                 * check if this user exists
////                                 */
////                                break;
////                            case IN:
////                                System.out.println("SIGN IN");
////                        }
////                }
////            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
