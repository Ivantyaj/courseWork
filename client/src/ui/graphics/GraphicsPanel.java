package ui.graphics;

import Message.Message;
import org.jfree.chart.ChartPanel;
import ui.DatePanel;
import ui.SocketGuiInterface;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class GraphicsPanel extends JPanel implements SocketGuiInterface {

    private ObjectOutputStream clientSendStream;
    private Message message;

    private String[] columnNameReport = {
            "id",
            "Дата",
            "Итоговая сумма"
    };

    private String[] chartsColumnName = {
            "Налоги",
            "Сырье",
            "Зарплата",
    };

    private chartPieUI ui;
    private ChartPanel chart;

    private JButton btnShow;
    private JButton btnFilter;
    private JButton btnFind;
    private JButton btnToFile;
    private JTable tableReport;

    private JPanel tabSearchPanel;
    private JPanel tabFilterPanel;

    private JTabbedPane tabbedPane;
    private int id;

    private DatePanel datePanelFrom;
    private DatePanel datePanelTo;
    private DatePanel datePanelFind;

    private JFormattedTextField ftfSumFrom;
    private JFormattedTextField ftfSumTo;
    private JFormattedTextField ftfSumFind;
    private JFormattedTextField ftfIdFind;

    private JFileChooser fileChooser = null;

    public GraphicsPanel(ObjectOutputStream css, Message mes) {
        setClientSendStream(css);
        setMessage(mes);
        setLayout(null);


        Float[] data = {
                30f, 120f, 45f

        };
//        String[] chartsColumnName = {};
//
//        Float[] data = {};
        ui = new chartPieUI("Самая затратная статья расходов");
        ui.createChart(chartsColumnName, data);
        chart = ui.getPanel();
        chart.setBounds(160, 10, 600, 400);


        tableReport = new JTable(new Object[][]{}, columnNameReport);
        JScrollPane scrollPaneResult = new JScrollPane(tableReport);


        //table.setSize(400,50);
        tableReport.setCellSelectionEnabled(false);
        tableReport.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableReport.setRowSelectionAllowed(true);
        tableReport.getSelectionModel().addListSelectionListener(new GraphicsPanel.TableSelectListener());
        tableReport.setDefaultEditor(Object.class, null); //
        scrollPaneResult.setBounds(10, 430, 400, 150);

        btnShow = new JButton("Отобразить");
        btnShow.setBounds(10, 630, 130, 25);
        btnShow.addActionListener(new ButtonActionListener());

        btnToFile = new JButton("Сохранить");
        btnToFile.setBounds(140, 630, 130, 25);
        btnToFile.addActionListener(new ButtonActionListener());

        // Создание экземпляра JFileChooser
        fileChooser = new JFileChooser();

        tabbedPane = new JTabbedPane();

        tabSearchPanel = new JPanel();
        tabFilterPanel = new JPanel();

        tabFilterPanel.setLayout(null);
        btnFilter = new JButton("Отфильтровать");
        btnFilter.setBounds(250, 90, 130, 25);
        btnFilter.addActionListener(new ButtonActionListener());

        JLabel lbDateFrom = new JLabel("Начальная дата:");
        lbDateFrom.setBounds(5, 5, 100, 20);
        JLabel lbDateTo = new JLabel("Конечная дата:");
        lbDateTo.setBounds(110, 5, 100, 20);

        datePanelFrom = new DatePanel();
        datePanelFrom.setBounds(5, 30, 90, 20);

        datePanelTo = new DatePanel();
        datePanelTo.setBounds(110, 30, 90, 20);

        JLabel lbSumFrom = new JLabel("Сумма от:");
        lbSumFrom.setBounds(5, 55, 100, 20);
        JLabel lbSumTo = new JLabel("Сумма до");
        lbSumTo.setBounds(110, 55, 100, 20);

        ftfSumFrom = new JFormattedTextField();
        ftfSumFrom.setBounds(5, 80, 100, 20);

        ftfSumTo = new JFormattedTextField();
        ftfSumTo.setBounds(110, 80, 100, 20);

        tabFilterPanel.add(lbDateFrom);
        tabFilterPanel.add(lbSumFrom);
        tabFilterPanel.add(lbSumTo);
        tabFilterPanel.add(lbDateTo);
        tabFilterPanel.add(ftfSumFrom);
        tabFilterPanel.add(ftfSumTo);
        tabFilterPanel.add(datePanelFrom);
        tabFilterPanel.add(datePanelTo);
        tabFilterPanel.add(btnFilter);

        tabbedPane.addTab("Фильтр", tabFilterPanel);

        tabSearchPanel.setLayout(null);
        JLabel lbIdFind = new JLabel("id:");
        lbIdFind.setBounds(5, 5, 100, 20);
        JLabel lbDateFind = new JLabel("Дата:");
        lbDateFind.setBounds(5, 35, 100, 20);
        JLabel lbSumFind = new JLabel("Сумма:");
        lbSumFind.setBounds(5, 65, 100, 20);

        ftfIdFind = new JFormattedTextField();
        ftfIdFind.setBounds(100, 5, 100, 20);
        datePanelFind = new DatePanel();
        datePanelFind.setBounds(100, 30, 100, 20);
        ftfSumFind = new JFormattedTextField();
        ftfSumFind.setBounds(100, 65, 100, 20);

        btnFind = new JButton("Найти");
        btnFind.setBounds(250, 90, 130, 25);
        btnFind.addActionListener(new ButtonActionListener());

        tabSearchPanel.add(lbDateFind);
        tabSearchPanel.add(lbSumFind);
        tabSearchPanel.add(lbIdFind);
        tabSearchPanel.add(ftfIdFind);
        tabSearchPanel.add(ftfSumFind);
        tabSearchPanel.add(datePanelFind);
        tabSearchPanel.add(btnFind);
        tabbedPane.addTab("Поиск", tabSearchPanel);


        tabbedPane.setBounds(500, 430, 400, 150);
        //tabbedPane.addChangeListener(new EvaluateUI.TabActionListener());

        add(tabbedPane);

        add(chart);
        add(scrollPaneResult);
        add(btnShow);
        add(btnToFile);
    }

    public void setClientSendStream(ObjectOutputStream clientSendStream) {
        this.clientSendStream = clientSendStream;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
    }

    public void setSourseReport(Object[][] data) {
        tableReport.setModel(new DefaultTableModel(data, columnNameReport));
    }

    public void setChartData(Float[] data) {
        ui = new chartPieUI("Самая затратная статья расходов");
        ui.createChart(chartsColumnName, data);
        //ui.getChart();
        chart.setChart(ui.getChart());
        //chart = ui.getPanel();


        //chart.setChart(ui.getChart());
    }

    public class TableSelectListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {

            int selectedRow = tableReport.getSelectedRow();
            if (selectedRow >= 0) {
                TableModel model = tableReport.getModel();

            }
        }
    }

    public class ButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == btnShow) {
                id = tableReport.getSelectedRow();

                message = new Message();

//                ArrayList<Object> arrayList = new ArrayList<>();
//                arrayList.add(tableReport.getValueAt(id, 0));

                message.setArrayOneObject(tableReport.getValueAt(id, 0));
                //message.setMessageArray(arrayList);

                message.setCommand(Message.cmd.RequestReportOne);
                try {
                    clientSendStream.writeObject(message);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (e.getSource() == btnFilter) {

                if (datePanelFrom.getTextDate().equals("") || datePanelTo.getTextDate().equals("")
                        || ftfSumFrom.getText().equals("") || ftfSumTo.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Необходимо ввести все данные!");
                    return;
                }

                message = new Message();
                ArrayList<Object> filterList = new ArrayList<>();

                filterList.add(datePanelFrom.getTextDate());
                filterList.add(datePanelTo.getTextDate());
                filterList.add(ftfSumFrom.getText());
                filterList.add(ftfSumTo.getText());

                message.setMessageArray(filterList);
                message.setCommand(Message.cmd.FilterReports);
                try {
                    clientSendStream.writeObject(message);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (e.getSource() == btnFind) {
                message = new Message();
                ArrayList<Object> filterList = new ArrayList<>();

                filterList.add(ftfIdFind.getText());
                filterList.add(datePanelFind.getTextDate());
                filterList.add(ftfSumFind.getText());

                message.setMessageArray(filterList);
                message.setCommand(Message.cmd.SearchReports);
                try {
                    clientSendStream.writeObject(message);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (e.getSource() == btnToFile) {


                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Текстовый документ (*.txt)", "txt");
                fileChooser.setFileFilter(filter);
                fileChooser.setDialogTitle("Сохранение файла");
                // Определение режима - только файл
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = fileChooser.showSaveDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File fi = fileChooser.getSelectedFile();
                    try {
                        FileWriter fw = new FileWriter(fi.getPath());
                        int ColC = tableReport.getColumnCount(); //Определяем кол-во столбцов
                        int ItemC = tableReport.getRowCount();  //и элементов (строк)

                        StringBuilder sb;

                        for (int i = 0; i < ItemC; i++) { //проходим все строки
                            sb = new StringBuilder();
                            for (int j = 0; j < ColC; j++) { //собираем одну строку из множества столбцов
                                sb.append(tableReport.getValueAt(i, j));
                                if (j < ColC - 1) sb.append(',');
                                if (j == ColC - 1) sb.append("\r\n");
                            }
                            fw.write(sb.toString());
                            sb = null;
                        }

                        fw.flush();
                        fw.close();
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, e1.getMessage());
                    }
                    JOptionPane.showMessageDialog(null,
                            "Файл '" + fileChooser.getSelectedFile() +
                                    "  сохранен");
                }
            }
        }
    }
}
