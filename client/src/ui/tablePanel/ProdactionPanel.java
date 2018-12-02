package ui.tablePanel;

import Message.Message;
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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ProdactionPanel extends JPanel implements SocketGuiInterface {

    String[] columnName = {
            "id",
            "кВт/ч",
            "Тариф",
            "Амортизация",
            "Дата"
    };

    ObjectOutputStream clientSendStream;
    Message message;

    JTable table;
    JTabbedPane tabbedPane;


    JButton btnTabDelete;

    JPanel tabDeletePanel;
    JPanel tabInsertPanel;
    //JPanel tabModifyPanel;

    JFormattedTextField ftfEnergy;
    JFormattedTextField ftfTariff;
    JFormattedTextField ftfAmortisation;
    DatePanel datePanel;


    JButton btnTabAdd;
    JButton btnTabRedact;
    private int id;


    public ProdactionPanel(ObjectOutputStream css, Message mes) {
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
        table.getSelectionModel().addListSelectionListener(new ProdactionPanel.TableSelectListener());
        table.setDefaultEditor(Object.class, null); //? /////////////////////////////////

        scrollPane.setBounds(10, 10, 890, 350);
        //add(table);


        tabbedPane = new JTabbedPane();

        tabDeletePanel = new JPanel();
        tabDeletePanel.setLayout(null);
        btnTabDelete = new JButton("Удалить");
        btnTabDelete.addActionListener(new ProdactionPanel.ButtonActionListener());
        btnTabDelete.setBounds(10, 50, 90, 20);
        tabDeletePanel.add(btnTabDelete);

        tabInsertPanel = new JPanel();
        tabInsertPanel.setLayout(null);

        btnTabAdd = new JButton("Добавить");
        btnTabAdd.addActionListener(new ProdactionPanel.ButtonActionListener());
        btnTabAdd.setBounds(300, 70, 100, 20);

        btnTabRedact = new JButton("Изменить");
        btnTabRedact.addActionListener(new ProdactionPanel.ButtonActionListener());
        btnTabRedact.setBounds(300, 90, 100, 20);
        //tabModifyPanel = new JPanel();

        ftfEnergy = new JFormattedTextField();
        ftfEnergy.setBounds(100, 5, 90, 20);
        //ftfName.addKeyListener(new TftCaractersListener());

        ftfTariff = new JFormattedTextField();
        ftfTariff.setBounds(100, 35, 90, 20);

        ftfAmortisation = new JFormattedTextField();
        ftfAmortisation.setBounds(100, 65, 90, 20);

        datePanel = new DatePanel();
        datePanel.setBounds(100, 80, 90, 20);

        JLabel lbEnergy = new JLabel("Затраты энергии");
        JLabel lbTariff = new JLabel("Тариф");
        JLabel lbAmortisation = new JLabel("Амортизация");
        JLabel lbDate = new JLabel("Дата гггг-мм-дд");

        lbEnergy.setBounds(5, 5, 90, 20);
        lbTariff.setBounds(5, 30, 90, 20);
        lbAmortisation.setBounds(5, 55, 90, 20);
        lbDate.setBounds(5, 80, 90, 20);

        tabInsertPanel.add(lbEnergy);
        tabInsertPanel.add(lbTariff);
        tabInsertPanel.add(lbAmortisation);
        tabInsertPanel.add(lbDate);

        tabInsertPanel.add(btnTabAdd);
        tabInsertPanel.add(btnTabRedact);
        tabInsertPanel.add(datePanel);
        tabInsertPanel.add(ftfEnergy);
        tabInsertPanel.add(ftfTariff);
        tabInsertPanel.add(ftfAmortisation);


        tabbedPane.addTab("Добавить", tabInsertPanel);

        JLabel labelDelete = new JLabel("Выберите запись/записи для удаления");
        labelDelete.setBounds(10, 10, 200, 30);

        tabDeletePanel.add(labelDelete);

        tabbedPane.addTab("Удалить", tabDeletePanel);
        //tabbedPane.addTab("Изменить", tabModifyPanel);

        tabbedPane.setBounds(10, 360, 890, 310);
        tabbedPane.addChangeListener(new ProdactionPanel.TabActionListener());

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
                    message.setCommand(Message.cmd.ProdactionDelete);
                    try {
                        clientSendStream.writeObject(message);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            if (e.getSource() == btnTabAdd) {
                setSendData(Message.cmd.ProdactionAdd);
            }
            if (e.getSource() == btnTabRedact) {
                setSendData(Message.cmd.ProdactionRedact);
            }

            message = new Message();
            message.setCommand(Message.cmd.ProdactionRequest);
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
        addData.add(ftfEnergy.getText());
        addData.add(ftfTariff.getText());
        addData.add(ftfAmortisation.getText());
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
                ftfEnergy.setValue(model.getValueAt(selectedRow, 1));
                ftfTariff.setValue(model.getValueAt(selectedRow, 2));
                ftfAmortisation.setValue(model.getValueAt(selectedRow, 3));
                datePanel.setValue(model.getValueAt(selectedRow, 4));
            }

        }
    }
}
