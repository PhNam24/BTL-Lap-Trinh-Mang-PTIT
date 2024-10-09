package Server.View;

import Server.DAO.GameMatchDAO;
import Server.Model.GameMatch;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MatchHistoryManagement extends JFrame {
    private JTextField searchField;
    private JTable matchTable;
    private DefaultTableModel tableModel;
    private GameMatchDAO gameMatchDAO;

    public MatchHistoryManagement() {
        gameMatchDAO = new GameMatchDAO();

        setTitle("Quản lý lịch sử đấu");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Tiêu đề
        JLabel titleLabel = new JLabel("Quản lý lịch sử đấu", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Panel tìm kiếm
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Tìm kiếm");
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        // Bảng lịch sử đấu
        String[] columnNames = {"ID", "Player 1 ID", "Player 2 ID", "Winner ID", "Product ID"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa bất kỳ ô nào
            }
        };
        matchTable = new JTable(tableModel);
        matchTable.setRowHeight(30);
        JScrollPane tableScrollPane = new JScrollPane(matchTable);
        add(tableScrollPane, BorderLayout.CENTER);

        // Nút trở về
        JButton backButton = new JButton("Trở về");
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Thêm dữ liệu
        addData(gameMatchDAO.getAllGameMatches());

        // Xử lý sự kiện
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchTerm = searchField.getText();
                tableModel.setRowCount(0);
                addData(gameMatchDAO.getGameMatchesByPlayerID(Integer.parseInt(searchTerm)));
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void addData(ArrayList<GameMatch> matches) {
        tableModel.setRowCount(0);
        for (GameMatch match : matches) {
            tableModel.addRow(new Object[]{
                    match.getId(),
                    match.getPlayer1ID(),
                    match.getPlayer2ID(),
                    match.getWinnerID(),
                    match.getProductID()
            });
        }
    }
}