package ui;

import Instruments.Server;
import Instruments.ServerThread;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerGUI extends JFrame {

    public static final ServerGUI INSTANCE = new ServerGUI("Сервер");

    int clientCount = 0;

    JButton btnStart;
    JButton btnPort;
    JTextField textPort;
    JTextField textIP;
    JLabel labelPort;
    JTextArea textAreaMessage;

    Server server;

    public void setServer(Server server) {
        this.server = server;
    }

    private ServerGUI(String str) {
        super(str);
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        btnStart = new JButton("Запуск");
        btnPort = new JButton("Установить порт");
        textPort = new JTextField(9);
        textIP = new JTextField(9);
        labelPort = new JLabel("Client: ");
        textAreaMessage = new JTextArea(9,9);

        textIP.setEditable(false);

        setLayout(null);

        textPort.setBounds(10, 10, 70, 20);
        btnStart.setBounds(10, 40, 70, 20);
        btnPort.setBounds(10, 65, 70, 20);
        labelPort.setBounds(10, 95, 90, 20);
        textAreaMessage.setBounds(10, 120, 300, 200);

        add(btnStart);
        add(btnPort);
        add(textPort);
        add(labelPort);
        add(textAreaMessage);


        btnPort.addActionListener(new ServerGUI.ButtonActionListener());
        btnStart.addActionListener(new ServerGUI.ButtonActionListener());

    }

    public void addClient(){
        clientCount++;
        labelPort.setText("Client: " + String.valueOf(clientCount));
    }

    public void removeClient(){
        clientCount--;
        labelPort.setText("Client: " + String.valueOf(clientCount));
    }

    public void setMessage(String message){
        textAreaMessage.setText(message);
    }

    public class ButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnPort) {
                System.out.println(textPort.getText());
                server.setsPort(textPort.getText()); //try/cath
            }
            if (e.getSource() == btnStart) {
                System.out.println("Start serv");
                server.start();
            }
        }
    }


}
