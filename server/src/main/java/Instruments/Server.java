package Instruments;

import ui.WaitFrame;
import java.io.IOException;
import java.net.ServerSocket;

public class Server extends Thread {
    private ServerSocket serverSocket;

    public static final int PORT = 1502;

    public void setPort(int port) {
        this.port = port;
    }

    private int port = PORT;

    public Server(ServerSocket socket) {
        this.serverSocket = socket;
    }
    @Override
    public void run() {
        WaitFrame waitFrame = new WaitFrame("Wait");
        waitFrame.setVisible(true);
        waitFrame.setResizable(false);
        waitFrame.setLocationRelativeTo(null);
        try {
            while (true) {
                    new ServerThread(serverSocket.accept()).start();
                    waitFrame.setVisible(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

