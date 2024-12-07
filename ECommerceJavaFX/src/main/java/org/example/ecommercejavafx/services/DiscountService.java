package org.example.ecommercejavafx.services;

import org.example.ecommercejavafx.models.Discount;
import org.example.ecommercejavafx.utils.DatabaseUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiscountService {

    // Add Discount
    public void addDiscount(Discount discount) {
        String sql = "INSERT INTO discounts (code, discount_type, discount_value, start_date, end_date, usage_limit) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, discount.getCode());
            pstmt.setString(2, discount.getDiscountType());
            pstmt.setDouble(3, discount.getDiscountValue());
            pstmt.setTimestamp(4, Timestamp.valueOf(discount.getStartDate()));
            pstmt.setTimestamp(5, Timestamp.valueOf(discount.getEndDate()));
            pstmt.setObject(6, discount.getUsageLimit(), Types.INTEGER);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Discount added successfully.");
            } else {
                System.out.println("Failed to add discount.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete Discount By ID
    public void deleteDiscountById(int discountId) {
        String sql = "DELETE FROM discounts WHERE id = ?";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, discountId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Discount deleted successfully: ID " + discountId);
            } else {
                System.out.println("Failed to delete discount: ID " + discountId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get All Discounts
    public List<Discount> getAllDiscounts() {
        List<Discount> discounts = new ArrayList<>();
        String sql = "SELECT * FROM discounts";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Discount discount = new Discount(
                        rs.getInt("id"),
                        rs.getString("code"),
                        rs.getString("discount_type"),
                        rs.getDouble("discount_value"),
                        rs.getTimestamp("start_date").toLocalDateTime(),
                        rs.getTimestamp("end_date").toLocalDateTime(),
                        (Integer) rs.getObject("usage_limit")
                );
                discounts.add(discount);
            }
            System.out.println("Loaded " + discounts.size() + " discounts from database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return discounts;
    }

    // Get Discount by ID
    public Discount getDiscountById(int discountId) {
        String sql = "SELECT * FROM discounts WHERE id = ?";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, discountId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Discount(
                        rs.getInt("id"),
                        rs.getString("code"),
                        rs.getString("discount_type"),
                        rs.getDouble("discount_value"),
                        rs.getTimestamp("start_date").toLocalDateTime(),
                        rs.getTimestamp("end_date").toLocalDateTime(),
                        (Integer) rs.getObject("usage_limit")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update Discount
    public void updateDiscount(Discount discount) {
        String sql = "UPDATE discounts SET code = ?, discount_type = ?, discount_value = ?, start_date = ?, end_date = ?, usage_limit = ? WHERE id = ?";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, discount.getCode());
            pstmt.setString(2, discount.getDiscountType());
            pstmt.setDouble(3, discount.getDiscountValue());
            pstmt.setTimestamp(4, Timestamp.valueOf(discount.getStartDate()));
            pstmt.setTimestamp(5, Timestamp.valueOf(discount.getEndDate()));
            pstmt.setObject(6, discount.getUsageLimit(), Types.INTEGER);
            pstmt.setInt(7, discount.getId());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Discount updated successfully: ID " + discount.getId());
            } else {
                System.out.println("Failed to update discount: ID " + discount.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete Discount
    public void deleteDiscount(int discountId) {
        String sql = "DELETE FROM discounts WHERE id = ?";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, discountId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Discount deleted successfully: ID " + discountId);
            } else {
                System.out.println("Failed to delete discount: ID " + discountId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Discount getDiscountByCode(String discountCode) {
        String sql = "SELECT * FROM discounts WHERE code = ?";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1,discountCode);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Discount(
                        rs.getInt("id"),
                        rs.getString("code"),
                        rs.getString("discount_type"),
                        rs.getDouble("discount_value"),
                        rs.getTimestamp("start_date").toLocalDateTime(),
                        rs.getTimestamp("end_date").toLocalDateTime(),
                        (Integer) rs.getObject("usage_limit")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
