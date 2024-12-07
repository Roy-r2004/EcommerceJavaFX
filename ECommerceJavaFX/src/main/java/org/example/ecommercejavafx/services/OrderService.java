package org.example.ecommercejavafx.services;

import org.example.ecommercejavafx.models.Order;
import org.example.ecommercejavafx.models.Product;
import org.example.ecommercejavafx.utils.DatabaseUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderService {

    // Create Order
    public void addOrder(Order order) {
        String sql = "INSERT INTO orders (user_id, product_id, quantity, total_price, discounted_price, status, discount_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, order.getUserId());
            pstmt.setInt(2, order.getProductId());
            pstmt.setInt(3, order.getQuantity());
            pstmt.setDouble(4, order.getTotalPrice());
            pstmt.setDouble(5, order.getDiscountedPrice());
            pstmt.setString(6, order.getStatus());
            if (order.getDiscountId() != null) {
                pstmt.setInt(7, order.getDiscountId());
            } else {
                pstmt.setNull(7, Types.INTEGER);
            }
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Order added successfully.");
            } else {
                System.out.println("Failed to add order.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Read All Orders
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT orders.*, discounts.discount_value, discounts.discount_type " +
                "FROM orders " +
                "LEFT JOIN discounts ON orders.discount_id = discounts.id";  // Joining with discounts table to get discount info

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                int userId = rs.getInt("user_id");
                int productId = rs.getInt("product_id");
                int quantity = rs.getInt("quantity");
                double totalPrice = rs.getDouble("total_price");
                double discountedPrice = rs.getDouble("discounted_price");
                Timestamp orderDate = rs.getTimestamp("order_date");
                String status = rs.getString("status");
                Integer discountId = rs.getInt("discount_id") != 0 ? rs.getInt("discount_id") : null;

                // Create an Order object and add it to the list
                Order order = new Order(id, userId, productId, quantity, totalPrice, orderDate, status, discountId);
                order.setDiscountedPrice(discountedPrice);  // Set the calculated discounted price

                orders.add(order);
            }
            System.out.println("Loaded " + orders.size() + " orders from database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }


    // Get Order by ID
    public Order getOrderById(int orderId) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Order(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("product_id"),
                        rs.getInt("quantity"),
                        rs.getDouble("total_price"),
                        rs.getTimestamp("order_date"),
                        rs.getString("status"),
                        rs.getInt("discount_id") != 0 ? rs.getInt("discount_id") : null,
                        rs.getDouble("discounted_price")  // Include discounted price
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get Orders by User ID
    public List<Order> getOrdersByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE user_id = ?";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Order order = new Order(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("product_id"),
                        rs.getInt("quantity"),
                        rs.getDouble("total_price"),
                        rs.getTimestamp("order_date"),
                        rs.getString("status"),
                        rs.getInt("discount_id") != 0 ? rs.getInt("discount_id") : null,
                        rs.getDouble("discounted_price")  // Include discounted price
                );
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    // Update Order Details (e.g., quantity, status, total_price, discounted_price)
    public void updateOrder(Order order) {
        String sql = "UPDATE orders SET user_id = ?, product_id = ?, quantity = ?, total_price = ?, discounted_price = ?, status = ?, discount_id = ? WHERE id = ?";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, order.getUserId());
            pstmt.setInt(2, order.getProductId());
            pstmt.setInt(3, order.getQuantity());
            pstmt.setDouble(4, order.getTotalPrice());
            pstmt.setDouble(5, order.getDiscountedPrice());
            pstmt.setString(6, order.getStatus());
            if (order.getDiscountId() != null) {
                pstmt.setInt(7, order.getDiscountId());
            } else {
                pstmt.setNull(7, Types.INTEGER);
            }
            pstmt.setInt(8, order.getId());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Order updated successfully: ID " + order.getId());
            } else {
                System.out.println("Failed to update order: ID " + order.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update Order Status
    public void updateOrderStatus(int orderId, String status) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, orderId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Order status updated successfully: ID " + orderId);
            } else {
                System.out.println("Failed to update order status: ID " + orderId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete Order
    public void deleteOrder(int id) {
        String sql = "DELETE FROM orders WHERE id = ?";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Order deleted successfully: ID " + id);
            } else {
                System.out.println("Failed to delete order: ID " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateOrderItem(int orderId, int productId, int quantity) {
        String query = "UPDATE orders SET quantity = ? WHERE id = ? AND product_id = ?";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, orderId);
            stmt.setInt(3, productId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
