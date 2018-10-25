package ui;

import javax.swing.*;
import java.net.*;
import java.io.*;

public class MainWindow {
//    public void start() {
//        try {
//            System.out.println("server connecting....");
//            Socket clientSocket = new Socket("127.0.0.1",2525);
//            System.out.println("connection established....");
//            System.out.println("Введите строку для проверки");
//            BufferedReader stdin =  new BufferedReader(new InputStreamReader(System.in));
//            ObjectOutputStream outStr =  new ObjectOutputStream(clientSocket.getOutputStream());
//            ObjectInputStream  inStr =  new ObjectInputStream(clientSocket.getInputStream());//создание //потока ввода
//            System.out.println("'exit' − выход");
//            String clientMessage = stdin.readLine();
//            while(!clientMessage.equals("exit")) {
//                outStr.writeObject(clientMessage);
//                System.out.println("~Ответ~: "+inStr.readObject());
//                System.out.println("---------------------------");
//                System.out.println("Введите строку для проверки");
//                clientMessage = stdin.readLine();
//            }
//            outStr.close();
//            inStr.close();
//            clientSocket.close();
//        }catch(Exception e)	{
//            e.printStackTrace();
//        }
//    }
}
