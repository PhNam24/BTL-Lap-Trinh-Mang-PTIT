package TestClient;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable {
    private PrintWriter  outputWriter;
    private Socket socketOfClient;

    @Override
    public void run() {

        try {
            socketOfClient = new Socket("127.0.0.1", 7777);
            System.out.println("Kết nối thành công!");
            outputWriter = new PrintWriter(new OutputStreamWriter(socketOfClient.getOutputStream()));
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(socketOfClient.getInputStream()));
            String message;
            Scanner scanner = new Scanner(System.in);
            while (true) {
                message = inputReader.readLine();
                if (message == null) {
                    write("Invalid");
                }
                System.out.println(message);
                String command = scanner.nextLine();
                String[] split = command.split(",");
                switch (split   [0]) {
                    case "client-verify" -> write("client-verify" + ',' + split[1] + ',' + split[2]);
                    case "register" -> write("register" + ',' + split[1] + ',' + split[2] + ',' + split[3] + ',' + split[4]);
                    case "offline" -> write("offline");
                    case "go-to-room" -> write("go-to-room,100");
                    case "create-room" -> write("create-room,");
                    case "view-room-list" -> write("view-room-list");
                    case "quick-room" -> write("quick-room");
                    case "cancel-room" -> write("cancel-room");
                    case "join-room" -> write("join-room, 100");
                    case "leave-room" -> write("leave-room");
                    case "guess-price" -> write("guess-price");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void write(String message) throws IOException {
        outputWriter.write(message);
        outputWriter.println();
        outputWriter.flush();
    }

    public Socket getSocketOfClient() {
        return socketOfClient;
    }

    public static void main(String[] args) {
        Client client = new Client();
        Thread thread = new Thread(client);
        thread.start();
    }
}
