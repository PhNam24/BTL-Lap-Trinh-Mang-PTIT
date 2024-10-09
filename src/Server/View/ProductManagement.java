package Server.View;

import Server.DAO.ProductDAO;
import Server.Model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ProductManagement extends JFrame {
    private JTextField searchField;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private ProductDAO productDAO;

    public ProductManagement() {
        productDAO = new ProductDAO();

        setTitle("Quản lý sản phẩm");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Title
        JLabel titleLabel = new JLabel("Quản lý sản phẩm", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Tìm kiếm");
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"ID", "Tên sản phẩm", "Đơn vị", "Giá", "Hình ảnh"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 4) return ImageIcon.class;
                return Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Disable editing
            }
        };
        productTable = new JTable(tableModel);
        productTable.setRowHeight(50); // For better image display
        JScrollPane tableScrollPane = new JScrollPane(productTable);
        add(tableScrollPane, BorderLayout.CENTER);

        // Back button
        JButton backButton = new JButton("Trở về");
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Add product data
        addData(productDAO.getAllProducts());

        // Event handling
        productTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    Product product = null;
                    int selectedRow = productTable.getSelectedRow();
                    product = new Product(
                            String.valueOf(productTable.getValueAt(selectedRow, 1)),
                            String.valueOf(productTable.getValueAt(selectedRow, 2)),
                            Double.parseDouble(String.valueOf(productTable.getValueAt(selectedRow, 3))),
                            String.valueOf(productTable.getValueAt(selectedRow, 4))
                    );
                    product.setId(Integer.parseInt(String.valueOf(productTable.getValueAt(selectedRow, 0))));
                    new EditProduct(product);
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchTerm = searchField.getText();
                tableModel.setRowCount(0);
                addData(productDAO.searchProducts(searchTerm));
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void addData(ArrayList<Product> products) {
        ImageIcon imageIcon = new ImageIcon(new ImageIcon("C:\\Users\\phamn\\Downloads\\gamer.png")
                .getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));

        for (Product product : products) {
            tableModel.addRow(new Object[]{
                    product.getId(), product.getName(), product.getAmount(), product.getPrice(), imageIcon
            });
        }
    }
}
