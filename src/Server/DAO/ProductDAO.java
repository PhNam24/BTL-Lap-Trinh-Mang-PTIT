package Server.DAO;

import Server.Model.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class ProductDAO extends DAO {

    public ProductDAO() {
        super();
    }

    // Thêm sản phẩm
    public void addProduct(Product product) {
        try {
            String query = "INSERT INTO product(name, amount, price, picture) VALUES(?, ?, ?, ?)";
            PreparedStatement stm = con.prepareStatement(query);
            stm.setString(1, product.getName());
            stm.setString(2, product.getAmount());
            stm.setDouble(3, product.getPrice());
            stm.setString(4, product.getPicture());
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Sửa thông tin sản phẩm
    public void updateProduct(Product product) {
        try {
            String query = "UPDATE product SET name = ?, amount = ?, price = ?, picture = ? WHERE id = ?";
            PreparedStatement stm = con.prepareStatement(query);
            stm.setString(1, product.getName());
            stm.setString(2, product.getAmount());
            stm.setDouble(3, product.getPrice());
            stm.setString(4, product.getPicture());
            stm.setInt(5, product.getId());
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xoá sản phẩm
    public void deleteProduct(Product product) {
        try {
            String query = "DELETE FROM product WHERE id = ?";
            PreparedStatement stm = con.prepareStatement(query);
            stm.setInt(1, product.getId());
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lấy tên sản phẩm theo ID
    public String getProductName(int id) {
        try {
            String query = "SELECT name FROM product WHERE id = ?";
            PreparedStatement stm = con.prepareStatement(query);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // lấy sản phẩm ngẫu nhiên
    public Product getRandomProduct() {
        ArrayList<Product> products = getAllProducts();
        return products.get(new Random().nextInt(products.size()));
    }

    // Tìm kiếm sản phẩm
    public ArrayList<Product> searchProducts(String keyword) {
        ArrayList<Product> products = new ArrayList<>();
        try {
            String query = "SELECT * FROM product WHERE name LIKE ?";
            PreparedStatement stm = con.prepareStatement(query);
            stm.setString(1, "%" + keyword + "%");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                products.add(new Product(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getDouble(4),
                        rs.getString(5)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    // Lấy danh sách tất cả sản phẩm
    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> products = new ArrayList<>();
        try {
            String query = "SELECT * FROM product";
            PreparedStatement stm = con.prepareStatement(query);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                products.add(new Product(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getDouble(4),
                        rs.getString(5)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
}
