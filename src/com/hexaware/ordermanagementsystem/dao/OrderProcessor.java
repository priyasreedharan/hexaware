package com.hexaware.ordermanagementsystem.dao;
import com.hexaware.ordermanagementsystem.entity.Product;
import com.hexaware.ordermanagementsystem.entity.User;
import com.hexaware.ordermanagementsystem.exception.OrderNotFoundException;
import com.hexaware.ordermanagementsystem.exception.UserNotFoundException;
import com.hexaware.ordermanagementsystem.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderProcessor implements IOrderManagementRepository {

    @Override
    public void createUser(User user) {
        try (Connection conn = DBUtil.getDBConn()) {
            String sql = "INSERT INTO users (userId, username, password, role) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, user.getUserId());
            pstmt.setString(2, user.getUsername());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getRole());

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("User inserted into DB.");
            } else {
                System.out.println("User not inserted.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createProduct(User user, Product product) {
        if (!user.getRole().equalsIgnoreCase("Admin")) {
            System.out.println("Only Admin can create products.");
            return;
        }

        try (Connection conn = DBUtil.getDBConn()) {
            String sql = "INSERT INTO products (productId, productName, description, price, quantityInStock, type) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, product.getProductId());
            pstmt.setString(2, product.getProductName());
            pstmt.setString(3, product.getDescription());
            pstmt.setDouble(4, product.getPrice());
            pstmt.setInt(5, product.getQuantityInStock());
            pstmt.setString(6, product.getType());

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Product inserted into DB.");
            } else {
                System.out.println("Product not inserted.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createOrder(User user, List<Product> products) throws UserNotFoundException {
        try (Connection conn = DBUtil.getDBConn()) {
            String checkUser = "SELECT * FROM users WHERE userId = ?";
            PreparedStatement pstmt = conn.prepareStatement(checkUser);
            pstmt.setInt(1, user.getUserId());
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                throw new UserNotFoundException("User not found with ID: " + user.getUserId());
            }

            String insertOrder = "INSERT INTO orders (userId, productId) VALUES (?, ?)";
            for (Product product : products) {
                PreparedStatement orderStmt = conn.prepareStatement(insertOrder);
                orderStmt.setInt(1, user.getUserId());
                orderStmt.setInt(2, product.getProductId());
                orderStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cancelOrder(int userId, int orderId) throws UserNotFoundException, OrderNotFoundException {
        try (Connection conn = DBUtil.getDBConn()) {
            String checkUser = "SELECT * FROM users WHERE userId = ?";
            PreparedStatement pstmt = conn.prepareStatement(checkUser);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                throw new UserNotFoundException("User not found with ID: " + userId);
            }

            String checkOrder = "SELECT * FROM orders WHERE orderId = ?";
            pstmt = conn.prepareStatement(checkOrder);
            pstmt.setInt(1, orderId);
            rs = pstmt.executeQuery();

            if (!rs.next()) {
                throw new OrderNotFoundException("Order not found with ID: " + orderId);
            }

            String deleteOrder = "DELETE FROM orders WHERE orderId = ?";
            pstmt = conn.prepareStatement(deleteOrder);
            pstmt.setInt(1, orderId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();

        try (Connection conn = DBUtil.getDBConn()) {
            String sql = "SELECT * FROM products";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int productId = rs.getInt("productId");
                String name = rs.getString("productName");
                String description = rs.getString("description");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantityInStock");
                String type = rs.getString("type");

                Product product = new Product(productId, name, description, price, quantity, type);
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    @Override
    public List<Product> getOrderByUser(User user) throws UserNotFoundException {
        List<Product> products = new ArrayList<>();

        try (Connection conn = DBUtil.getDBConn()) {
            String checkUser = "SELECT * FROM users WHERE userId = ?";
            PreparedStatement pstmt = conn.prepareStatement(checkUser);
            pstmt.setInt(1, user.getUserId());
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                throw new UserNotFoundException("User not found with ID: " + user.getUserId());
            }

            String sql = "SELECT * FROM orders WHERE userId = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, user.getUserId());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                int productId = rs.getInt("productId");
                Product product = getProductById(productId);
                if (product != null) {
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    private Product getProductById(int productId) {
        try (Connection conn = DBUtil.getDBConn()) {
            String sql = "SELECT * FROM products WHERE productId = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("productName");
                String description = rs.getString("description");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantityInStock");
                String type = rs.getString("type");

                return new Product(productId, name, description, price, quantity, type);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
