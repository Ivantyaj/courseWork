package Instruments;

import ui.ServerGUI;
import ui.WaitFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import javax.swing.*;

public class Server extends Thread {
    private ServerSocket serverSocket;

    public static final int PORT = 1502;

    int maxClient;

    public void setPort(int port) {
        this.port = port;
    }

    private int port = PORT;

    // public void setsPort(String sPort) {
    //    this.sPort = sPort;
    //}

    public Server(ServerSocket socket, int maxClient) {
        this.serverSocket = socket;
        this.maxClient = maxClient;
    }

//    public void start() {
//        try {
//
//            ServerSocket serverSocket = new ServerSocket(port);
//
//            WaitFrame waitFrame = new WaitFrame("Wait");
//            waitFrame .setVisible(true);
//            waitFrame .setResizable(false);
//            waitFrame .setLocationRelativeTo(null);
//
//            //serverGui.setVisible(false);
//            ServerThread thread;
//            while (true) {
//                new ServerThread(serverSocket.accept()).start();
//                waitFrame.setVisible(false);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void run() {
        WaitFrame waitFrame = new WaitFrame("Wait");
        waitFrame.setVisible(true);
        waitFrame.setResizable(false);
        waitFrame.setLocationRelativeTo(null);
        try {
            while (true) {
                new ServerThread(serverSocket.accept(), maxClient).start();
                waitFrame.setVisible(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

