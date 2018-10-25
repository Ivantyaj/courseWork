package Instruments;

import java.io.*;
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
            printStream = new PrintStream(socket.getOutputStream());
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            objectInput = new ObjectInputStream(socket.getInputStream());
            objectOutput = new ObjectOutputStream(socket.getOutputStream());

//            daoFactory = new SqlDaoFactory();
//            GenericDao dao = null;
//            Message message = null;
//            Message replyMessage = null;
//            User user;
//            MessageType operationType;
//            MessageType messageType;
//
//            while (true) {
//                message = (Message) objectInput.readObject();
//                operationType = message.getOperationType();
//                messageType = message.getMessageType();
//                switch (operationType) {
//                    case SIGN:
//                        System.out.println("SIGN operation :");
//                        switch (messageType) {
//                            case UP:
//                                System.out.println("SIGN UP");
//                                user = (User) message.getMessage();
//
//                                /**
//                                 * check if this user exists
//                                 */
//                                break;
//                            case IN:
//                                System.out.println("SIGN IN");
//                        }
//                }
//            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
