package ui.tablePanel;

import Message.Message;
import BDTable.User;
import ui.SocketGuiInterface;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class UserPanel extends JPanel implements SocketGuiInterface {
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
    //JPanel tabModifyPanel;

    JFormattedTextField ftfFName;
    JFormattedTextField ftfSName;
    JComboBox comboBox;

    JButton btnTabAdd;
    JButton btnTabRedact;
    private int id;


    public UserPanel(ObjectOutputStream css, Message mes) {
        setClientSendStream(css);
        setMessage(mes);

        setSize(900, 680);
        setLayout(null);

        table = new JTable(new Object[][]{}, columnName);
        JScrollPane scrollPane = new JScrollPane(table);

        //table.setSize(400,50);
        table.setCellSelectionEnabled(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowSelectionAllowed(true);
        table.getSelectionModel().addListSelectionListener(new TableSelectListener());
        table.setDefaultEditor(Object.class, null); //? /////////////////////////////////

        scrollPane.setBounds(10, 10, 890, 350);
        //add(table);


        tabbedPane = new JTabbedPane();

        tabDeletePanel = new JPanel();
        tabDeletePanel.setLayout(null);
        btnTabDelete = new JButton("Удалить");
        btnTabDelete.addActionListener(new UserPanel.ButtonActionListener());
        btnTabDelete.setBounds(10, 50, 90, 20);
        tabDeletePanel.add(btnTabDelete);

        tabInsertPanel = new JPanel();
        tabInsertPanel.setLayout(null);

        btnTabAdd = new JButton("Добавить");
        btnTabAdd.addActionListener(new ButtonActionListener());
        btnTabAdd.setBounds(300, 70, 100, 20);

        btnTabRedact = new JButton("Изменить");
        btnTabRedact.addActionListener(new ButtonActionListener());
        btnTabRedact.setBounds(300, 90, 100, 20);
        //tabModifyPanel = new JPanel();

        ftfFName = new JFormattedTextField();
        ftfFName.setBounds(100, 5, 90, 20);
        //ftfName.addKeyListener(new TftCaractersListener());

        ftfSName = new JFormattedTextField();
        ftfSName.setBounds(100, 35, 90, 20);
        //ftfCount.addKeyListener(new TftCaractersListener());

        String[] items = {
                "Администратор",
                "Пользователь",
        };
        comboBox = new JComboBox(items);
        comboBox.setBounds(100, 55, 90, 20);

        JLabel lbLogin = new JLabel("Логин");
        JLabel lbPassword = new JLabel("Пароль");
        JLabel lbRole = new JLabel("Роль");

        lbLogin.setBounds(5, 5, 90, 20);
        lbPassword.setBounds(5, 30, 90, 20);
        lbRole.setBounds(5, 55, 90, 20);

        tabInsertPanel.add(lbLogin);
        tabInsertPanel.add(lbPassword);
        tabInsertPanel.add(lbRole);

        tabInsertPanel.add(btnTabAdd);
        tabInsertPanel.add(btnTabRedact);
        tabInsertPanel.add(comboBox);
        tabInsertPanel.add(ftfFName);
        tabInsertPanel.add(ftfSName);


        tabbedPane.addTab("Добавить", tabInsertPanel);

        JLabel labelDelete = new JLabel("Выберите запись/записи для удаления");
        labelDelete.setBounds(10, 10, 200, 30);

        tabDeletePanel.add(labelDelete);

        tabbedPane.addTab("Удалить", tabDeletePanel);
        //tabbedPane.addTab("Изменить", tabModifyPanel);

        tabbedPane.setBounds(10, 360, 890, 310);
        tabbedPane.addChangeListener(new UserPanel.TabActionListener());

        add(scrollPane);
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
                for (int id : selectedRows) {
                    listID.add(table.getValueAt(id, 0));
                }
                message = new Message();
                if (!listID.isEmpty()) {
                    message.setMessageArray(listID);
                    message.setCommand(Message.cmd.UserDelete);
                    try {
                        clientSendStream.writeObject(message);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
//
//                message = new Message();
//                message.setCommand(Message.cmd.UserRequest);
//                try {
//                    clientSendStream.writeObject(message);
//                } catch (IOException e1) {
//                    e1.printStackTrace();
//                }
            }
            if (e.getSource() == btnTabAdd) {
                ArrayList<Object> addData = new ArrayList<>();
                addData.add(ftfFName.getText());
                addData.add(ftfSName.getText());

                if (comboBox.getSelectedIndex() == 0)
                    addData.add(String.valueOf(User.Role.ADMIN));
                else
                    addData.add(String.valueOf(User.Role.USER));

                message = new Message();

                message.setMessageArray(addData);
                message.setCommand(Message.cmd.UserAdd);
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
        }
    }

    public class TabActionListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            JPanel sourcePanel = (JPanel) ((JTabbedPane) e.getSource()).getSelectedComponent();
            if (sourcePanel == tabDeletePanel) {
                table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            } else if (sourcePanel == tabInsertPanel) {
                table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//            } else if (sourcePanel == tabModifyPanel) {
//                table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            }
        }
    }

    public void setSourse(Object[][] data) {
        table.setModel(new DefaultTableModel(data, columnName));
    }

    public class TftCaractersListener extends KeyAdapter {

        public void keyTyped(KeyEvent e) {
            char c = e.getKeyChar();
            if (!((c >= 'А') && (c <= 'я') ||
                    (c == KeyEvent.VK_BACK_SPACE) ||
                    (c == KeyEvent.VK_DELETE))) {
                JOptionPane.showMessageDialog(null, "Только русские символы!");
                e.consume();
            }
        }

    }

    public class TableSelectListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                TableModel model = table.getModel();
                id = Integer.parseInt((String) model.getValueAt(selectedRow, 0));
                ftfFName.setValue(model.getValueAt(selectedRow, 1));
                ftfSName.setValue(model.getValueAt(selectedRow, 2));
                if (String.valueOf(User.Role.ADMIN) == model.getValueAt(selectedRow, 3))
                    comboBox.setSelectedIndex(0);
                else
                    comboBox.setSelectedIndex(1);
            }

        }
    }
}