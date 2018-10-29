package ui;

import javax.swing.*;


public class WaitFrame extends JFrame {

    JLabel labelWait;
    JLabel labelServer;

    public WaitFrame(String title) {
        super(title);
        setSize(190,100);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        labelServer = new JLabel("---SERVER---");
        labelWait = new JLabel("WAIT FOR CONNECTION");

        setLayout(null);
        labelWait.setBounds(15,30,180,30);
        labelServer.setBounds(50,10,180,30);

        add(labelServer);
        add(labelWait);

    }
}
