package Server;

import Server.Controller.ServerThread;
import Server.Controller.ServerThreadBUS;
import Server.View.Admin;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Server {
    public static volatile ServerThreadBUS serverThreadBus;
    public static Socket clientSocket;
    public static int ROOM_ID;
    public static volatile Admin admin;

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        serverThreadBus = new ServerThreadBUS();
        System.out.println("Server is waiting to accept user...");
        int clientNumber = 0;
        ROOM_ID = 100;

        try {
            serverSocket = new ServerSocket(7777);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                10,
                100,
                10,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(8)
        );
        admin = new Admin();
        admin.run();
        try {
            while (true) {
                clientSocket = serverSocket.accept();
                System.out.println(clientSocket.getInetAddress().getHostAddress());
                ServerThread serverThread = new ServerThread(clientSocket, clientNumber++);
                serverThreadBus.add(serverThread);
                System.out.println("Số thread đang chạy là: " + serverThreadBus.getLength());
                executor.execute(serverThread);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
