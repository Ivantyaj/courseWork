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

        //ServerGUI a = new ServerGUI("Сервер");
        //a.setVisible(true);

        System.out.println(socket.getInetAddress().getHostName() +
                " " + socket.getInetAddress() + "connected");

        dbWorker = new DBWorker();

        try {
            serverReadStream = new ObjectInputStream(socket.getInputStream());
            serverSendStream = new ObjectOutputStream(socket.getOutputStream());

            //message = (Message) serverReadStream.readObject();

            while (!message.getCommand().equals(Message.cmd.Stop)){
                message =(Message) serverReadStream.readObject();
                System.out.println(message);
                //if(message.getCommand().equals(Message.cmd.LogIn))

                switch (message.getCommand()){
                    case LogIn:
                        //message.setCommand(Message.cmd.Start);

                        String login = message.getMessageArray().get(0);
                        String password = message.getMessageArray().get(1);

                        if(login.isEmpty() || password.isEmpty()){
                            message.setCommand(Message.cmd.LogInRefuse);
                        } else {

                            String query = "select * from users where " +
                                    "login = " + login + " and" + " password = " + password;
                            Statement statement = dbWorker.getConnection().createStatement();
                            ResultSet resultSet = statement.executeQuery(query);

                            if (resultSet.next()) {
                                User user = new User();
                                user.setRole(resultSet.getString("role"));
                                message.setCommand(Message.cmd.LogInSucsess);
                                message.setMessageArray(user.getRoleName());
                            } else {
                                message.setCommand(Message.cmd.LogInRefuse);
                            }
                        }

                        serverSendStream.writeObject(message);

                        break;
                }
            }

        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
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
