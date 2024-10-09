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
                    break;
                }
                System.out.println(message);
                String command = scanner.nextLine();
                if (command.equals("login")) {
                    write("leaderboard");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void write(String message) throws IOException {
        outputWriter.write(message + "\r\n");
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
