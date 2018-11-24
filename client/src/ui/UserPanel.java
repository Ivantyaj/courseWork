package ui;

import Message.Message;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class UserPanel extends JPanel  implements SocketGuiInterface {
    String[] columnName = {
            "id",
            "Логин",
            "Пароль",
            "Роль"
    };

    ObjectOutputStream clientSendStream;
    Message message;

    JTable table;
    JTabbedPane tabbedPane;


    JButton btnTabDelete;

    JPanel tabDeletePanel;
    JPanel tabInsertPanel;
    JPanel tabModifyPanel;


    public UserPanel(ObjectOutputStream css, Message mes){
        setClientSendStream(css);
        setMessage(mes);

        setSize(900,680);
        setLayout(null);

        table = new JTable(new Object[][] {}, columnName);
        JScrollPane scrollPane = new JScrollPane(table);

        //table.setSize(400,50);
        table.setCellSelectionEnabled(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowSelectionAllowed(true);

        scrollPane.setBounds(10,10,890,350);
        //add(table);




        tabbedPane = new JTabbedPane();

        tabDeletePanel = new JPanel();
        tabDeletePanel.setLayout(null);
        btnTabDelete = new JButton("Удалить");
        btnTabDelete.addActionListener(new UserPanel.ButtonActionListener());
        btnTabDelete.setBounds(10,10,90,20);
        tabDeletePanel.add(btnTabDelete);

        tabInsertPanel = new JPanel();
        tabModifyPanel = new JPanel();



        tabbedPane.addTab("Добавить", tabInsertPanel);
        tabbedPane.addTab("Удалить", tabDeletePanel);
        tabbedPane.addTab("Изменить", tabModifyPanel);

        tabbedPane.setBounds(10,360,890,310);
        tabbedPane.addChangeListener(new UserPanel.TabActionListener());

        add(scrollPane, BorderLayout.CENTER);
        add(tabbedPane);

    }

    public void setClientSendStream(ObjectOutputStream clientSendStream) {
        this.clientSendStream = clientSendStream;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public class ButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == btnTabDelete) {
                int[] selectedRows = table.getSelectedRows();
                ArrayList<Object> listID = new ArrayList<>();
                for (int id: selectedRows){
                    listID.add(table.getValueAt(id,0));
                }
                if(!listID.isEmpty()) {
                    message.setMessageArray(listID);
                    message.setCommand(Message.cmd.UserDelete);
                    try {
                        clientSendStream.writeObject(message);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }

                message = new Message();
                message.setCommand(Message.cmd.UserRequest);
                try {
                    clientSendStream.writeObject(message);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
//
//                message.setCommand(Message.cmd.UserRequest);
//                try {
//                    clientSendStream.writeObject(new Message());
//                } catch (IOException e1) {
//                    e1.printStackTrace();
//                }
            }
//            else if (e.getSource() == btnEnter) {
//                ArrayList<Object> stringArrayList = new ArrayList<>();
//                stringArrayList.add(textFieldLogin.getText());
//                stringArrayList.add(textFieldPassword.getText());
//                message.setMessageArray(stringArrayList);
//                message.setCommand(Message.cmd.LogIn);
//                try {
//                    clientSendStream.writeObject(message);
//                } catch (IOException e1) {
//                    e1.printStackTrace();
//                }
//            }
        }
    }

    public class TabActionListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            JPanel sourcePanel = (JPanel) ((JTabbedPane)e.getSource()).getSelectedComponent();
            if (sourcePanel == tabDeletePanel){
                table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            } else if(sourcePanel == tabInsertPanel){
                table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            } else if (sourcePanel == tabModifyPanel) {
                table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            }
        }
    }

    void setSourse(Object[][] data){
        table.setModel(new DefaultTableModel(data, columnName));
    }
}