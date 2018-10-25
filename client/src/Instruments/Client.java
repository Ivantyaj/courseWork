package Instruments;

public class Client {
    public static final int PORT = 1502;

    public static void main(String[] args) {
        int port = 0;
        if (args.length == 0) {
            port = PORT;
            System.out.println("first");
        } else if (Integer.parseInt(args[0]) != 0 && args[0].length() == 4) {
            port = Integer.parseInt(args[0]);
            System.out.println("second");
        } else {
            System.out.println("Invalid port!");
            System.exit(0);
        }
        MainWindow mainWindow = new MainWindow(port);
        mainWindow.setVisible(true);
    }
}
