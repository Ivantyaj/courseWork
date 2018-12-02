package ui;

import Instruments.Server;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.text.ParseException;

public class ServerGUI extends JFrame {

    public static final ServerGUI INSTANCE = new ServerGUI("Сервер");

    private static final int PORT = 1502;
    private int port = PORT;

    private int clientCount = 0;

    private JButton btnStart;

    private JLabel labelClient;
    private JTextArea textAreaMessage;
    private ServerSocket serverSocket;
    private JFormattedTextField ftfPort;

    private ServerGUI(String str) {
        super(str);
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        btnStart = new JButton("Запуск");
        //btnStop = new JButton("Остановка");
        //textIP = new JTextField(9);
        labelClient = new JLabel("Client: ");
        textAreaMessage = new JTextArea(9, 9);
        textAreaMessage.setEditable(false);
        JScrollPane sp = new JScrollPane(textAreaMessage);

        setLayout(null);

        btnStart.setBounds(10, 50, 90, 20);
        labelClient.setBounds(10, 105, 90, 20);
        sp.setBounds(10, 130, 300, 200);

        JLabel lbPort = new JLabel("Port:");
        lbPort.setBounds(10, 10, 40, 20);

        MaskFormatter mf = null;
        try {
            mf = new MaskFormatter("####");
            mf.setPlaceholderCharacter('*');
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ftfPort = new JFormattedTextField(mf);
        ftfPort.addKeyListener(new TftCaractersListener());
        ftfPort.setBounds(50,10,40,20);
        ftfPort.setText(String.valueOf(PORT));

        add(btnStart);
        add(lbPort);

        add(labelClient);
        add(sp);
        add(ftfPort);

        btnStart.addActionListener(new ServerGUI.ButtonActionListener());

    }

    public void addClient() {
        clientCount++;
        labelClient.setText("Client: " + String.valueOf(clientCount));
    }

    public void removeClient() {
        clientCount--;
        labelClient.setText("Client: " + String.valueOf(clientCount));
    }

    public void setMessage(String message) {
        textAreaMessage.setText(textAreaMessage.getText() + message + "\r\n");
    }

    public void setPort(int port) {
        this.port = port;
    }

    public class ButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnStart) {
                System.out.println("Start server");
                setPort(port);
                start();
                btnStart.setEnabled(false);
                ftfPort.setEnabled(false);
            }
        }
    }

    private void start() {
        try {
            serverSocket = new ServerSocket(port);
            new Server(serverSocket).start();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public class TftCaractersListener extends KeyAdapter {

        public void keyTyped(KeyEvent e) {
            char c = e.getKeyChar();
            if (!((c >= '0') && (c <= '9') ||
                    (c == KeyEvent.VK_BACK_SPACE) ||
                    (c == KeyEvent.VK_DELETE))) {
                JOptionPane.showMessageDialog(null, "Вводите олько цифры!", "ОШИБКА!", JOptionPane.ERROR_MESSAGE);
                e.consume();
            }
        }

    }
}
