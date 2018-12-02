package ui.tablePanel;

import Message.Message;
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

public class RawPackagePanel extends JPanel implements SocketGuiInterface {

    private String[] columnName = {
            "id",
            "Назначение",
            "Количество",
            "Цена"
    };

    private ObjectOutputStream clientSendStream;
    private Message message;

    private JTable table;

    private JButton btnTabDelete;

    private JPanel tabDeletePanel;
    private JPanel tabInsertPanel;

    private JFormattedTextField ftfName;
    private JFormattedTextField ftfCount;
    private JFormattedTextField ftfPrice;

    private JButton btnTabAdd;
    private JButton btnTabRedact;
    private int id;


    public RawPackagePanel(ObjectOutputStream css, Message mes) {
        setClientSendStream(css);
        setMessage(mes);

        setSize(900, 680);
        setLayout(null);

        table = new JTable(new Object[][]{}, columnName);
        JScrollPane scrollPane = new JScrollPane(table);

        table.setCellSelectionEnabled(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowSelectionAllowed(true);
        table.getSelectionModel().addListSelectionListener(new RawPackagePanel.TableSelectListener());
        table.setDefaultEditor(Object.class, null);

        scrollPane.setBounds(10, 10, 890, 350);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabDeletePanel = new JPanel();
        tabDeletePanel.setLayout(null);
        btnTabDelete = new JButton("Удалить");
        btnTabDelete.addActionListener(new RawPackagePanel.ButtonActionListener());
        btnTabDelete.setBounds(10, 50, 90, 20);
        tabDeletePanel.add(btnTabDelete);

        tabInsertPanel = new JPanel();
        tabInsertPanel.setLayout(null);

        btnTabAdd = new JButton("Добавить");
        btnTabAdd.addActionListener(new RawPackagePanel.ButtonActionListener());
        btnTabAdd.setBounds(300, 70, 100, 20);

        btnTabRedact = new JButton("Изменить");
        btnTabRedact.addActionListener(new RawPackagePanel.ButtonActionListener());
        btnTabRedact.setBounds(300, 90, 100, 20);

        ftfName = new JFormattedTextField();
        ftfName.setBounds(130, 5, 90, 20);

        ftfCount = new JFormattedTextField();
        ftfCount.setBounds(130, 35, 90, 20);

        ftfPrice = new JFormattedTextField();
        ftfPrice.setBounds(130, 65, 90, 20);

        JLabel lbName = new JLabel("Назначение пакета");
        JLabel lbCount = new JLabel("Количество");
        JLabel lbPrice = new JLabel("Цена за пакет");

        lbName.setBounds(5, 5, 130, 20);
        lbCount.setBounds(5, 30, 130, 20);
        lbPrice.setBounds(5, 55, 130, 20);

        tabInsertPanel.add(lbName);
        tabInsertPanel.add(lbCount);
        tabInsertPanel.add(lbPrice);

        tabInsertPanel.add(btnTabAdd);
        tabInsertPanel.add(btnTabRedact);
        tabInsertPanel.add(ftfName);
        tabInsertPanel.add(ftfCount);
        tabInsertPanel.add(ftfPrice);

        tabbedPane.addTab("Добавить", tabInsertPanel);

        JLabel labelDelete = new JLabel("Выберите запись/записи для удаления");
        labelDelete.setBounds(10, 10, 200, 30);

        tabDeletePanel.add(labelDelete);

        tabbedPane.addTab("Удалить", tabDeletePanel);

        tabbedPane.setBounds(10, 360, 890, 310);
        tabbedPane.addChangeListener(new RawPackagePanel.TabActionListener());

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
                    message.setCommand(Message.cmd.RawDelete);
                    try {
                        clientSendStream.writeObject(message);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            if (e.getSource() == btnTabAdd) {
                setSendData(Message.cmd.RawAdd);
            }
            if (e.getSource() == btnTabRedact) {
                setSendData(Message.cmd.RawRedact);
            }

            message = new Message();
            message.setCommand(Message.cmd.RawRequest);
            try {
                clientSendStream.writeObject(message);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void setSendData(Message.cmd cmd){
        ArrayList<Object> addData = new ArrayList<Object>();
        addData.add(String.valueOf(id));
        addData.add(ftfName.getText());
        addData.add(ftfCount.getText());
        addData.add(ftfPrice.getText());

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
                ftfName.setValue(model.getValueAt(selectedRow, 1));
                ftfCount.setValue(model.getValueAt(selectedRow, 2));
                ftfPrice.setValue(model.getValueAt(selectedRow, 3));
            }

        }
    }
}
