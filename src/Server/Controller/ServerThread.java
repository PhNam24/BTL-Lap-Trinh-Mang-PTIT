package Server.Controller;

import Server.DAO.PlayerDAO;
import Server.Model.GameMatch;
import Server.Model.Player;
import Server.Model.Product;
import Server.Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThread extends Thread {

    private Player player;
    private final Socket clientSocket;
    private final int clientNumber;
    private BufferedReader in;
    private PrintWriter out;
    private boolean isClosed;
    private Room room;
    private final String clientIP;
    private final PlayerDAO playerDAO;

    public ServerThread(Socket clientSocket, int clientNumber) throws IOException {
        this.clientSocket = clientSocket;
        this.clientNumber = clientNumber;
        this.playerDAO = new PlayerDAO();
        System.out.println("Server thread number " + clientNumber + " Started");
        this.isClosed = false;
        this.room = null;
        if (this.clientSocket.getInetAddress().getHostAddress().equals("127.0.0.1")) {
            clientIP = "127.0.0.1";
        } else {
            clientIP = this.clientSocket.getInetAddress().getHostAddress();
        }
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
    }

    public void write(String message) throws IOException {
        out.write(message);
        out.println();
        out.flush();
    }

    public String getStringFromPlayer(Player player) {
        return player.toString();
    }

    public void goToOwnRoom() throws IOException {
        write("go-to-room," + room.getId() + "," + room.getCompetitor(this.getClientNumber()).getClientIP() + ",1," + getStringFromPlayer(room.getCompetitor(this.getClientNumber()).getPlayer()));
        room.getCompetitor(this.clientNumber).write("go-to-room," + room.getId() + "," + this.clientIP + ",0," + getStringFromPlayer(player));
    }

    public void goToPartnerRoom() throws IOException {
        write("go-to-room," + room.getId() + "," + room.getCompetitor(this.getClientNumber()).getClientIP() + ",0," + getStringFromPlayer(room.getCompetitor(this.getClientNumber()).getPlayer()));
        room.getCompetitor(this.clientNumber).write("go-to-room," + room.getId() + "," + this.clientIP + ",1," + getStringFromPlayer(player));
    }

    @Override
    public void run() {
        try {
            // Mở luồng vào ra trên Socket tại Server.
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            System.out.println("Khời động luông mới thành công, ID là: " + clientNumber);
            write("server-send-id" + "," + this.clientNumber);

            String message;
            while (!isClosed) {
                message = in.readLine();
                if (message == null) {
                   write("Invalid");
                }
                String[] messageSplit = message.split(",");
                // Xác minh đăng nhập
                if (messageSplit[0].equals("client-verify")) {
                    System.out.println(message);
                    Player player1 = playerDAO.verifyPlayer(new Player(messageSplit[1], messageSplit[2]));
                    if (player1 == null)
                        write("wrong-player," + messageSplit[1] + "," + messageSplit[2]);
                    else if (!player1.isOnline()) {
                        write("login-success," + getStringFromPlayer(player1));
                        this.player = player1;
                        playerDAO.updateToOnline(this.player.getId());
                        Server.serverThreadBus.broadCast(clientNumber, "chat-server," + player1.getNickName() + " đang online");
                        Server.admin.addMessage("[" + player1.getId() + "] " + player1.getNickName() + " đang online");
                    } else if (playerDAO.checkDuplicated(player1.getUsername())) {
                        write("dupplicate-login," + messageSplit[1] + "," + messageSplit[2]);
                    }
                }

                // Xử lý đăng kí
                if (messageSplit[0].equals("register")) {
                    boolean checkDup = playerDAO.checkDuplicated(messageSplit[1]);
                    if (checkDup) write("duplicate-username,");
                    else {
                        Player playerRegister = new Player(messageSplit[1], messageSplit[2], messageSplit[3], messageSplit[4]);
                        playerDAO.addPlayer(playerRegister);
                        this.player = playerDAO.verifyPlayer(playerRegister);
                        playerDAO.updateToOnline(this.player.getId());
                        Server.serverThreadBus.broadCast(clientNumber, "chat-server," + this.player.getNickName() + " đang online");
                        write("login-success," + getStringFromPlayer(this.player));
                    }
                }
                // Xử lý người chơi đăng xuất
                if (messageSplit[0].equals("offline")) {
                    playerDAO.updateToOffline(this.player.getId());
                    Server.admin.addMessage("[" + player.getId() + "] " + player.getNickName() + " đã offline");
                    Server.serverThreadBus.broadCast(clientNumber, "chat-server," + this.player.getNickName() + " đã offline");
                    this.player = null;
                }

                // update
                if (messageSplit[0].equals("update")) {
                    write("update," + getStringFromPlayer(player));
                }

                //Xử lý lấy danh sách bảng xếp hạng
                if (messageSplit[0].equals("leaderboard")) {
                    ArrayList<Player> ranks = playerDAO.getLeaderBoard();
                    StringBuilder res = new StringBuilder("return-leaderboard,");
                    for (Player user : ranks) {
                        res.append(getStringFromPlayer(user)).append(",");
                    }
                    System.out.println(res);
                    write(res.toString());
                }

                // Lấy danh sách người chơi online
                //Xử lý danh sách online
                if (messageSplit[0].equals("onlineList")) {
                    ArrayList<Player> onlineList = playerDAO.getOnlineList();
                    StringBuilder res = new StringBuilder("return-onlineList,");
                    for (Player player : onlineList) {
                        res.append(player.getUsername() + ',' + player.getNickName() + ',' + player.getAvatar() + ',');
                    }
                    System.out.println(res);
                    write(res.toString());
                }

                // Xử lý chat toàn server
                if (messageSplit[0].equals("chat-server")) {
                    assert this.player != null;
                    Server.serverThreadBus.broadCast(clientNumber, messageSplit[0] + "," + this.player.getNickName() + " : " + messageSplit[1]);
                    Server.admin.addMessage("[" + player.getId() + "] " + player.getNickName() + " : " + messageSplit[1]);
                }

                // Xử lý vào phòng trong chức năng tìm kiếm phòng
                if (messageSplit[0].equals("go-to-room")) {
                    int roomName = Integer.parseInt(messageSplit[1]);
                    boolean isFinded = false;
                    for (ServerThread serverThread : Server.serverThreadBus.getListServerThreads()) {
                        if (serverThread.getRoom() != null && serverThread.getRoom().getId() == roomName) {
                            isFinded = true;
                            if (serverThread.getRoom().getNumberOfPlayer() == 2) {
                                write("room-fully,");
                            } else {
                                if (serverThread.getRoom().getPassword() == null || serverThread.getRoom().getPassword().equals(messageSplit[2])) {
                                    this.room = serverThread.getRoom();
                                    room.setPlayer2(this);
                                    this.playerDAO.updateToPlaying(this.player.getId());
                                    goToPartnerRoom();
                                } else {
                                    write("room-wrong-password,");
                                }
                            }
                            break;
                        }
                    }
                    if (!isFinded) {
                        write("room-not-found,");
                    }
                }

                //Xử lý tạo phòng
                if (messageSplit[0].equals("create-room")) {
                    room = new Room(this);
                    if (messageSplit.length == 2) {
                        room.setPassword(messageSplit[1]);
                        write("your-created-room," + room.getId() + "," + messageSplit[1]);
                        System.out.println("Tạo phòng mới thành công, password là " + messageSplit[1]);
                    } else {
                        write("your-created-room," + room.getId());
                        System.out.println("Tạo phòng mới thành công");
                    }
                    playerDAO.updateToPlaying(this.player.getId());
                }

                // Xử lý xem danh sách phòng trống
                if (messageSplit[0].equals("view-room-list")) {
                    StringBuilder res = new StringBuilder("room-list,");
                    int number = 1;
                    for (ServerThread serverThread : Server.serverThreadBus.getListServerThreads()) {
                        if (number > 8) break;
                        if (serverThread.room != null && serverThread.room.getNumberOfPlayer() == 1) {
                            res.append(serverThread.room.getId()).append(",").append(serverThread.room.getPassword()).append(",");
                        }
                        number++;
                    }
                    write(res.toString());
                    System.out.println(res);
                }

                // Xử lý tìm phòng nhanh
                if (messageSplit[0].equals("quick-room")) {
                    boolean isFinded = false;
                    for (ServerThread serverThread : Server.serverThreadBus.getListServerThreads()) {
                        if (serverThread.room != null && serverThread.room.getNumberOfPlayer() == 1 && serverThread.room.getPassword().equals(" ")) {
                            serverThread.room.setPlayer2(this);
                            this.room = serverThread.room;
                            System.out.println("Đã vào phòng " + room.getId());
                            goToPartnerRoom();
                            playerDAO.updateToPlaying(this.player.getId());
                            isFinded = true;
                            //Xử lý phần mời cả 2 người chơi vào phòng
                            break;
                        }
                    }

                    if (!isFinded) {
                        this.room = new Room(this);
                        playerDAO.updateToPlaying(this.player.getId());
                        System.out.println("Không tìm thấy phòng, tạo phòng mới");
                    }
                }

                // Xử lý không tìm được phòng
                if (messageSplit[0].equals("cancel-room")) {
                    assert this.player != null;
                    playerDAO.updateToNotPlaying(this.player.getId());
                    System.out.println("Đã hủy phòng");
                    this.room = null;
                }
                // Xử lý khi có người chơi thứ 2 vào phòng
                if (messageSplit[0].equals("join-room")) {
                    int ID_room = Integer.parseInt(messageSplit[1]);
                    for (ServerThread serverThread : Server.serverThreadBus.getListServerThreads()) {
                        if (serverThread.room != null && serverThread.room.getId() == ID_room) {
                            serverThread.room.setPlayer2(this);
                            this.room = serverThread.room;
                            System.out.println("Đã vào phòng " + room.getId());
                            goToPartnerRoom();
                            playerDAO.updateToPlaying(this.player.getId());
                            break;
                        }
                    }
                }

                // Xử lý khi người chơi thoán phòng
                if (messageSplit[0].equals("leave-room")) {
                    if (room != null) {
                        room.setPlayersToNotPlaying();
                        room.getCompetitor(clientNumber).write("leave-room,");
                        room.getCompetitor(clientNumber).room = null;
                        this.room = null;
                    }
                }

                // Xử lý khi bắt đầu ván chơi
                if (messageSplit[0].equals("new-game")) {
                    assert room != null;
                    if (!room.isSetProduct()) {
                        room.setProduct();
                        room.setSetProduct(true);
                    }
                    Product product = room.getProduct();
                    System.out.println(product.toString());
                    write("game-start," + product.getName() + "," + product.getAmount() + ',' + product.getPrice() + ',' + product.getPicture() + ',');
                }

                // Xử lý khi người chơi đoán giá
                if (messageSplit[0].equals("guess-price")) {
                    assert room != null;
                    room.setPlayersPrice(Integer.parseInt(messageSplit[1]), Double.parseDouble(messageSplit[2]));
                }

                // Xử lý khi cả 2 đều đoán xong
                if (messageSplit[0].equals("result")) {
                    assert room != null;
                    int winner = room.getResult();
                    if (winner == this.player.getId()) {
                        playerDAO.addWin(this.player.getId());
                        write("win,");
                    }
                    else if (winner != -1) {
                        playerDAO.addLose(this.player.getId());
                        write("lose,");
                    }
                    else {
                        playerDAO.addDraw(this.player.getId());
                        write("draw,");
                    }
                    synchronized (room) {
                        if (!room.isDrawHandle()) {
                            room.saveGameMatch(new GameMatch(this.player.getId(), room.getCompetitorID(clientNumber), winner, room.getProduct().getId()));
                            room.setDrawHandle(true);
                        }
                    }
                }

                // Xử lý khi người chơi thắng
//                if (messageSplit[0].equals("win")) {
//                    playerDAO.addWin(this.player.getId());
//                    room.getCompetitor(clientNumber).write("caro," + messageSplit[1] + "," + messageSplit[2]);
//                    room.saveGameMatch(new GameMatch(this.player.getId(), room.getCompetitorID(clientNumber), this.player.getId(), room.getProduct().getId()));
//                    room.broadCast("new-game,");
//                }
//
//                // Xử lý khi người chơi thua
//                if (messageSplit[0].equals("lose")) {
//                    playerDAO.addLose(this.player.getId());
//                    room.getCompetitor(clientNumber).write("caro," + messageSplit[1]);
//                    write("new-game");
//                }
//
//                // Xử lý khi người chơi hoà
//                if (messageSplit[0].equals("draw")) {
//                    playerDAO.addDraw(this.player.getId());
//                    room.getCompetitor(clientNumber).write("caro," + messageSplit[1]);
//                    synchronized (room) {
//                        if (!room.isDrawHandle()) {
//                            room.saveGameMatch(new GameMatch(this.player.getId(), room.getCompetitorID(clientNumber), -1, room.getProduct().getId()));
//                            room.setDrawHandle(true);
//                        }
//                    }
//                    write("new-game");
//                }
            }
        } catch (IOException e) {
            // Thay đổi giá trị cờ để thoát luồng
            isClosed = true;
            // Cập nhật trạng thái của player
            if (this.player != null) {
                playerDAO.updateToOffline(this.player.getId());
                playerDAO.updateToNotPlaying(this.player.getId());
                Server.serverThreadBus.broadCast(clientNumber, "chat-server," + this.player.getNickName() + " đã offline");
                Server.admin.addMessage("[" + player.getId() + "] " + player.getNickName() + " đã offline");
            }

            // remove thread khỏi bus
            Server.serverThreadBus.remove(clientNumber);
            System.out.println(this.clientNumber + " đã thoát");
            if (room != null) {
                try {
                    if (room.getCompetitor(clientNumber) != null) {
                        room.getCompetitor(clientNumber).write("leave-room,");
                        room.getCompetitor(clientNumber).room = null;
                    }
                    this.room = null;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

        }
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getClientNumber() {
        return clientNumber;
    }

    public BufferedReader getIn() {
        return in;
    }

    public PrintWriter getOut() {
        return out;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getClientIP() {
        return clientIP;
    }
}
