package ui;

import Message.Message;
import ui.tablePanel.AccessoriesPanel;
import ui.tablePanel.ProdactionPanel;
import ui.tablePanel.StaffPanel;
import ui.tablePanel.UserPanel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.IOException;
import java.io.ObjectOutputStream;


public class DB extends JFrame implements SocketGuiInterface {
//    String[] columnName = {
//      "id",
//      "Логин",
//      "Пароль",
//      "Роль"
//    };

    ObjectOutputStream clientSendStream;
    Message message;

    //JTable table;
    JTabbedPane tabbedPane;


    //JButton btnTabDelete;

    UserPanel tabUserPanel;
    StaffPanel tabStaffPanel;
    AccessoriesPanel tabAccessoriesPanel;
    ProdactionPanel tabProdactionPanel;
//    JPanel tabInsertPanel;
//    JPanel tabModifyPanel;


    public DB(ObjectOutputStream objectOutputStream, Message mes){
        super("База данных");

        setClientSendStream(objectOutputStream);
        setMessage(mes);

        setSize(950,750);
        setResizable(false);
        setLayout(null);
        tabbedPane = new JTabbedPane();

        tabUserPanel = new UserPanel(clientSendStream, message);
        tabStaffPanel = new StaffPanel(clientSendStream, message);
        tabAccessoriesPanel = new AccessoriesPanel(clientSendStream, message);
        tabProdactionPanel = new ProdactionPanel(clientSendStream, message);

        tabbedPane.addTab("Пользователи", tabUserPanel);
        tabbedPane.addTab("Штат", tabStaffPanel);
        tabbedPane.addTab("Сырье", tabAccessoriesPanel);
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
    public void setAccessoriesData(Object[][] data){ tabAccessoriesPanel.setSourse(data); }
    public void setProdactionData(Object[][] data){ tabProdactionPanel.setSourse(data); }


//    public class ButtonActionListener implements ActionListener {
//        public void actionPerformed(ActionEvent e) {
//
//            if (e.getSource() == btnTabDelete) {
//                int[] selectedRows = table.getSelectedRows();
//                ArrayList<Object> listID = new ArrayList<>();
//                for (int id: selectedRows){
//                    listID.add(table.getValueAt(id,0));
//                }
//                if(!listID.isEmpty()) {
//                    message.setMessageArray(listID);
//                    message.setCommand(Message.cmd.UserDelete);
//                    try {
//                        clientSendStream.writeObject(message);
//                    } catch (IOException e1) {
//                        e1.printStackTrace();
//                    }
//                }
//
//                message = new Message();
//                message.setCommand(Message.cmd.UserRequest);
//                try {
//                    clientSendStream.writeObject(message);
//                } catch (IOException e1) {
//                    e1.printStackTrace();
//                }
////
////                message.setCommand(Message.cmd.UserRequest);
////                try {
////                    clientSendStream.writeObject(new Message());
////                } catch (IOException e1) {
////                    e1.printStackTrace();
////                }
//            }
////            else if (e.getSource() == btnEnter) {
////                ArrayList<Object> stringArrayList = new ArrayList<>();
////                stringArrayList.add(textFieldLogin.getText());
////                stringArrayList.add(textFieldPassword.getText());
////                message.setMessageArray(stringArrayList);
////                message.setCommand(Message.cmd.LogIn);
////                try {
////                    clientSendStream.writeObject(message);
////                } catch (IOException e1) {
////                    e1.printStackTrace();
////                }
////            }
//        }
//    }
//
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
            if (sourcePanel == tabAccessoriesPanel) {
                message = new Message();
                message.setCommand(Message.cmd.AccessoriesRequest);
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
