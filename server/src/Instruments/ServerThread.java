package Instruments;

import DataBase.DBWorker;
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

        try {
            serverReadStream = new ObjectInputStream(socket.getInputStream());
            serverSendStream = new ObjectOutputStream(socket.getOutputStream());

            while (!message.getCommand().equals(Message.cmd.Stop)) {
                message = (Message) serverReadStream.readObject();
                System.out.println(message);
                serverGui.setMessage(message.toString());

                switch (message.getCommand()) {
                    case LogIn:

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
                                user.setRole(resultSet.getString("role"));
                                user.setLogin(resultSet.getString("login"));
                                user.setPassword(resultSet.getString("password"));
                            }
                        }
                        message.setArrayOneObject(user);
                        serverSendStream.writeObject(message);

                        break;
                }
            }

        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
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
