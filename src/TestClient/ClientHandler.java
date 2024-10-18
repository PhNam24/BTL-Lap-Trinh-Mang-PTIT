package TestClient;

import Server.Model.Player;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread {
    private PrintWriter output;
    private Socket clientSocket;

    public ArrayList<Player> getLeaderBoard(String[] msg) {
        ArrayList<Player> leaderBoard = new ArrayList<>();
        for(int i = 1; i < msg.length; i += 10) {
            leaderBoard.add(new Player(
                    Integer.parseInt(msg[i]),
                    msg[i + 1],
                    msg[i + 2],
                    msg[i + 3],
                    msg[i + 4],
                    Integer.parseInt(msg[i + 5]),
                    Integer.parseInt(msg[i + 6]),
                    Integer.parseInt(msg[i + 7]),
                    Double.parseDouble(msg[i + 8])
            ));
        }

        return leaderBoard;
    }

    public Player getPlayerFromString(int start, String[] msg) {
        return new Player(
                Integer.parseInt(msg[start]),
                msg[start + 1],
                msg[start + 2],
                msg[start + 3],
                msg[start + 4],
                Integer.parseInt(msg[start + 5]),
                Integer.parseInt(msg[start + 6]),
                Integer.parseInt(msg[start + 7]),
                Double.parseDouble(msg[start + 8]));
    }

    @Override
    public void run() {
        try {
            clientSocket = new Socket("127.0.0.1", 7777);
            System.out.println("Kết nối thành công!");
            output = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String message;

            // listening
            while (true) {
                message = inputReader.readLine();
                if (message == null) {
                    break;
                }
                String[] messageSplit = message.split(",");
                if (messageSplit[0].equals("server-send-id")) {
                    int serverId = Integer.parseInt(messageSplit[1]);
                }
                //Đăng nhập thành công
                if (messageSplit[0].equals("login-success")) {
                    System.out.println("Đăng nhập thành công");
                    Client.closeAllViews();
                    Client.player = getPlayerFromString(1, messageSplit);
                    System.out.println(Client.player);
                    Client.openView(Client.View.HOMEPAGE);
                }
                //Thông tin tài khoản sai
                if (messageSplit[0].equals("wrong-player")) {
                    System.out.println("Thông tin sai");
                    Client.closeView(Client.View.GAME_NOTICE);
                    Client.openView(Client.View.LOGIN, messageSplit[1], messageSplit[2]);
                    Client.loginForm.showMessage("Tài khoản hoặc mật khẩu không chính xác");
                }
                //Tài khoản đã đăng nhập ở nơi khác
                if (messageSplit[0].equals("dupplicate-login")) {
                    System.out.println("Đã đăng nhập");
                    Client.closeView(Client.View.GAME_NOTICE);
                    Client.openView(Client.View.LOGIN, messageSplit[1], messageSplit[2]);
                    Client.loginForm.showMessage("Tài khoản đã đăng nhập ở nơi khác");
                }

                //Xử lý register trùng tên
                if (messageSplit[0].equals("duplicate-username")) {
                    Client.closeAllViews();
                    Client.openView(Client.View.REGISTER);
                    JOptionPane.showMessageDialog(Client.registerForm, "Tên tài khoản đã được người khác sử dụng");
                }

                //Xử lý nhận thông tin, chat từ toàn server
                if (messageSplit[0].equals("chat-server")) {
                    if (Client.mainLobbyForm != null) {
                        Client.mainLobbyForm.addMessage(messageSplit[1]);
                    }
                }

                //Xử lý kết quả tìm phòng từ server
                if (messageSplit[0].equals("room-fully")) {
                    Client.closeAllViews();
                    Client.openView(Client.View.HOMEPAGE);
                    JOptionPane.showMessageDialog(Client.mainLobbyForm, "Phòng chơi đã đủ 2 người chơi");
                }
                // Xử lý không tìm thấy phòng trong chức năng vào phòng
                if (messageSplit[0].equals("room-not-found")) {
                    Client.closeAllViews();
                    Client.openView(Client.View.HOMEPAGE);
                    JOptionPane.showMessageDialog(Client.mainLobbyForm, "Không tìm thấy phòng");
                }
                // Xử lý phòng có mật khẩu sai
                if (messageSplit[0].equals("room-wrong-mainLobbyForm")) {
                    Client.closeAllViews();
                    Client.openView(Client.View.HOMEPAGE);
                    JOptionPane.showMessageDialog(Client.mainLobbyForm, "Mật khẩu phòng sai");
                }

                //Xử lý lấy danh sách phòng
//                if (messageSplit[0].equals("room-list")) {
//                    ArrayList<String> rooms = new ArrayList<>();
//                    ArrayList<String> passwords = new ArrayList<>();
//                    for (int i = 1; i < messageSplit.length; i = i + 2) {
//                        rooms.add("Phòng " + messageSplit[i]);
//                        passwords.add(messageSplit[i + 1]);
//                    }
//                    Client.roomListFrm.updateRoomList(rooms, passwords);
//                }
//                if (messageSplit[0].equals("go-to-room")) {
//                    System.out.println("Vào phòng");
//                    int roomID = Integer.parseInt(messageSplit[1]);
//                    String competitorIP = messageSplit[2];
//                    int isStart = Integer.parseInt(messageSplit[3]);
//
//                    Player competitor = getPlayerFromString(4, messageSplit);
//                    if (Client.findRoomFrm != null) {
//                        Client.findRoomFrm.showFoundRoom();
//                        try {
//                            Thread.sleep(3000);
//                        } catch (InterruptedException ex) {
//                            JOptionPane.showMessageDialog(Client.findRoomFrm, "Lỗi khi sleep thread");
//                        }
//                    } else if (Client.waitingRoomFrm != null) {
//                        Client.waitingRoomFrm.showFoundCompetitor();
//                        try {
//                            Thread.sleep(3000);
//                        } catch (InterruptedException ex) {
//                            JOptionPane.showMessageDialog(Client.waitingRoomFrm, "Lỗi khi sleep thread");
//                        }
//                    }
//                    Client.closeAllViews();
//                    System.out.println("Đã vào phòng: " + roomID);
//                    //Xử lý vào phòng
//                    Client.openView(Client.View.GAME_CLIENT
//                            , competitor
//                            , roomID
//                            , isStart
//                            , competitorIP);
//                    Client.inGameForm.newgame();
//                }
//                //Tạo phòng và server trả về tên phòng
//                if (messageSplit[0].equals("your-created-room")) {
//                    Client.closeAllViews();
//                    Client.openView(Client.View.WAITING_ROOM);
//                    Client.waitingRoomFrm.setRoomName(messageSplit[1]);
//                    if (messageSplit.length == 3)
//                        Client.waitingRoomFrm.setRoomPassword("Mật khẩu phòng: " + messageSplit[2]);
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void write(String message) throws IOException {
        output.write(message);
        output.println();
        output.flush();
    }

    public Socket getClientSocket() {
        return clientSocket;
    }
}
