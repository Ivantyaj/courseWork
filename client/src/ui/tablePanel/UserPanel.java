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
import javax.swing.text.MaskFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.util.ArrayList;

public class UserPanel extends JPanel implements SocketGuiInterface {
    private String[] columnName = {
            "id",
            "Логин",
            "Пароль",
            "Роль",
            "Имя",
            "Фамилия",
            "Телефон"
    };

    private ObjectOutputStream clientSendStream;
    private Message message;

    private JTable table;
    private JTabbedPane tabbedPane;


    private JPanel tabDeletePanel;
    private JPanel tabInsertPanel;

    private JFormattedTextField ftfLogin;
    private JFormattedTextField ftfPassword;

    private JFormattedTextField ftfFName;
    private JFormattedTextField ftfSName;
    private JFormattedTextField ftfPhone;
    private JComboBox comboBox;

    private JButton btnTabAdd;
    private JButton btnTabRedact;
    private JButton btnTabDelete;

    private int id;


    public UserPanel(ObjectOutputStream css, Message mes) {
        setClientSendStream(css);
        setMessage(mes);

        setSize(900, 680);
        setLayout(null);

        table = new JTable(new Object[][]{}, columnName);
        JScrollPane scrollPane = new JScrollPane(table);

        table.setCellSelectionEnabled(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowSelectionAllowed(true);
        table.getSelectionModel().addListSelectionListener(new TableSelectListener());
        table.setDefaultEditor(Object.class, null);

        scrollPane.setBounds(10, 10, 890, 350);

        tabbedPane = new JTabbedPane();

        tabInsertPanel = new JPanel();
        tabInsertPanel.setLayout(null);

        btnTabAdd = new JButton("Зарегестрировать");
        btnTabAdd.addActionListener(new ButtonActionListener());
        btnTabAdd.setBounds(300, 70, 140, 20);

        btnTabRedact = new JButton("Изменить");
        btnTabRedact.addActionListener(new ButtonActionListener());
        btnTabRedact.setBounds(300, 100, 140, 20);
        //tabModifyPanel = new JPanel();

        ftfLogin = new JFormattedTextField();
        ftfLogin.setBounds(100, 5, 120, 20);
        //ftfName.addKeyListener(new TftCaractersListener());

        ftfPassword = new JFormattedTextField();
        ftfPassword.setBounds(100, 45, 120, 20);
        //ftfCount.addKeyListener(new TftCaractersListener());

        String[] items = {
                "Администратор",
                "Пользователь",
        };
        comboBox = new JComboBox(items);
        comboBox.setBounds(100, 65, 120, 20);

        JLabel lbLogin = new JLabel("Логин");
        JLabel lbPassword = new JLabel("Пароль");
        JLabel lbRole = new JLabel("Роль");

        lbLogin.setBounds(5, 5, 120, 20);
        lbPassword.setBounds(5, 40, 120, 20);
        lbRole.setBounds(5, 65, 120, 20);

        MaskFormatter mf = null;
        try {
            mf = new MaskFormatter("+375(" + "##" + ")" + "###-##-##");
            mf.setPlaceholderCharacter('_');
        } catch (ParseException e) {
            e.printStackTrace();
        }

        JLabel lbFName = new JLabel("Имя");
        JLabel lbSName = new JLabel("Фамилия");
        JLabel lbPhone = new JLabel("Телефон");

        lbFName.setBounds(5, 90, 120, 20);
        lbSName.setBounds(5, 115, 120, 20);
        lbPhone.setBounds(5, 140, 120, 20);

        ftfPhone = new JFormattedTextField(mf);
        ftfPhone.setBounds(100, 140, 120, 20);
        ftfPhone.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!((c >= '0') && (c <= '9') ||
                        (c == KeyEvent.VK_BACK_SPACE) ||
                        (c == KeyEvent.VK_DELETE))) {
                    JOptionPane.showMessageDialog(null, "Некорректный ввод");
                    e.consume();
                }
            }
        });

        ftfFName = new JFormattedTextField();
        ftfFName.setBounds(100, 115, 120, 20);

        ftfSName = new JFormattedTextField();
        ftfSName.setBounds(100, 90, 120, 20);

        tabInsertPanel.add(lbFName);
        tabInsertPanel.add(lbSName);
        tabInsertPanel.add(lbPhone);
        tabInsertPanel.add(ftfFName);
        tabInsertPanel.add(ftfSName);
        tabInsertPanel.add(ftfPhone);

        tabInsertPanel.add(lbLogin);
        tabInsertPanel.add(lbPassword);
        tabInsertPanel.add(lbRole);

        tabInsertPanel.add(btnTabAdd);
        tabInsertPanel.add(btnTabRedact);
        tabInsertPanel.add(comboBox);
        tabInsertPanel.add(ftfLogin);
        tabInsertPanel.add(ftfPassword);


        tabbedPane.addTab("Добавить", tabInsertPanel);

        tabDeletePanel = new JPanel();
        tabDeletePanel.setLayout(null);
        btnTabDelete = new JButton("Удалить");
        btnTabDelete.addActionListener(new UserPanel.ButtonActionListener());
        btnTabDelete.setBounds(10, 65, 120, 35);

        JLabel labelDelete = new JLabel("Выберите запись/записи для удаления");
        labelDelete.setBounds(10, 10, 300, 30);

        tabDeletePanel.add(labelDelete);
        tabDeletePanel.add(btnTabDelete);

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

                int dialogResult = JOptionPane.showConfirmDialog(tabDeletePanel,
                    "Вы уверены?\r\nЭто действие необратимо!",
                    "Удаление",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
                if (dialogResult != JOptionPane.YES_OPTION){
                    return;
                }
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
            }
            if (e.getSource() == btnTabAdd) {
                setSendData(Message.cmd.UserAdd);
            }
            if (e.getSource() == btnTabRedact) {
                setSendData(Message.cmd.UserRedact);
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

    private void setSendData(Message.cmd cmd) {
        ArrayList<Object> addData = new ArrayList<>();
        addData.add(String.valueOf(id));
        addData.add(ftfLogin.getText());
        addData.add(ftfPassword.getText());

        if (comboBox.getSelectedIndex() == 0)
            addData.add(String.valueOf(User.Role.ADMIN));
        else
            addData.add(String.valueOf(User.Role.USER));

        addData.add(ftfFName.getText());
        addData.add(ftfSName.getText());
        addData.add(ftfPhone.getText());

        message = new Message();

        message.setMessageArray(addData);
        message.setCommand(cmd);
        try {
            clientSendStream.writeObject(message);
        } catch (IOException e1) {
            e1.printStackTrace();
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

    public class TableSelectListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                TableModel model = table.getModel();
                id = Integer.parseInt((String) model.getValueAt(selectedRow, 0));
                ftfLogin.setValue(model.getValueAt(selectedRow, 1));
                ftfPassword.setValue(model.getValueAt(selectedRow, 2));
                if (String.valueOf(User.Role.ADMIN) == model.getValueAt(selectedRow, 3))
                    comboBox.setSelectedIndex(0);
                else
                    comboBox.setSelectedIndex(1);
                ftfFName.setValue(model.getValueAt(selectedRow,4));
                ftfSName.setValue(model.getValueAt(selectedRow,5));
                ftfPhone.setValue(model.getValueAt(selectedRow,6));
            }

        }
    }
}