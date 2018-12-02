package ui;

import Message.Message;
import ui.tablePanel.RawPackagePanel;
import ui.tablePanel.ProdactionPanel;
import ui.tablePanel.StaffPanel;
import ui.tablePanel.UserPanel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.IOException;
import java.io.ObjectOutputStream;


public class DB extends JFrame implements SocketGuiInterface {

    private ObjectOutputStream clientSendStream;
    private Message message;

    private JTabbedPane tabbedPane;

    private UserPanel tabUserPanel;
    private StaffPanel tabStaffPanel;
    private RawPackagePanel tabRawPackagePanel;
    private ProdactionPanel tabProdactionPanel;

    DB(ObjectOutputStream objectOutputStream, Message mes){
        super("База данных");

        setClientSendStream(objectOutputStream);
        setMessage(mes);

        setSize(950,750);
        setResizable(false);
        setLayout(null);
        tabbedPane = new JTabbedPane();

        tabUserPanel = new UserPanel(clientSendStream, message);
        tabStaffPanel = new StaffPanel(clientSendStream, message);
        tabRawPackagePanel = new RawPackagePanel(clientSendStream, message);
        tabProdactionPanel = new ProdactionPanel(clientSendStream, message);

        tabbedPane.addTab("Пользователи", tabUserPanel);
        tabbedPane.addTab("Штат", tabStaffPanel);
        tabbedPane.addTab("Сырье", tabRawPackagePanel);
        tabbedPane.addTab("Производство", tabProdactionPanel);


        tabbedPane.setBounds(5,5,920,700);
        tabbedPane.addChangeListener(new DB.TabActionListener());

        add(tabbedPane);
    }

    public void setClientSendStream(ObjectOutputStream clientSendStream) {
        this.clientSendStream = clientSendStream;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public void setUserData(Object[][] data){
        tabUserPanel.setSourse(data);
    }
    public void setStaffData(Object[][] data){ tabStaffPanel.setSourse(data); }
    public void setRawData(Object[][] data){ tabRawPackagePanel.setSourse(data); }
    public void setProdactionData(Object[][] data){ tabProdactionPanel.setSourse(data); }

    public class TabActionListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            JPanel sourcePanel = (JPanel) ((JTabbedPane)e.getSource()).getSelectedComponent();
            if (sourcePanel == tabStaffPanel){
                message = new Message();
                message.setCommand(Message.cmd.StaffRequest);
                try {
                    clientSendStream.writeObject(message);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (sourcePanel == tabUserPanel) {
                message = new Message();
                message.setCommand(Message.cmd.UserRequest);
                try {
                    clientSendStream.writeObject(message);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (sourcePanel == tabRawPackagePanel) {
                message = new Message();
                message.setCommand(Message.cmd.RawRequest);
                try {
                    clientSendStream.writeObject(message);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (sourcePanel == tabProdactionPanel) {
                message = new Message();
                message.setCommand(Message.cmd.ProdactionRequest);
                try {
                    clientSendStream.writeObject(message);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
