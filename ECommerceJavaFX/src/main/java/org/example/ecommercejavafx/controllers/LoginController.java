package org.example.ecommercejavafx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.ecommercejavafx.utils.DatabaseUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {
    @FXML
    private Button loginButton;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    public void initialize() {
        loginButton.setOnAction(e->handleLoginButton());
    }

    public static String validateUser(String username, String password) {
        try (Connection conn = DatabaseUtils.getConnection()) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();

                System.out.println("Username: " + username);
                System.out.println("Password: " + password);

                if (rs.next()) {
                    String role = rs.getString("role");
                    System.out.println("Role: " + role);  // Check the role value
                    return role;
                } else {
                    System.out.println("No user found with that username and password.");
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public void handleLoginButton() {
String password=passwordField.getText();
String username=usernameField.getText();
String role=validateUser(username,password);

        if (role != null) {
            try {
                FXMLLoader loader = null;
                Parent root = null;
                Scene scene = null;

                if (role.equals("admin")) {
                    loader = new FXMLLoader(getClass().getResource("/views/admin/admin_dashboard.fxml"));
                    root = loader.load();
                    scene = new Scene(root, 1200, 800);
                    scene.getStylesheets().add(getClass().getResource("/styles/admin-dashboard.css").toExternalForm());
                    Stage currentStage = (Stage) loginButton.getScene().getWindow();  // Get current stage
                    currentStage.setScene(scene);
                    currentStage.setTitle("Admin Dashboard");
                    currentStage.show();
                } else if (role.equals("customer")) {
                    loader = new FXMLLoader(getClass().getResource("/views/customer/customer_dashboard.fxml"));
                    root = loader.load();
                    scene = new Scene(root, 1200, 800);
                    scene.getStylesheets().add(getClass().getResource("/styles/customer_dashboard.css").toExternalForm());
                    Stage currentStage = (Stage) loginButton.getScene().getWindow();  // Get current stage
                    currentStage.setScene(scene);
                    currentStage.setTitle("Customer Dashboard");
                    currentStage.show();
                }
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "An error occurred while loading the page.");
            }
        } else {
            showAlert("Invalid Login", "No user found with the provided username and password.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);  // You can set a header if needed
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void SignUpLink(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/common/SignUp.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1200, 800);
            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            currentStage.setTitle("Sign Up page");
            currentStage.setScene(scene);
            currentStage.setMinWidth(1200);
            currentStage.setMinHeight(800);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("FXML loading error: " + e.getMessage());
        }
    }


}
