package Server.View;

import Server.Controller.Room;
import Server.Server;
import Server.Controller.ServerThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Admin extends JFrame implements Runnable {

    private JTextArea displayArea;
    private JTextArea notificationArea;
    private JTextField notificationInput;


    public Admin() {
        setTitle("Admin");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Panel chính
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel cho các nút
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5, 5, 5));
        JButton viewThreadsButton = new JButton("Danh sách luồng");
        JButton viewRoomsButton = new JButton("Danh sách phòng");
        JButton managePlayersButton = new JButton("Quản lý người chơi");
        JButton viewGamesButton = new JButton("Lịch sử đấu");
        JButton manageProductsButton = new JButton("Quản lý sản phẩm");

        buttonPanel.add(viewThreadsButton);
        buttonPanel.add(viewRoomsButton);
        buttonPanel.add(viewGamesButton);
        buttonPanel.add(managePlayersButton);
        buttonPanel.add(manageProductsButton);

        // TextArea để hiển thị thông tin
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane displayScrollPane = new JScrollPane(displayArea);
        displayScrollPane.setPreferredSize(new Dimension(0, 150));

        // Panel cho thông báo (lớn hơn)
        JPanel notificationPanel = new JPanel(new BorderLayout(5, 5));
        notificationArea = new JTextArea();
        notificationArea.setEditable(false);
        JScrollPane notificationScrollPane = new JScrollPane(notificationArea);
        notificationScrollPane.setPreferredSize(new Dimension(0, 250)); // Tăng kích thước
        notificationInput = new JTextField();
        JButton sendNotificationButton = new JButton("Phát thông báo");

        JPanel inputPanel = new JPanel(new BorderLayout(5, 0));
        inputPanel.add(notificationInput, BorderLayout.CENTER);
        inputPanel.add(sendNotificationButton, BorderLayout.EAST);

        notificationPanel.add(notificationScrollPane, BorderLayout.CENTER);
        notificationPanel.add(inputPanel, BorderLayout.SOUTH);

        // Thêm các thành phần vào panel chính
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(displayScrollPane, BorderLayout.CENTER);
        mainPanel.add(notificationPanel, BorderLayout.SOUTH);

        // Thêm panel chính vào frame
        add(mainPanel, BorderLayout.CENTER);

        // Thêm listeners cho các nút
        viewThreadsButton.addActionListener(this::viewThreadsButtonActionPerformed);
        viewRoomsButton.addActionListener(this::viewRoomsButtonActionPerformed);
        managePlayersButton.addActionListener(this::managePlayersButtonActionPerformed);
        viewGamesButton.addActionListener(this::viewGamesButtonActionPerformed);
        manageProductsButton.addActionListener(this::manageProductsButtonActionPerformed);
        sendNotificationButton.addActionListener(this::sendNotificationButtonActionPerformed);
    }

    private void manageProductsButtonActionPerformed(ActionEvent actionEvent) {
        new ProductManagement().setVisible(true);
    }

    private void viewGamesButtonActionPerformed(ActionEvent actionEvent) {
        new MatchHistoryManagement().setVisible(true);
    }

    private void managePlayersButtonActionPerformed(ActionEvent actionEvent) {
        new PlayerManagement().setVisible(true);
    }

    private void sendNotificationButtonActionPerformed(ActionEvent actionEvent) {
        sendMessage();
    }

    private void viewRoomsButtonActionPerformed(ActionEvent actionEvent) {
        StringBuilder res = new StringBuilder();
        int i = 1;
        for (ServerThread serverThread : Server.serverThreadBus.getListServerThreads()) {
            Room room1 = serverThread.getRoom();
            String listUser = "List user ID: ";
            if (room1 != null) {
                if (room1.getNumberOfPlayer() == 1) {
                    listUser += room1.getPlayer1().getPlayer().getId();
                } else {
                    listUser += room1.getPlayer1().getPlayer().getId() + ", " + room1.getPlayer2().getPlayer().getId();
                }
                res.append(i).append(". Room_ID: ").append(room1.getId()).append(", Number of player: ").append(room1.getNumberOfPlayer()).append(", ").append(listUser).append("\n");
                i++;
            }

        }
        displayArea.setText(res.toString());
    }

    public void viewThreadsButtonActionPerformed(ActionEvent e) {
        StringBuilder res = new StringBuilder();
        String room;
        int i = 1;
        for (ServerThread serverThread : Server.serverThreadBus.getListServerThreads()) {
            if (serverThread.getRoom() == null)
                room = null;
            else room = "" + serverThread.getRoom().getId();
            if (serverThread.getPlayer() != null) {
                res.append(i).append(". Client-number: ").append(serverThread.getClientNumber()).append(", User-ID: ").append(serverThread.getPlayer().getId()).append(", Room: ").append(room).append("\n");
            } else {
                res.append(i).append(". Client-number: ").append(serverThread.getClientNumber()).append(", User-ID: null, Room: ").append(room).append("\n");
            }
            i++;
        }
        displayArea.setText(res.toString());
    }

    private void sendMessage() {
        String message = notificationInput.getText();
        if (message.isEmpty()) {
            return;
        }
        String temp = notificationArea.getText();
        temp += "Thông báo từ máy chủ : " + message + "\n";
        notificationArea.setText(temp);
        notificationArea.setCaretPosition(notificationArea.getDocument().getLength());
        Server.serverThreadBus.broadCast(-1, "chat-server,Thông báo từ máy chủ : " + message);
        notificationInput.setText("");
    }

    public void addMessage(String message) {
        String tmp = notificationArea.getText();
        tmp = tmp + message + "\n";
        notificationArea.setText(tmp);
        notificationArea.setCaretPosition(displayArea.getDocument().getLength());
    }

    @Override
    public void run() {
        new Admin().setVisible(true);
    }
}
