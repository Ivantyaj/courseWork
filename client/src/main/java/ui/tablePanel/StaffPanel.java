package ui.tablePanel;

import Message.Message;
import TextField.StrTextField;
import ui.DatePanel;
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
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class StaffPanel extends JPanel implements SocketGuiInterface {

    private String[] columnName = {
            "id",
            "Сумма з/п",
            "Процент отчислений",
            "Дата"
    };

    private ObjectOutputStream clientSendStream;
    private Message message;

    private JTable table;
    private JTabbedPane tabbedPane;


    private JButton btnTabDelete;

    private JPanel tabDeletePanel;
    private JPanel tabInsertPanel;

    private JFormattedTextField ftfFName;
    private JFormattedTextField ftfSName;


    private JButton btnTabAdd;
    private JButton btnTabRedact;

    private DatePanel datePanel;
    private int id;


    public StaffPanel(ObjectOutputStream css, Message mes) {
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

        tabDeletePanel = new JPanel();
        tabDeletePanel.setLayout(null);
        btnTabDelete = new JButton("Удалить");
        btnTabDelete.addActionListener(new StaffPanel.ButtonActionListener());
        btnTabDelete.setBounds(10, 65, 120, 35);

        JLabel labelDelete = new JLabel("Выберите запись/записи для удаления");
        labelDelete.setBounds(10, 10, 300, 30);

        tabDeletePanel.add(labelDelete);
        tabDeletePanel.add(btnTabDelete);

        tabInsertPanel = new JPanel();
        tabInsertPanel.setLayout(null);

        btnTabAdd = new JButton("Добавить");
        btnTabAdd.addActionListener(new ButtonActionListener());
        btnTabAdd.setBounds(300, 70, 100, 20);

        btnTabRedact = new JButton("Изменить");
        btnTabRedact.addActionListener(new ButtonActionListener());
        btnTabRedact.setBounds(300, 90, 100, 20);

        ftfFName = new StrTextField();
        ftfFName.setBounds(130, 5, 90, 20);

        ftfSName = new StrTextField();
        ftfSName.setBounds(130, 35, 90, 20);

        datePanel = new DatePanel();
        datePanel.setBounds(130,55,90,20);

        JLabel lbSumSalary = new JLabel("Cумма з/п");
        JLabel lbGoverment = new JLabel("Процент отчислений");
        JLabel lbDate = new JLabel("Дата гггг-мм-дд");

        lbSumSalary.setBounds(5, 5, 130, 20);
        lbGoverment.setBounds(5, 30, 130, 20);
        lbDate.setBounds(5, 55, 130, 20);

        tabInsertPanel.add(lbSumSalary);
        tabInsertPanel.add(lbGoverment);
        tabInsertPanel.add(lbDate);

        tabInsertPanel.add(btnTabAdd);
        tabInsertPanel.add(btnTabRedact);
        tabInsertPanel.add(datePanel);
        tabInsertPanel.add(ftfFName);
        tabInsertPanel.add(ftfSName);


        tabbedPane.addTab("Добавить", tabInsertPanel);

        tabbedPane.addTab("Удалить", tabDeletePanel);

        tabbedPane.setBounds(10, 360, 890, 310);
        tabbedPane.addChangeListener(new StaffPanel.TabActionListener());

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
                    message.setCommand(Message.cmd.StaffDelete);
                    try {
                        clientSendStream.writeObject(message);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            if (e.getSource() == btnTabAdd) {
                sendData(Message.cmd.StaffAdd);
            }
            if (e.getSource() == btnTabRedact){
                sendData(Message.cmd.StaffRedact);
            }

            message = new Message();
            message.setCommand(Message.cmd.StaffRequest);
            try {
                clientSendStream.writeObject(message);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void sendData(Message.cmd cmd){

            ArrayList<Object> addData = new ArrayList<>();
            addData.add(String.valueOf(id));
            addData.add(ftfFName.getText());
            addData.add(ftfSName.getText());
            addData.add(datePanel.getText());

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
                ftfFName.setValue(model.getValueAt(selectedRow, 1));
                datePanel.setValue(model.getValueAt(selectedRow,3));
                ftfSName.setValue(model.getValueAt(selectedRow, 2));
            }

        }
    }
}