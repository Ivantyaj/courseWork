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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AccessoriesPanel extends JPanel implements SocketGuiInterface {

    String[] columnName = {
            "id",
            "Название",
            "Количество",
            "Цена"
    };

    ObjectOutputStream clientSendStream;
    Message message;

    JTable table;
    JTabbedPane tabbedPane;


    JButton btnTabDelete;

    JPanel tabDeletePanel;
    JPanel tabInsertPanel;
    //JPanel tabModifyPanel;

    JFormattedTextField ftfName;
    JFormattedTextField ftfCount;
    JFormattedTextField ftfPrice;


    JButton btnTabAdd;
    JButton btnTabRedact;
    private int id;


    public AccessoriesPanel(ObjectOutputStream css, Message mes) {
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
        table.getSelectionModel().addListSelectionListener(new AccessoriesPanel.TableSelectListener());
        table.setDefaultEditor(Object.class, null); //? /////////////////////////////////

        scrollPane.setBounds(10, 10, 890, 350);
        //add(table);


        tabbedPane = new JTabbedPane();

        tabDeletePanel = new JPanel();
        tabDeletePanel.setLayout(null);
        btnTabDelete = new JButton("Удалить");
        btnTabDelete.addActionListener(new AccessoriesPanel.ButtonActionListener());
        btnTabDelete.setBounds(10, 50, 90, 20);
        tabDeletePanel.add(btnTabDelete);

        tabInsertPanel = new JPanel();
        tabInsertPanel.setLayout(null);

        btnTabAdd = new JButton("Добавить");
        btnTabAdd.addActionListener(new AccessoriesPanel.ButtonActionListener());
        btnTabAdd.setBounds(300, 70, 100, 20);

        btnTabRedact = new JButton("Изменить");
        btnTabRedact.addActionListener(new AccessoriesPanel.ButtonActionListener());
        btnTabRedact.setBounds(300, 90, 100, 20);
        //tabModifyPanel = new JPanel();

        ftfName = new JFormattedTextField();
        ftfName.setBounds(100, 5, 90, 20);
        //ftfName.addKeyListener(new TftCaractersListener());

        ftfCount = new JFormattedTextField();
        ftfCount.setBounds(100, 35, 90, 20);

        ftfPrice = new JFormattedTextField();
        ftfPrice.setBounds(100, 65, 90, 20);

        JLabel lbName = new JLabel("Название пакета");
        JLabel lbCount = new JLabel("Количество");
        JLabel lbPrice = new JLabel("Цена за пакет");

        lbName.setBounds(5, 5, 90, 20);
        lbCount.setBounds(5, 30, 90, 20);
        lbPrice.setBounds(5, 55, 90, 20);

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
        //tabbedPane.addTab("Изменить", tabModifyPanel);

        tabbedPane.setBounds(10, 360, 890, 310);
        tabbedPane.addChangeListener(new AccessoriesPanel.TabActionListener());

        add(scrollPane);
        add(tabbedPane);

    }

    final static String DATE_FORMAT = "yyyy-MM-dd";

    public static boolean isDateValid(String date) {
        try {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
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
                    message.setCommand(Message.cmd.AccessoriesDelete);
                    try {
                        clientSendStream.writeObject(message);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            if (e.getSource() == btnTabAdd) {

                    ArrayList<Object> addData = new ArrayList<>();
                    addData.add(ftfName.getText());
                    addData.add(ftfCount.getText());
                    addData.add(ftfPrice.getText());

                    message = new Message();

                    message.setMessageArray(addData);
                    message.setCommand(Message.cmd.AccessoriesAdd);
                    try {
                        clientSendStream.writeObject(message);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

            }

            message = new Message();
            message.setCommand(Message.cmd.AccessoriesRequest);
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
                ftfName.setValue(model.getValueAt(selectedRow, 1));
                ftfCount.setValue(model.getValueAt(selectedRow, 2));
                ftfPrice.setValue(model.getValueAt(selectedRow, 3));
            }

        }
    }
}
