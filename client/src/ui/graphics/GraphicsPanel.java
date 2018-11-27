package ui.graphics;

import Message.Message;
import org.jfree.chart.ChartPanel;
import ui.SocketGuiInterface;

import javax.swing.*;
import java.io.ObjectOutputStream;

public class GraphicsPanel extends JPanel implements SocketGuiInterface {

    private ObjectOutputStream clientSendStream;
    private Message message;

    private chartPieUI ui;
    private ChartPanel chart;

    public GraphicsPanel(ObjectOutputStream css, Message mes) {
        setClientSendStream(css);
        setMessage(mes);

            String[] str = {
            "Налоги",
            "Сырье",
            "Зарплата",
            "Долги",
    };

    Float[] data = {
            30f, 10f, 20f, 40f,

    };
//        String[] str = {};
//
//        Float[] data = {};
        ui = new chartPieUI("Самая затратная статья расходов");
        ui.createChart(str, data);
        ChartPanel chart = ui.getPanel();
        chart.setBounds(10, 10, 300, 300);
        add(chart);
    }

    public void setClientSendStream(ObjectOutputStream clientSendStream) {
        this.clientSendStream = clientSendStream;
    }

    public void setMessage(Message message) {
        this.message = message;
    }


}
