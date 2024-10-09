package Server.View;

import Server.DAO.PlayerDAO;
import Server.Model.Player;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class PlayerManagement extends JFrame {
    private JTextField searchField;
    private JTable playerTable;
    private DefaultTableModel tableModel;
    private PlayerDAO playerDAO;

    public PlayerManagement() {
        playerDAO = new PlayerDAO();

        setTitle("Quản lý người chơi");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Tiêu đề
        JLabel titleLabel = new JLabel("Quản lý người chơi", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Panel tìm kiếm
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Tìm kiếm");
        JButton leaderBoardButton = new JButton("Leaderboard");
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(leaderBoardButton);
        add(searchPanel, BorderLayout.NORTH);

        // Bảng người chơi
        String[] columnNames = {"ID", "Username", "Password", "Nickname", "Avatar", "Win", "Lose", "Draw", "Score"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 4) return ImageIcon.class;
                return Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa bất kỳ ô nào
            }
        };
        playerTable = new JTable(tableModel);
        playerTable.setRowHeight(50); // Để hiển thị avatar tốt hơn
        JScrollPane tableScrollPane = new JScrollPane(playerTable);
        add(tableScrollPane, BorderLayout.CENTER);

        // Nút trở về
        JButton backButton = new JButton("Trở về");
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Thêm dữ liệu mẫu
        addData(playerDAO.getAllPlayers());

        // Xử lý sự kiện
        playerTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Player player = new Player();
                if (e.getClickCount() == 1) {
                    int selectedRow = playerTable.getSelectedRow();
                    player = new Player(
                            String.valueOf(playerTable.getValueAt(selectedRow, 1)),
                            String.valueOf(playerTable.getValueAt(selectedRow, 2)),
                            String.valueOf(playerTable.getValueAt(selectedRow, 3)),
                            String.valueOf(playerTable.getValueAt(selectedRow, 4))
                    );
                }

                new EditPlayer( player);
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchTerm = searchField.getText();
                tableModel.setRowCount(0);
                addData(playerDAO.searchPlayers(searchTerm));
            }
        });

        leaderBoardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.setRowCount(0);
                addData(playerDAO.getLeaderBoard());
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Xử lý sự kiện trở về (chưa triển khai)
                dispose();
            }
        });
    }

    private void addData(ArrayList<Player> players) {
        // Thêm dữ liệu mẫu vào bảng
        ImageIcon avatarIcon = new ImageIcon(new ImageIcon("C:\\Users\\phamn\\Downloads\\gamer.png")
                .getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));

        ArrayList<Object[]> data = new ArrayList<>();
        for (Player player : players) {
            data.add(new Object[] {player.getId(), player.getUsername(), player.getPassword(), player.getNickName(),
                    avatarIcon, player.getWin(), player.getLose(), player.getDraw(), player.getScore()});
        }


        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }
}