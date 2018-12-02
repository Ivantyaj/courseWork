package ui.evaluatePanel;

import BDTable.User;
import Message.Message;
import ui.DatePanel;
import ui.SocketGuiInterface;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MainReportPanel extends JPanel implements SocketGuiInterface {
    private String[] columnNameStaff = {
            "id",
            "Сумма з/п",
            "Процент отчислений",
            "Дата"
    };
    private String[] columnNameRaw = {
            "id",
            "Назначение",
            "Количество",
            "Цена"
    };
    private String[] columnNameProdaction = {
            "id",
            "кВт/ч",
            "Тариф",
            "Амортизация",
            "Дата"
    };

    private JTable tableStaff;
    private JTable tableRaw;
    private JTable tableProdaction;


    private ObjectOutputStream clientSendStream;
    private Message message;

    private JButton btnEvaluate;

    private DatePanel datePanel;
    private JFormattedTextField ftfTransportSum;
    private JFormattedTextField ftfAddSum;

    private JCheckBox cbDefect;
    private JCheckBox cbTransport;
    private JCheckBox cbAdditional;

    private JComboBox comboBox;

    //private int idUser;
    private User user;

    public MainReportPanel(ObjectOutputStream css, Message mes) {
        setClientSendStream(css);
        setMessage(mes);

        user = new User();
        user.setId(-1);
        user.setRole(User.Role.FAIL);

        setSize(900, 680);
        setLayout(null);

        tableStaff = new JTable(new Object[][]{}, columnNameStaff);
        JScrollPane scrollPaneStaff = new JScrollPane(tableStaff);

        tableStaff.setCellSelectionEnabled(false);
        tableStaff.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableStaff.setRowSelectionAllowed(true);
        tableStaff.getSelectionModel().addListSelectionListener(new MainReportPanel.TableSelectListener());
        tableStaff.setDefaultEditor(Object.class, null); //

        scrollPaneStaff.setBounds(10, 20, 890, 105);
        JLabel lbStaff = new JLabel("Персонал:");
        lbStaff.setBounds(10, 0, 100, 20);

        tableRaw = new JTable(new Object[][]{}, columnNameRaw);
        JScrollPane scrollPaneRaw = new JScrollPane(tableRaw);

        tableRaw.setCellSelectionEnabled(false);
        tableRaw.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableRaw.setRowSelectionAllowed(true);
        tableRaw.getSelectionModel().addListSelectionListener(new MainReportPanel.TableSelectListener());
        tableRaw.setDefaultEditor(Object.class, null); //

        scrollPaneRaw.setBounds(10, 145, 890, 105);
        JLabel lbRaw = new JLabel("Набор сырья:");
        lbRaw.setBounds(10, 125, 130, 20);

        tableProdaction = new JTable(new Object[][]{}, columnNameProdaction);
        JScrollPane scrollPaneProdaction = new JScrollPane(tableProdaction);

        tableProdaction.setCellSelectionEnabled(false);
        tableProdaction.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableProdaction.setRowSelectionAllowed(true);
        tableProdaction.getSelectionModel().addListSelectionListener(new MainReportPanel.TableSelectListener());
        tableProdaction.setDefaultEditor(Object.class, null); //

        scrollPaneProdaction.setBounds(10, 270, 890, 105);
        JLabel lbProdaction = new JLabel("Производство:");
        lbProdaction.setBounds(10, 250, 100, 20);

        btnEvaluate = new JButton("Рассчитать");
        btnEvaluate.setBounds(10, 630, 100, 25);
        btnEvaluate.addActionListener(new ButtonActionListener());

        JLabel lbDate = new JLabel("Дата: ");
        lbDate.setBounds(10, 380, 80, 20);

        datePanel = new DatePanel();
        datePanel.setBounds(30, 400, 90, 20);

        cbDefect = new JCheckBox("Учитывать возможный брак?");
        cbDefect.setBounds(10, 430, 200, 20);
        cbDefect.addItemListener(new CheckBoxActionListener());

        String[] items = {
                "5%",
                "10%",
                "15%"

        };
        comboBox = new JComboBox(items);
        comboBox.setBounds(30, 460, 50, 20);
        comboBox.setEditable(false);
        comboBox.setEnabled(false);

        cbTransport = new JCheckBox("Вносить расходы на перевозку?");
        cbTransport.setBounds(10, 490, 250, 20);
        cbTransport.addItemListener(new CheckBoxActionListener());

        ftfTransportSum = new JFormattedTextField();
        ftfTransportSum.setBounds(30, 520, 90, 20);
        ftfTransportSum.setEnabled(false);

        cbAdditional = new JCheckBox("Добавить сумму неучтенных расходов");
        cbAdditional.setBounds(10, 550, 250, 20);
        cbAdditional.addItemListener(new CheckBoxActionListener());

        ftfAddSum = new JFormattedTextField();
        ftfAddSum.setBounds(30, 580, 90, 20);
        ftfAddSum.setEnabled(false);

        add(scrollPaneStaff);
        add(scrollPaneRaw);
        add(scrollPaneProdaction);

        add(lbStaff);
        add(lbRaw);
        add(lbProdaction);
        add(lbDate);

        add(btnEvaluate);
        add(datePanel);
        add(ftfTransportSum);
        add(ftfAddSum);
        add(comboBox);
        add(cbTransport);
        add(cbDefect);
        add(cbAdditional);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getIdUser() {
        return user.getId();
    }

    public void setClientSendStream(ObjectOutputStream clientSendStream) {
        this.clientSendStream = clientSendStream;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public void setSourseStaff(Object[][] data) {
        tableStaff.setModel(new DefaultTableModel(data, columnNameStaff));
    }

    public void setSourseRaw(Object[][] data) {
        tableRaw.setModel(new DefaultTableModel(data, columnNameRaw));
    }

    public void setSourseProdaction(Object[][] data) {
        tableProdaction.setModel(new DefaultTableModel(data, columnNameProdaction));
    }

    public class TableSelectListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getSource() == tableStaff) {
                int selectedRow = tableStaff.getSelectedRow();
                if (selectedRow >= 0) {
                    TableModel model = tableStaff.getModel();
                }
            }
        }
    }

    public class ButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == btnEvaluate) {
                if(user.getRole() == User.Role.USER){
                    JOptionPane.showMessageDialog(null, "Недостаточно прав для доступа!\nОбратитесь к администратору!");
                    return;
                }
                if(datePanel.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Выберите дату!");
                    return;
                }
                ArrayList<Object> listID = new ArrayList<>();

                int selectStaff = tableStaff.getSelectedRow();
                int selectRaw = tableRaw.getSelectedRow();
                int selectProdaction = tableProdaction.getSelectedRow();
                if (selectStaff < 0 || selectRaw < 0 || selectProdaction < 0) {
                    JOptionPane.showMessageDialog(null, "Выберите все данные для расчета!");
                } else {
                    listID.add(tableStaff.getValueAt(selectStaff, 0));
                    listID.add(tableRaw.getValueAt(selectRaw, 0));
                    listID.add(tableProdaction.getValueAt(selectProdaction, 0));
                    listID.add(String.valueOf(user.getId()));
                    listID.add(datePanel.getText());

                    if (cbDefect.isSelected()) {
                        int index = comboBox.getSelectedIndex();
                        Float persent;
                        switch (index) {
                            case 0:
                                persent = (float) 1.05;
                                break;
                            case 1:
                                persent = (float) 1.10;
                                break;
                            case 2:
                                persent = (float) 1.15;
                                break;
                            default:
                                persent = (float) 1;
                                break;
                        }

                        listID.add(String.valueOf(persent));
                    } else {
                        listID.add("1");
                    }
                    if (cbTransport.isSelected()) {
                        listID.add(ftfTransportSum.getText());
                    } else {
                        listID.add("0");
                    }
                    if (cbAdditional.isSelected()) {
                        listID.add(ftfAddSum.getText());
                    } else {
                        listID.add("0");
                    }


                    message = new Message();
                    if (!listID.isEmpty()) {
                        message.setMessageArray(listID);
                        message.setCommand(Message.cmd.Evaluate);
                        try {
                            clientSendStream.writeObject(message);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        }

    }

    public class CheckBoxActionListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (cbDefect.isSelected()) {
                comboBox.setEnabled(true);
            } else {
                comboBox.setEnabled(false);
            }
            if (cbTransport.isSelected()) {
                ftfTransportSum.setEnabled(true);
            } else {
                ftfTransportSum.setEnabled(false);
            }
            if (cbAdditional.isSelected()) {
                ftfAddSum.setEnabled(true);
            } else {
                ftfAddSum.setEnabled(false);
            }
        }
    }
}
