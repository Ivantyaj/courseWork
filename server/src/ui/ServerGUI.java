package ui;

import Instruments.Server;
import Instruments.ServerThread;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.MaskFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.text.ParseException;
import java.util.ArrayList;

public class ServerGUI extends JFrame {

    public static final ServerGUI INSTANCE = new ServerGUI("Сервер");

    private static final int PORT = 1502;
    private int port = PORT;


    private Thread thead;


    int clientCount = 0;

    JButton btnStart;
    //JButton btnStop;
    //JTextField textPort;

    JLabel labelClient;
    JTextArea textAreaMessage;
    ServerSocket serverSocket;
    JFormattedTextField ftfPort;

    Server server;


    ArrayList<Thread> threadArrayList = new ArrayList<>();

    public void addToThreadArray(Thread thread){
        threadArrayList.add(thread);
    }

    public void setServer(Server server) {
        this.server = server;
    }

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

    public class ButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
//            if (e.getSource() == btnStop) {
//
//            }
            if (e.getSource() == btnStart) {
                System.out.println("Start server");
                start();
                btnStart.setEnabled(false);
                ftfPort.setEnabled(false);
            }
        }
    }


    public void setPort(int port) {
        this.port = port;
    }
    private void start() {
        try {
            serverSocket = new ServerSocket(port);
            new Server(serverSocket).start();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    class DigitFilter extends DocumentFilter {
        private static final String DIGITS = "\\d+";

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {

            if (string.matches(DIGITS)) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attrs) throws BadLocationException {
            if (string.matches(DIGITS)) {
                super.replace(fb, offset, length, string, attrs);
            }
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
