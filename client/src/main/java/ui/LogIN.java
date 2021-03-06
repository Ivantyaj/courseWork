package ui;

import Message.Message;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class LogIN extends JFrame implements SocketGuiInterface{

    private ObjectOutputStream clientSendStream;
    private Message message;

    private JButton btnClear;
    private JButton btnEnter;
    private JTextField textFieldLogin;
    private JTextField textFieldPassword;


    public LogIN(String title){
        super(title);
        setSize(220, 130);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        btnEnter = new JButton("Войти");
        btnClear = new JButton("Очистить");
        textFieldLogin = new JTextField(9);
        textFieldPassword = new JTextField(9);
        textFieldLogin.setText("");
        textFieldPassword.setText("");
        JLabel labelLogin = new JLabel("Логин:");
        JLabel labelPassword = new JLabel("Пароль");

        setLayout(null);

        labelLogin.setBounds(10,10,60,20);
        labelPassword.setBounds(10,30,60,20);
        textFieldLogin.setBounds(80,10,115,20);
        textFieldPassword.setBounds(80,30,115,20);
        btnEnter.setBounds(10,60,90,20);
        btnClear.setBounds(105,60,90,20);

        btnClear.addActionListener(new ButtonActionListener());
        btnEnter.addActionListener(new ButtonActionListener());

        add(btnClear);
        add(btnEnter);
        add(textFieldLogin);
        add(textFieldPassword);
        add(labelLogin);
        add(labelPassword);
    }


    public void setClientSendStream(ObjectOutputStream clientSendStream) {
        this.clientSendStream = clientSendStream;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public class ButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == btnClear) {
                textFieldLogin.setText(null);
                textFieldPassword.setText(null);
            } else if (e.getSource() == btnEnter) {
                ArrayList<Object> stringArrayList = new ArrayList<>();
                stringArrayList.add(textFieldLogin.getText());
                stringArrayList.add(textFieldPassword.getText());
                message.setMessageArray(stringArrayList);
                message.setCommand(Message.cmd.LogIn);
                try {
                    clientSendStream.writeObject(message);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
