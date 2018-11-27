package ui.evaluatePanel;

import Message.Message;
import org.jfree.chart.ChartPanel;
import ui.SocketGuiInterface;
import ui.graphics.chartPieUI;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.text.MaskFormatter;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainReportPanel extends JPanel implements SocketGuiInterface {
    private String[] columnNameStaff = {
            "id",
            "Сумма з/п",
            "Процент отчислений",
            "Дата"
    };
    private String[] columnNameAcceccories = {
            "id",
            "Название",
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
    private JTable tableAccessories;
    private JTable tableProdaction;


    private ObjectOutputStream clientSendStream;
    private Message message;

    private JButton btnEvaluate;

    private JFormattedTextField ftfDate;
    private JFormattedTextField ftfDebtSum;
    private JFormattedTextField ftfAddSum;

    private JCheckBox cbDefect;
    private JCheckBox cbDebt;
    private JCheckBox cbAdditional;

    private JComboBox comboBox;

    JLabel lbDebtSum;
    JLabel lbDebtPersent;

    private int idUser;


    private chartPieUI ui;
    ChartPanel chart;

    public MainReportPanel(ObjectOutputStream css, Message mes) {
        setClientSendStream(css);
        setMessage(mes);

        setSize(900, 680);
        setLayout(null);

        tableStaff = new JTable(new Object[][]{}, columnNameStaff);
        JScrollPane scrollPaneStaff = new JScrollPane(tableStaff);

        //table.setSize(400,50);
        tableStaff.setCellSelectionEnabled(false);
        tableStaff.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableStaff.setRowSelectionAllowed(true);
        tableStaff.getSelectionModel().addListSelectionListener(new MainReportPanel.TableSelectListener());
        tableStaff.setDefaultEditor(Object.class, null); //

        scrollPaneStaff.setBounds(10, 20, 890, 105);
        JLabel lbStaff = new JLabel("Персонал:");
        lbStaff.setBounds(10, 0, 100, 20);

        tableAccessories = new JTable(new Object[][]{}, columnNameAcceccories);
        JScrollPane scrollPaneAccessories = new JScrollPane(tableAccessories);

        //table.setSize(400,50);
        tableAccessories.setCellSelectionEnabled(false);
        tableAccessories.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableAccessories.setRowSelectionAllowed(true);
        tableAccessories.getSelectionModel().addListSelectionListener(new MainReportPanel.TableSelectListener());
        tableAccessories.setDefaultEditor(Object.class, null); //

        scrollPaneAccessories.setBounds(10, 145, 890, 105);
        JLabel lbAccessories = new JLabel("Сырье:");
        lbAccessories.setBounds(10, 125, 100, 20);

        tableProdaction = new JTable(new Object[][]{}, columnNameProdaction);
        JScrollPane scrollPaneProdaction = new JScrollPane(tableProdaction);

        //table.setSize(400,50);
        tableProdaction.setCellSelectionEnabled(false);
        tableProdaction.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableProdaction.setRowSelectionAllowed(true);
        tableProdaction.getSelectionModel().addListSelectionListener(new MainReportPanel.TableSelectListener());
        tableProdaction.setDefaultEditor(Object.class, null); //

        scrollPaneProdaction.setBounds(10, 270, 890, 105);
        JLabel lbProdaction = new JLabel("Производство:");
        lbProdaction.setBounds(10, 250, 100, 20);

        //add(table);

        btnEvaluate = new JButton("Рассчитать");
        btnEvaluate.setBounds(10, 630, 100, 25);
        btnEvaluate.addActionListener(new ButtonActionListener());

        MaskFormatter mf = null;
        try {
            mf = new MaskFormatter("####-##-##");
            mf.setPlaceholderCharacter('_');
        } catch (ParseException e) {
            e.printStackTrace();
        }

        JLabel lbDate = new JLabel("Дата: ");
        lbDate.setBounds(10, 380, 80, 20);

        ftfDate = new JFormattedTextField(mf);
        ftfDate.setBounds(30, 400, 80, 20);
        ftfDate.addKeyListener(new KeyAdapter() {
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

        cbDebt = new JCheckBox("Вносить в расчет сумму по задолженностям?");
        cbDebt.setBounds(10, 490, 250, 20);
        cbDebt.addItemListener(new CheckBoxActionListener());

        lbDebtSum = new JLabel("Сумма по долгу");
        lbDebtSum.setBounds(120, 520, 150, 20);
        lbDebtSum.setEnabled(false);

        ftfDebtSum = new JFormattedTextField();
        ftfDebtSum.setBounds(30, 520, 90, 20);
        ftfDebtSum.setEnabled(false);

        cbAdditional = new JCheckBox("Добавить сумму неучтенных расходов");
        cbAdditional.setBounds(10, 550, 250, 20);
        cbAdditional.addItemListener(new CheckBoxActionListener());

        ftfAddSum = new JFormattedTextField();
        ftfAddSum.setBounds(30, 580, 90, 20);
        ftfAddSum.setEnabled(false);

        add(scrollPaneStaff);
        add(scrollPaneAccessories);
        add(scrollPaneProdaction);

        add(lbStaff);
        add(lbAccessories);
        add(lbProdaction);
        add(lbDate);
        add(lbDebtSum);
        add(lbDebtSum);

        add(btnEvaluate);
        add(ftfDate);
        add(ftfDebtSum);
        add(ftfAddSum);
        add(comboBox);
        //add(cbAdditional);
        add(cbDebt);
        add(cbDefect);
        add(cbAdditional);
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
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

//    public class ButtonActionListener implements ActionListener {
//        public void actionPerformed(ActionEvent e) {
//
//            if (e.getSource() == btnTabDelete) {
//                int[] selectedRows = table.getSelectedRows();
//                ArrayList<Object> listID = new ArrayList<>();
//                for (int id : selectedRows) {
//                    listID.add(table.getValueAt(id, 0));
//                }
//                message = new Message();
//                if (!listID.isEmpty()) {
//                    message.setMessageArray(listID);
//                    message.setCommand(Message.cmd.UserDelete);
//                    try {
//                        clientSendStream.writeObject(message);
//                    } catch (IOException e1) {
//                        e1.printStackTrace();
//                    }
//                }
////
////                message = new Message();
////                message.setCommand(Message.cmd.UserRequest);
////                try {
////                    clientSendStream.writeObject(message);
////                } catch (IOException e1) {
////                    e1.printStackTrace();
////                }
//            }
//            if (e.getSource() == btnTabAdd) {
//                ArrayList<Object> addData = new ArrayList<>();
//                addData.add(ftfFName.getText());
//                addData.add(ftfSName.getText());
//
//                if (comboBox.getSelectedIndex() == 0)
//                    addData.add(String.valueOf(User.Role.ADMIN));
//                else
//                    addData.add(String.valueOf(User.Role.USER));
//
//                message = new Message();
//
//                message.setMessageArray(addData);
//                message.setCommand(Message.cmd.UserAdd);
//                try {
//                    clientSendStream.writeObject(message);
//                } catch (IOException e1) {
//                    e1.printStackTrace();
//                }
//            }
//
//            message = new Message();
//            message.setCommand(Message.cmd.UserRequest);
//            try {
//                clientSendStream.writeObject(message);
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//        }
//    }
//
//    public class TabActionListener implements ChangeListener {
//        @Override
//        public void stateChanged(ChangeEvent e) {
//            JPanel sourcePanel = (JPanel) ((JTabbedPane) e.getSource()).getSelectedComponent();
//            if (sourcePanel == tabDeletePanel) {
//                table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//            } else if (sourcePanel == tabInsertPanel) {
//                table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
////            } else if (sourcePanel == tabModifyPanel) {
////                table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//            }
//        }
//    }

    public void setSourseStaff(Object[][] data) {
        tableStaff.setModel(new DefaultTableModel(data, columnNameStaff));
    }

    public void setSourseAccessories(Object[][] data) {
        tableAccessories.setModel(new DefaultTableModel(data, columnNameAcceccories));
    }

    public void setSourseProdaction(Object[][] data) {
        tableProdaction.setModel(new DefaultTableModel(data, columnNameProdaction));
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
            if (e.getSource() == tableStaff) {
                int selectedRow = tableStaff.getSelectedRow();
                if (selectedRow >= 0) {
                    TableModel model = tableStaff.getModel();
                    //id = Integer.parseInt((String) model.getValueAt(selectedRow, 0));
                }
            }
        }
    }

    public class ButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == btnEvaluate) {
                if (!isDateValid(ftfDate.getText())) {
                    JOptionPane.showMessageDialog(null, "Дата введена не корректно");
                } else {
                    ArrayList<Object> listID = new ArrayList<>();

                    int selectStaff = tableStaff.getSelectedRow();
                    int selectAccessories = tableAccessories.getSelectedRow();
                    int selectProdaction = tableProdaction.getSelectedRow();
                    if (selectStaff < 0 || selectAccessories < 0 || selectProdaction < 0) {
                        JOptionPane.showMessageDialog(null, "Выберите все данные для расчета!");
                    } else {
                        listID.add(tableStaff.getValueAt(selectStaff, 0));
                        listID.add(tableAccessories.getValueAt(selectAccessories, 0));
                        listID.add(tableProdaction.getValueAt(selectProdaction, 0));
                        listID.add(String.valueOf(idUser));
                        listID.add(ftfDate.getText());

                        if (cbDefect.isSelected()) {
                            int index = comboBox.getSelectedIndex();
                            Float persent;
                            switch (index) {
                                case 0:
                                    persent = (float) 0.05;
                                    break;
                                case 1:
                                    persent = (float) 0.10;
                                    break;
                                case 2:
                                    persent = (float) 0.15;
                                    break;
                                default:
                                    persent = (float) 0;
                                    break;
                            }

                            listID.add(String.valueOf(persent));
                        } else {
                            listID.add("0");
                        }
                        if (cbDebt.isSelected()) {
                            listID.add(ftfDebtSum.getText());
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

    }

    public class CheckBoxActionListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (cbDefect.isSelected()) {
                comboBox.setEnabled(true);
                //comboBox.setEditable(true);
            } else {
                comboBox.setEnabled(false);
                //comboBox.setEditable(false);
            }
            if (cbDebt.isSelected()) {
                lbDebtSum.setEnabled(true);
                ftfDebtSum.setEnabled(true);
            } else {
                lbDebtSum.setEnabled(false);
                ftfDebtSum.setEnabled(false);
            }
            if (cbAdditional.isSelected()) {
                ftfAddSum.setEnabled(true);
            } else {
                ftfAddSum.setEnabled(false);
            }
        }
    }
}
