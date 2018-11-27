package ui;

import Message.Message;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LogIN extends JFrame implements SocketGuiInterface{

    ObjectOutputStream clientSendStream;
    Message message;

    JButton btnClear;
    JButton btnEnter;
    JTextField textFieldLogin;
    JTextField textFieldPassword;
    JLabel labelLogin;
    JLabel labelPassword;




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
        labelLogin = new JLabel("Логин:");
        labelPassword = new JLabel("Пароль");

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
