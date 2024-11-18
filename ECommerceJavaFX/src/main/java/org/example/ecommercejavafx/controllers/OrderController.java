package org.example.ecommercejavafx.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import org.example.ecommercejavafx.models.Order;
import org.example.ecommercejavafx.services.OrderService;

import java.util.List;

public class OrderController {

    @FXML
    private VBox orderManagementSection;

    @FXML
    private ListView<Order> orderListView;

    @FXML
    private TextField orderIdField;

    @FXML
    private ComboBox<String> orderStatusComboBox;

    @FXML
    private Button addOrderButton;

    @FXML
    private Button updateOrderStatusButton;

    @FXML
    private Button deleteOrderButton;

    private final OrderService orderService = new OrderService();

    @FXML
    public void initialize() {
        // Load orders on initialization
        loadOrders();

        // Set the order status ComboBox with default statuses
        orderStatusComboBox.getItems().addAll("Pending", "Shipped", "Delivered");

        // Set a custom display format for orders
        orderListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Order order, boolean empty) {
                super.updateItem(order, empty);
                if (empty || order == null) {
                    setText(null);
                } else {
                    String orderInfo = String.format("Order ID: %d | User ID: %d | Product ID: %d | Qty: %d | Status: %s | Total: $%.2f",
                            order.getId(), order.getUserId(), order.getProductId(), order.getQuantity(), order.getStatus(), order.getTotalPrice());
                    setText(orderInfo);
                }
            }
        });

        // Set button actions for Order Management
        addOrderButton.setOnAction(event -> addOrder());
        updateOrderStatusButton.setOnAction(event -> updateOrderStatus());
        deleteOrderButton.setOnAction(event -> deleteOrder());
    }

    private void loadOrders() {
        List<Order> orders = orderService.getAllOrders();
        orderListView.getItems().clear();
        orderListView.getItems().addAll(orders);
        System.out.println("Loaded " + orders.size() + " orders.");
    }

    private void addOrder() {
        // Placeholder method to add order
        System.out.println("Add Order clicked - Implement the logic as needed.");
    }

    private void updateOrderStatus() {
        try {
            int orderId = Integer.parseInt(orderIdField.getText());
            String status = orderStatusComboBox.getValue();
            if (status == null) {
                System.out.println("Please select a valid order status.");
                return;
            }
            orderService.updateOrderStatus(orderId, status);
            System.out.println("Order status updated successfully.");
            loadOrders();
        } catch (NumberFormatException e) {
            System.out.println("Error: Order ID must be a number.");
        }
    }

    private void deleteOrder() {
        try {
            int orderId = Integer.parseInt(orderIdField.getText());
            orderService.deleteOrder(orderId);
            System.out.println("Order deleted successfully.");
            loadOrders();
        } catch (NumberFormatException e) {
            System.out.println("Error: Order ID must be a number.");
        }
    }
}
