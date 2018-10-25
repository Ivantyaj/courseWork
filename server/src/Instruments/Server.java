package Instruments;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ServerSocket;
import javax.swing.*;

public class Server extends JFrame {
    public static final int PORT = 1502;

    JButton btnStart;
    JButton btnPort;
    JTextField textPort;
    JTextField textIP;


    public Server(String str) {
        super(str);
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        btnStart = new JButton("Запуск");
        btnPort = new JButton("Установить порт");
        textPort = new JTextField(9);
        textIP = new JTextField(9);

        textIP.setEditable(false);

//        l1 = new JLabel("Название медикамента");
//        l2 = new JLabel("Описание лекарственных свойств");
//        l3 = new JLabel("Дата поступления");
//        l4 = new JLabel("Отпуск без рецепта");
//        box_1 = new JComboBox(box1);
//        box_2 = new JComboBox(box2);
//        box_3 = new JComboBox<Object>(box3);
//        flag1 = new JRadioButton("да");
//        flag2 = new JRadioButton("нет");
//        bg = new ButtonGroup();
//        bg.add(flag1);
//        bg.add(flag2);

        setLayout(null);

        textPort.setBounds(10 ,10,30,20);
        btnStart.setBounds(10, 40, 30, 20);
        btnPort.setBounds(10, 65, 30, 20);

        add(btnStart);
        add(btnPort);
        add(textPort);
//        add(area);
//        add(l1);
//        add(l2);
//        add(l3);
//        add(l4);
//        add(box_1);
//        add(box_2);
//        add(box_3);
//        add(flag1);
//        add(flag2);

        btnPort.addActionListener(new ButtonActionListener());
        btnStart.addActionListener(new FlagActionListener());
//        flag2.addActionListener(new FlagActionListener());
//        del.addActionListener(new DelActionListener());
//        box_1.addActionListener(new BoxActionListener());
//        box_2.addActionListener(new BoxActionListener());
//        box_3.addActionListener(new BoxActionListener());
    }

    public class ButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == btnPort){
                text.setText(null);
                area.setText(null);

            }
        }
    }


    public void start() {
        int port = 0;
        try {
            if (args.length != 0 && Integer.parseInt(args[0]) != 0 && args[0].length() == 4) {
                port = Integer.parseInt(args[0]);
                System.out.println("first");
            } else if (args.length == 0) {
                port = PORT;
                System.out.println("second");
            } else {
                System.out.println("Invalid port!");
                System.exit(0);
            }
            ServerSocket serverSocket = new ServerSocket(port);

            while (true) {
                new ServerThread(serverSocket.accept()).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

