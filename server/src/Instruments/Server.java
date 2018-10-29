package Instruments;

import ui.WaitFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ServerSocket;
import javax.swing.*;

public class Server{
    public static final int PORT = 1502;


    String sPort = "";
    int port = 0;

    public void setsPort(String sPort) {
        this.sPort = sPort;
    }

//    public Server(String str) {
//        this.sPort = str;
//    }

    public void start() {
        try {
            if (sPort.length() == 0) {
                port = PORT;
                System.out.println("second");
            }else if (Integer.parseInt(sPort) != 0 && sPort.length() == 4) {
                port = Integer.parseInt(sPort);
                System.out.println("first");
            } else {
                System.out.println("Invalid port!");
                //System.exit(0);
                return;
            }
            ServerSocket serverSocket = new ServerSocket(port);

            WaitFrame waitFrame = new WaitFrame("Wait");
            waitFrame .setVisible(true);
            waitFrame .setResizable(false);
            waitFrame .setLocationRelativeTo(null);

            while (true) { //????
                new ServerThread(serverSocket.accept()).start();
                waitFrame.setVisible(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

