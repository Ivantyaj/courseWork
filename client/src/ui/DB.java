package ui;

import Message.Message;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class DB extends JFrame implements SocketGuiInterface {
//    String[] columnName = {
//      "id",
//      "Логин",
//      "Пароль",
//      "Роль"
//    };

    ObjectOutputStream clientSendStream;
    Message message;

    //JTable table;
    JTabbedPane tabbedPane;


    //JButton btnTabDelete;

    UserPanel tabUserPanel;
    StaffPanel tabStaffPanel;
//    JPanel tabInsertPanel;
//    JPanel tabModifyPanel;


    public DB(ObjectOutputStream objectOutputStream, Message mes){
        super("База данных");

        setClientSendStream(objectOutputStream);
        setMessage(mes);

        setSize(950,750);
        setResizable(false);
        setLayout(null);
        tabbedPane = new JTabbedPane();

        tabUserPanel = new UserPanel(clientSendStream, message);
        tabStaffPanel = new StaffPanel(clientSendStream, message);

        tabbedPane.addTab("Пользователи", tabUserPanel);
        tabbedPane.addTab("Штат", tabStaffPanel);


        tabbedPane.setBounds(5,5,920,700);
        tabbedPane.addChangeListener(new DB.TabActionListener());

        add(tabbedPane);
        //table = new JTable(data, columnName);
        //JScrollPane scrollPane = new JScrollPane(table);

        //table.setSize(400,50);
//        table.setCellSelectionEnabled(false);
//        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        table.setRowSelectionAllowed(true);
//
//        scrollPane.setBounds(10,10,400,80);
//        //add(table);
//
//
//        JPanel tablePanel = new JPanel();
//        tablePanel.setLayout(null);
//        //tablePanel.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//        tablePanel.add(scrollPane);
//        tablePanel.setSize(500,100);
//        //tablePanel.
//
//        //tablePanel.setBounds(10,10,480,500);
//
//
//
//        tabbedPane = new JTabbedPane();
//
//        tabDeletePanel = new JPanel();
//        tabDeletePanel.setLayout(null);
//        btnTabDelete = new JButton("Удалить");
//        btnTabDelete.addActionListener(new ButtonActionListener());
//        btnTabDelete.setBounds(5,5,90,20);
//        tabDeletePanel.add(btnTabDelete);
//
//        tabInsertPanel = new JPanel();
//        tabModifyPanel = new JPanel();
//
//
//
//        tabbedPane.addTab("Добавить", tabInsertPanel);
//        tabbedPane.addTab("Удалить", tabDeletePanel);
//        tabbedPane.addTab("Изменить", tabModifyPanel);
//
//        tabbedPane.setBounds(10,250,400,150);
//        tabbedPane.addChangeListener(new TabActionListener());
//
//
//
//        add(tablePanel);
//        add(tabbedPane);
//        //tablePanel.add(tablePanel);
//        //add(scrollPane);
    }

    public void setClientSendStream(ObjectOutputStream clientSendStream) {
        this.clientSendStream = clientSendStream;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public void setUserData(Object[][] data){
        tabUserPanel.setSourse(data);
    }
    public void setStaffData(Object[][] data){
        tabStaffPanel.setSourse(data);
    }


//    public class ButtonActionListener implements ActionListener {
//        public void actionPerformed(ActionEvent e) {
//
//            if (e.getSource() == btnTabDelete) {
//                int[] selectedRows = table.getSelectedRows();
//                ArrayList<Object> listID = new ArrayList<>();
//                for (int id: selectedRows){
//                    listID.add(table.getValueAt(id,0));
//                }
//                if(!listID.isEmpty()) {
//                    message.setMessageArray(listID);
//                    message.setCommand(Message.cmd.UserDelete);
//                    try {
//                        clientSendStream.writeObject(message);
//                    } catch (IOException e1) {
//                        e1.printStackTrace();
//                    }
//                }
//
//                message = new Message();
//                message.setCommand(Message.cmd.UserRequest);
//                try {
//                    clientSendStream.writeObject(message);
//                } catch (IOException e1) {
//                    e1.printStackTrace();
//                }
////
////                message.setCommand(Message.cmd.UserRequest);
////                try {
////                    clientSendStream.writeObject(new Message());
////                } catch (IOException e1) {
////                    e1.printStackTrace();
////                }
//            }
////            else if (e.getSource() == btnEnter) {
////                ArrayList<Object> stringArrayList = new ArrayList<>();
////                stringArrayList.add(textFieldLogin.getText());
////                stringArrayList.add(textFieldPassword.getText());
////                message.setMessageArray(stringArrayList);
////                message.setCommand(Message.cmd.LogIn);
////                try {
////                    clientSendStream.writeObject(message);
////                } catch (IOException e1) {
////                    e1.printStackTrace();
////                }
////            }
//        }
//    }
//
    public class TabActionListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            JPanel sourcePanel = (JPanel) ((JTabbedPane)e.getSource()).getSelectedComponent();
            if (sourcePanel == tabStaffPanel){
                message = new Message();
                message.setCommand(Message.cmd.StaffRequest);
                try {
                    clientSendStream.writeObject(message);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
