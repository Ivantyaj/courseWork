package ui;

import javax.swing.*;


public class WaitFrame extends JFrame {

    JLabel labelWait;
    JLabel labelServer;

    public WaitFrame(String title) {
        super(title);
        setSize(210,100);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        labelServer = new JLabel("---СЕРВЕР---");
        labelWait = new JLabel("ОЖИДАНИЕ ПОДКЛЮЧЕНИЯ");

        setLayout(null);
        labelWait.setBounds(10,30,180,30);
        labelServer.setBounds(60,10,180,30);

        add(labelServer);
        add(labelWait);

    }
}
