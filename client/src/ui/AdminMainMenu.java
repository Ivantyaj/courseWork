package ui;

import BDTable.User;
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

    DB dbUI;
    EvaluateUI evaluateUI;

    User user;


    public AdminMainMenu(ObjectOutputStream objectOutputStream, Message mes){
        super("Администратор");

        setClientSendStream(objectOutputStream);
        setMessage(mes);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        user = new User();
        user.setRole(User.Role.USER);


        setSize(150, 200);

        btnUser = new JButton("Расчет затрат");
        btnDBUser = new JButton("Войти в базу данных");
        btnBack = new JButton("Назад");

        dbUI = new DB(clientSendStream, message);
        dbUI.setVisible(false);
        dbUI.setLocationRelativeTo(null);

        evaluateUI = new EvaluateUI(clientSendStream, message);
        evaluateUI.setVisible(false);
        evaluateUI.setLocationRelativeTo(null);
        evaluateUI.setUser(user);

        //setDefaultCloseOperation(HIDE_ON_CLOSE);

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
                if(user.getRole() == User.Role.USER){
                    JOptionPane.showMessageDialog(null, "Недостаточно прав для доступа!\nОбратитесь к администратору!");
                    return;
                }

                //message.setMessageArray(stringArrayList);
                message = new Message();
                message.setCommand(Message.cmd.UserRequest);
                try {
                    clientSendStream.writeObject(message);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                dbUI.setVisible(true);

            } else if (e.getSource() == btnUser) {

                try {
                    evaluateUI.requestAll(message, clientSendStream);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                evaluateUI.setVisible(true);
            }
        }
    }

    public DB getDbUI() {
        return dbUI;
    }

    public EvaluateUI getEvaluateUI() {
        return evaluateUI;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        evaluateUI.setUser(user);
    }
}
