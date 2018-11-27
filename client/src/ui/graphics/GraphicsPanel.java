package ui.graphics;

import Message.Message;
import org.jfree.chart.ChartPanel;
import ui.SocketGuiInterface;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectOutputStream;

public class GraphicsPanel extends JPanel implements SocketGuiInterface {

    private ObjectOutputStream clientSendStream;
    private Message message;

    private String[] columnNameReport = {
            "id",
            "Дата",
            "Итоговая сумма"
    };

    private chartPieUI ui;
    private ChartPanel chart;

    private JButton btnRequest;
    private JButton btnFilter;
    private JTable tableReport;

    private JPanel tabSearchPanel;
    private JPanel tabFilterPanel;

    private JTabbedPane tabbedPane;

    public GraphicsPanel(ObjectOutputStream css, Message mes) {
        setClientSendStream(css);
        setMessage(mes);
        setLayout(null);

        String[] str = {
                "Налоги",
                "Сырье",
                "Зарплата",
                "Долги",
        };

        Float[] data = {
                30f, 120f, 45f, 78f,

        };
//        String[] str = {};
//
//        Float[] data = {};
        ui = new chartPieUI("Самая затратная статья расходов");
        ui.createChart(str, data);
        ChartPanel chart = ui.getPanel();
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

        btnRequest = new JButton("Отобразить");
        btnRequest.setBounds(10, 630, 130, 25);
        btnRequest.addActionListener(new ButtonActionListener());

        tabbedPane = new JTabbedPane();

        tabSearchPanel = new JPanel();
        tabFilterPanel = new JPanel();

        tabFilterPanel.setLayout(null);
        btnFilter = new JButton("Отфильтровать");
        btnFilter.setBounds(250, 90, 130, 25);
        btnFilter.addActionListener(new ButtonActionListener());

        tabFilterPanel.add(btnFilter);

        tabbedPane.addTab("Фильтр", tabFilterPanel);



        tabbedPane.addTab("Поиск", tabSearchPanel);


        tabbedPane.setBounds(500, 430, 400, 150);
        //tabbedPane.addChangeListener(new EvaluateUI.TabActionListener());

        add(tabbedPane);

        add(chart);
        add(scrollPaneResult);
        add(btnRequest);
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

    public class TableSelectListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getSource() == tableReport) {
                int selectedRow = tableReport.getSelectedRow();
                if (selectedRow >= 0) {
                    TableModel model = tableReport.getModel();
                    //id = Integer.parseInt((String) model.getValueAt(selectedRow, 0));
                }
            }
        }
    }

    public class ButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == btnRequest) {

            }
            if (e.getSource() == btnFilter){

            }
        }
    }
}
