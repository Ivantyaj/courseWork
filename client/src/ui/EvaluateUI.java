package ui;

import BDTable.User;
import Message.Message;
import ui.evaluatePanel.MainReportPanel;
import ui.graphics.GraphicsPanel;


import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class EvaluateUI extends JFrame implements SocketGuiInterface {


    private ObjectOutputStream clientSendStream;
    private Message message;

    private JTabbedPane tabbedPane;

    private MainReportPanel tabMainReportPanel;
    private GraphicsPanel tabGraphicsPanel;

    private User user;

    EvaluateUI(ObjectOutputStream objectOutputStream, Message mes) {
        super("Затраты");

        setClientSendStream(objectOutputStream);
        setMessage(mes);

        setSize(950, 750);
        setResizable(false);
        setLayout(null);
        tabbedPane = new JTabbedPane();

        tabMainReportPanel = new MainReportPanel(clientSendStream, message);
        tabGraphicsPanel = new GraphicsPanel(clientSendStream, message);

        tabbedPane.addTab("Расчет затрат", tabMainReportPanel);
        tabbedPane.addTab("Графики", tabGraphicsPanel);

        tabbedPane.setBounds(5, 5, 920, 700);
        tabbedPane.addChangeListener(new EvaluateUI.TabActionListener());

        add(tabbedPane);

    }

    public void setClientSendStream(ObjectOutputStream clientSendStream) {
        this.clientSendStream = clientSendStream;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public void setStaffData(Object[][] data) {
        tabMainReportPanel.setSourseStaff(data);
    }

    public void setRawData(Object[][] data) {
        tabMainReportPanel.setSourseRaw(data);
    }

    public void setProdactionData(Object[][] data) {
        tabMainReportPanel.setSourseProdaction(data);
    }

    public void setReportData(Object[][] data) { tabGraphicsPanel.setSourseReport(data);}
    public void setGraphicsData(Float[] data) { tabGraphicsPanel.setChartData(data);}

    public User getUser() {
        return user;
    }

    void setUser(User user) {
        this.user = user;
        tabMainReportPanel.setUser(user);
        tabGraphicsPanel.setUser(user);
    }

    void requestAll(Message mes, ObjectOutputStream sendStream) throws IOException {
        mes = new Message();
        mes.setCommand(Message.cmd.StaffRequest);

        sendStream.writeObject(mes);

        mes = new Message();
        mes.setCommand(Message.cmd.RawRequest);

        sendStream.writeObject(mes);

        mes = new Message();
        mes.setCommand(Message.cmd.ProdactionRequest);

        sendStream.writeObject(mes);
    }

    public class TabActionListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            JPanel sourcePanel = (JPanel) ((JTabbedPane) e.getSource()).getSelectedComponent();
            if (sourcePanel == tabMainReportPanel) {
                try {
                    requestAll(message, clientSendStream);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (sourcePanel == tabGraphicsPanel) {
                message = new Message();
                message.setCommand(Message.cmd.ReportRequest);
                try {
                    clientSendStream.writeObject(message);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
