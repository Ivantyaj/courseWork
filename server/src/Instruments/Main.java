package Instruments;

import ui.ServerGUI;

public class Main {
    public static void main(String[] args) {

        //Server server = new Server();
        ServerGUI serverGui = ServerGUI.INSTANCE;
        //serverGui.setServer(server);
        serverGui.setVisible(true);
        serverGui.setResizable(false);
        serverGui.setLocationRelativeTo(null);

        //server.start();
//        ServerGUI serverGui = new ServerGUI("Сервер");
//        serverGui.setServer(server);
//        serverGui.setVisible(true);
//        serverGui.setResizable(false);
//        serverGui.setLocationRelativeTo(null);
    }

}
