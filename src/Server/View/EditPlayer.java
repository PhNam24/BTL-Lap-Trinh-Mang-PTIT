package Server.View;

import Server.DAO.PlayerDAO;
import Server.Model.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class EditPlayer extends JFrame {
    private PlayerDAO playerDAO;

    // Khai báo các thành phần giao diện
    private JTextField usernameField, passwordField,nicknameField;
    private JLabel avatarLabel;
    private JButton updateButton, deleteButton, selectAvatarButton;
    private String avatarPath = "";

    public EditPlayer(Player player) {
        playerDAO = new PlayerDAO();

        setTitle("Edit Player");
        setSize(400, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tạo panel chính
        JPanel mainPanel = new JPanel(new GridLayout(7, 2, 10, 10));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(player.getUsername());
        usernameField.setEditable(false);  // Không cho phép chỉnh sửa Username

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(player.getPassword());

        JLabel nicknameLabel = new JLabel("Nickname:");
        nicknameField = new JTextField(player.getNickName());

        JLabel avatarLabelTitle = new JLabel("Avatar:");
        avatarLabel = new JLabel();
        avatarLabel.setPreferredSize(new Dimension(100, 100));
        avatarLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Nút để chọn Avatar từ hệ thống
        selectAvatarButton = new JButton("Chọn ảnh...");
        selectAvatarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectAvatar();
            }
        });

        // Nút cập nhật
        updateButton = new JButton("Cập nhật");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerDAO.updatePlayer(new Player(
                        usernameField.getText(),
                        passwordField.getText(),
                        nicknameField.getText(),
                        avatarPath
                ));
                showMessage("Cập nhật thành công!!!");
            }
        });

        // Nút xoá
        deleteButton = new JButton("Xoá");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerDAO.deletePlayer(new Player(
                        usernameField.getText(),
                        passwordField.getText(),
                        nicknameField.getText(),
                        avatarPath
                ));
                showMessage("Xoá người chơi thành công!!! ");
            }
        });

        // Thêm các thành phần vào panel
        mainPanel.add(usernameLabel);
        mainPanel.add(usernameField);
        mainPanel.add(passwordLabel);
        mainPanel.add(passwordField);
        mainPanel.add(nicknameLabel);
        mainPanel.add(nicknameField);
        mainPanel.add(avatarLabelTitle);
        mainPanel.add(avatarLabel);
        mainPanel.add(selectAvatarButton);

        // Thêm panel chính và các nút vào cửa sổ
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void showMessage(String s) {
        JOptionPane.showMessageDialog(this, s);
        dispose();
    }

    // Hàm xử lý khi bấm nút chọn Avatar
    private void selectAvatar() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn ảnh Avatar");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            avatarPath = selectedFile.getAbsolutePath();
            ImageIcon avatarIcon = new ImageIcon(new ImageIcon(avatarPath).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
            avatarLabel.setIcon(avatarIcon);
        }
    }
}
