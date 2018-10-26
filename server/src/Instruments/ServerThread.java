package Instruments;

import Message.Message;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;

public class ServerThread extends Thread {

    private PrintStream printStream;
    private BufferedReader bufferedReader;
    private ObjectOutput objectOutput;
    private ObjectInput objectInput;
    private Socket socket;
    private Connection connection;
//    private SqlDaoFactory   daoFactory;


    ObjectInputStream serverReadStream;
    ObjectOutputStream serverSendStream;

    Message message;

    public ServerThread(Socket socket) {
        this.socket = socket;
        //  daoFactory = new SqlDaoFactory();
        // connection = daoFactory.getConnection();
    }

    @Override
    public void run() {
        System.out.println(socket.getInetAddress().getHostName() +
                " " + socket.getInetAddress() + "connected");

        try {
            serverReadStream = new ObjectInputStream(socket.getInputStream());
            serverSendStream = new ObjectOutputStream(socket.getOutputStream());

            message = (Message) serverReadStream.readObject();
            System.out.println(message);
            while (!message.getCommand().equals(Message.cmd.Stop)){
                message =(Message) serverReadStream.readObject();
            }

        } catch (IOException | ClassNotFoundException e) {
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
