package Server.View;

import Server.DAO.ProductDAO;
import Server.Model.Product;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class EditProduct extends JFrame {
    private ProductDAO productDAO;

    // UI components
    private JTextField nameField, priceField, quantityField;
    private JLabel imageLabel;
    private JButton updateButton, deleteButton, selectImageButton;
    private String imagePath = "";

    public EditProduct(Product product) {
        productDAO = new ProductDAO();

        setTitle("Chỉnh sửa sản phẩm");
        setSize(400, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new GridLayout(6, 2, 10, 10));

        JLabel nameLabel = new JLabel("Tên sản phẩm:");
        nameField = new JTextField(product.getName());

        JLabel priceLabel = new JLabel("Giá:");
        priceField = new JTextField(String.valueOf(product.getPrice()));

        JLabel quantityLabel = new JLabel("Số lượng:");
        quantityField = new JTextField(String.valueOf(product.getAmount()));

        JLabel imageLabelTitle = new JLabel("Hình ảnh:");
        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(100, 100));
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Button to select image
        selectImageButton = new JButton("Chọn hình ảnh...");
        selectImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectImage();
            }
        });

        // Update button
        updateButton = new JButton("Cập nhật");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                productDAO.updateProduct(new Product(
                        product.getId(),
                        nameField.getText(),
                        quantityField.getText(),
                        Double.parseDouble(priceField.getText()),
                        imagePath
                ));
                showMessage("Cập nhật thành công!");
            }
        });

        // Delete button
        deleteButton = new JButton("Xoá");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                productDAO.deleteProduct(new Product(
                        product.getId(),
                        nameField.getText(),
                        priceField.getText(),
                        Double.parseDouble(quantityField.getText()),
                        imagePath
                ));
                showMessage("Xoá sản phẩm thành công!");
            }
        });

        // Add components to main panel
        mainPanel.add(nameLabel);
        mainPanel.add(nameField);
        mainPanel.add(priceLabel);
        mainPanel.add(priceField);
        mainPanel.add(quantityLabel);
        mainPanel.add(quantityField);
        mainPanel.add(imageLabelTitle);
        mainPanel.add(imageLabel);
        mainPanel.add(selectImageButton);

        // Add panels to the frame
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
        dispose();
    }

    // Method to select an image
    private void selectImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn hình ảnh");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            imagePath = selectedFile.getAbsolutePath();
            ImageIcon imageIcon = new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
            imageLabel.setIcon(imageIcon);
        }
    }
}
