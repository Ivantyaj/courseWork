package ui;

import Message.Message;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class AdminMainMenu extends JFrame implements SocketGuiInterface {

    ObjectOutputStream clientSendStream;
    Message message;

    JButton btnDBUser;
    JButton btnUser;
    JButton btnBack;


    public AdminMainMenu() {
        super("Администратор");
        setSize(150, 200);

        btnUser = new JButton("Расчет затрат");
        btnDBUser = new JButton("Войти в базу данных");
        btnBack = new JButton("Назад");

        setDefaultCloseOperation(HIDE_ON_CLOSE);

        setLayout(null);

        btnUser.setBounds(10, 10, 120, 30);
        btnDBUser.setBounds(10, 60, 120, 30);
        btnBack.setBounds(10, 110, 120, 30);

        add(btnBack);
        add(btnDBUser);
        add(btnUser);

        btnBack.addActionListener(new ButtonActionListener());
        btnDBUser.addActionListener(new ButtonActionListener());
        btnUser.addActionListener(new ButtonActionListener());

    }

    private void visible(boolean b) {
        setVisible(b);
    }

    @Override
    public void setClientSendStream(ObjectOutputStream clientSendStream) {
        this.clientSendStream = clientSendStream;
    }

    @Override
    public void setMessage(Message message) {
        this.message = message;
    }

    public class ButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == btnBack) {
                visible(false);
            } else if (e.getSource() == btnDBUser) {

                //message.setMessageArray(stringArrayList);
                message = new Message();
                message.setCommand(Message.cmd.UserRequest);
                try {
                    clientSendStream.writeObject(message);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            } else if (e.getSource() == btnUser) {

            }
        }
    }
}
