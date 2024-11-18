package org.example.ecommercejavafx.services;

import org.example.ecommercejavafx.models.Shipping;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShippingService {
    private Connection connection;

    // Constructor to initialize the database connection
    public ShippingService() {
        try {
            // Replace the URL, USER, and PASSWORD with your database connection details
            String url = "jdbc:mysql://localhost:3306/ecommerce_db"; // Modify with your database name
            String user = "root"; // Replace with your database username
            String password = "##Rizkallah_2004"; // Replace with your database password

            this.connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to connect to the database.");
        }
    }

    public void addShipping(Shipping shipping) {
        String query = "INSERT INTO shipping (order_id, shipping_provider, tracking_number, estimated_delivery, shipping_status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, shipping.getOrderId());
            statement.setString(2, shipping.getShippingProvider());
            statement.setString(3, shipping.getTrackingNumber());
            statement.setTimestamp(4, Timestamp.valueOf(shipping.getEstimatedDelivery()));
            statement.setString(5, shipping.getShippingStatus());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateShipping(Shipping shipping) {
        String query = "UPDATE shipping SET shipping_provider = ?, tracking_number = ?, estimated_delivery = ?, shipping_status = ? WHERE shipping_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, shipping.getShippingProvider());
            statement.setString(2, shipping.getTrackingNumber());
            statement.setTimestamp(3, Timestamp.valueOf(shipping.getEstimatedDelivery()));
            statement.setString(4, shipping.getShippingStatus());
            statement.setInt(5, shipping.getShippingId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteShipping(int shippingId) {
        String query = "DELETE FROM shipping WHERE shipping_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, shippingId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Shipping> getAllShipping() {
        List<Shipping> shippingList = new ArrayList<>();
        String query = "SELECT * FROM shipping";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Shipping shipping = new Shipping(
                        resultSet.getInt("shipping_id"),
                        resultSet.getInt("order_id"),
                        resultSet.getString("shipping_provider"),
                        resultSet.getString("tracking_number"),
                        resultSet.getTimestamp("estimated_delivery").toLocalDateTime(),
                        resultSet.getString("shipping_status")
                );
                shippingList.add(shipping);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shippingList;
    }
}
