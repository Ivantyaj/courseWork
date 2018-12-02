package Instruments;

import BDTable.*;
import DataBase.DBWorker;
import DataBase.SQLRequest;
import Message.Message;
import ui.ServerGUI;

import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ServerThread extends Thread {

    private Socket socket;
    private DBWorker dbWorker;
    private SQLRequest sqlRequest;

    ServerGUI serverGui;
//    private SqlDaoFactory   daoFactory;


    ObjectInputStream serverReadStream;
    ObjectOutputStream serverSendStream;

    Message message = new Message();

    public ServerThread(Socket socket) {
        this.socket = socket;
        //  daoFactory = new SqlDaoFactory();
        // connection = daoFactory.getConnection();
    }

    @Override
    public void run() {

        serverGui = ServerGUI.INSTANCE;
        serverGui.setVisible(true);
        serverGui.setResizable(false);
        serverGui.setLocationRelativeTo(null);
        serverGui.addClient();
        System.out.println("socet--------------------" + socket.getChannel() + socket.getInetAddress() + socket.getPort());

        System.out.println(socket.getInetAddress().getHostName() +
                " " + socket.getInetAddress() + "connected");


        dbWorker = new DBWorker();
        sqlRequest = new SQLRequest();

        try {
            serverReadStream = new ObjectInputStream(socket.getInputStream());
            serverSendStream = new ObjectOutputStream(socket.getOutputStream());

            while (!message.getCommand().equals(Message.cmd.Stop)) {
                message = (Message) serverReadStream.readObject();
                System.out.println("incoming: " + message);
                serverGui.setMessage(message.toString());

                try {
                    switch (message.getCommand()) {
                        case LogIn: {

                            String login = (String) message.getMessageArray().get(0);
                            String password = (String) message.getMessageArray().get(1);
                            User user = new User();
                            message.setCommand(Message.cmd.LogIn);
                            if (!(login.isEmpty() || password.isEmpty())) {

                                String query = "select * from users where " +
                                        "login = " + login + " and" + " password = " + password;
                                Statement statement = dbWorker.getConnection().createStatement();
                                ResultSet resultSet = statement.executeQuery(query);

                                if (resultSet.next()) {
//                                user.setId(resultSet.getInt("id"));
//                                user.setRole(resultSet.getString("role"));
//                                user.setLogin(resultSet.getString("login"));
//                                user.setPassword(resultSet.getString("password"));
                                    user = new User(resultSet);
                                }
                            }
                            message.setArrayOneObject(user);
                            serverSendStream.writeObject(message);
                        }
                        break;
                        case UserRequest: {
                            ArrayList<Object> userArrayList = new ArrayList<>();
                            //String query = "select * from users";
                            //Statement statement = dbWorker.getConnection().createStatement();
//                        ResultSet resultSet = statement.executeQuery(query);
                            ResultSet resultSet = sqlRequest.executeSqlQuery(message);
                            while (resultSet.next()) {
                                userArrayList.add(new User(resultSet));
                            }
                            message.setMessageArray(userArrayList);
                            serverSendStream.writeObject(message);
                        }
                        break;
                        case StaffRequest:
                            ArrayList<Object> staffArrayList = new ArrayList<>();
                            ResultSet resultSet = sqlRequest.executeSqlQuery(message);

                            //if(resultSet != null) {
                            while (resultSet.next()) {
                                staffArrayList.add(new Staff(resultSet));
                            }
                            //}
                            message.setMessageArray(staffArrayList);
                            serverSendStream.writeObject(message);
                            break;
//                    case AccessoriesDelete:
//                    case AccessoriesAdd:
//                    case ProdactionDelete:
//                    case ProdactionAdd:
//                    case UserDelete:
//                    case UserAdd:
//                    case StaffDelete:
//                    case StaffAdd:
//                    case StaffRedact:
//                    case UserRedact:
//                    case ProdactionRedact:
//                    case AccessoriesRedact:
//                        sqlRequest.executeSqlQuery(message);
//                        break;
                        case AccessoriesRequest: {
                            ArrayList<Object> accessoriesArrayList = new ArrayList<>();
                            ResultSet resultSetAccessories = sqlRequest.executeSqlQuery(message);

                            //if(resultSet != null) {
                            while (resultSetAccessories.next()) {
                                accessoriesArrayList.add(new Accessories(resultSetAccessories));
                            }
                            //}
                            message.setMessageArray(accessoriesArrayList);
                            serverSendStream.writeObject(message);
                        }
                        break;

                        case ProdactionRequest: {
                            ArrayList<Object> prodactionArrayList = new ArrayList<>();
                            ResultSet resultSetProdaction = sqlRequest.executeSqlQuery(message);

                            //if(resultSet != null) {
                            while (resultSetProdaction.next()) {
                                prodactionArrayList.add(new Prodaction(resultSetProdaction));
                            }
                            //}
                            message.setMessageArray(prodactionArrayList);
                            serverSendStream.writeObject(message);
                        }
                        break;
                        case Evaluate:
                            Report report = new Report(message.getMessageArray());
                            report.evaluateResult(sqlRequest);
                            sqlRequest.insertIntoReport(report);
                            break;
                        case ReportRequest:
                            ArrayList<Object> reportArrayList = new ArrayList<>();
                            ResultSet resultSetReport = sqlRequest.executeSqlQuery(message);

                            while (resultSetReport.next()) {
                                reportArrayList.add(new Report(resultSetReport));
                            }

                            message.setMessageArray(reportArrayList);
                            serverSendStream.writeObject(message);
                            break;
                        case RequestReportOne:
                            ResultSet resultSetOneReport = sqlRequest.requestOneReport(Integer.parseInt((String) message.getArrayOneObject()));

                            while (resultSetOneReport.next()) {
                                message.setArrayOneObject(new Report(resultSetOneReport));
                            }

                            serverSendStream.writeObject(message);
                            break;
                        case SearchReports:
                            ArrayList<Object> reportFindList = new ArrayList<>();
                            ResultSet resultSetFindReport = sqlRequest.searchInReports(message);

                            while (resultSetFindReport.next()) {
                                reportFindList.add(new Report(resultSetFindReport));
                            }

                            message.setMessageArray(reportFindList);
                            serverSendStream.writeObject(message);
                            break;
                        case FilterReports:
                            ArrayList<Object> reportFilterList = new ArrayList<>();
                            ResultSet resultSetFilterReport = sqlRequest.filterReports(message);

                            while (resultSetFilterReport.next()) {
                                reportFilterList.add(new Report(resultSetFilterReport));
                            }

                            message.setMessageArray(reportFilterList);
                            serverSendStream.writeObject(message);
                            break;
                        default:
                            sqlRequest.executeSqlQuery(message);
                            break;

                    }
                } catch (SQLException | NumberFormatException e) {
                    e.printStackTrace();

                    message = new Message();
                    message.setCommand(Message.cmd.FailSQL);
                    try {
                        serverSendStream.writeObject(message);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            serverGui.removeClient();
        }

    }
}
