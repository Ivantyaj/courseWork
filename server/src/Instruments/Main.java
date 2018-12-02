package Instruments;

import ui.ServerGUI;

public class Main {
    public static void main(String[] args) {
        ServerGUI serverGui = ServerGUI.INSTANCE;
        serverGui.setSize(350,400);
        serverGui.setVisible(true);
        serverGui.setResizable(false);
        serverGui.setLocationRelativeTo(null);
    }

}
