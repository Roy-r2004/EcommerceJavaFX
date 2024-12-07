package org.example.ecommercejavafx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.ecommercejavafx.models.User;
import org.example.ecommercejavafx.services.UserService;
import org.example.ecommercejavafx.utils.DatabaseUtils;
import org.example.ecommercejavafx.utils.SessionManager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    private final UserService userService = new UserService();


    @FXML
    private Button loginButton;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;


    @FXML
    public void initialize() {
        loginButton.setOnAction(e -> handleLoginButton());
    }

    public static String validateUser(String username, String password) {
        try (Connection conn = DatabaseUtils.getConnection()) {
            String query = "SELECT role FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    return rs.getString("role"); // Return the user role
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void handleLoginButton() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        String role = validateUser(username, password);

        if (role != null) {
            SessionManager.setLoggedIn(true); // Set login state
            SessionManager.setUserRole(role); // Save the user role

            User user = userService.getUserDetails(password); // Get full user details from the database
            SessionManager.setCurrentUser(user);

            try {
                FXMLLoader loader;
                Parent root;
                Scene scene;

                if (role.equals("admin")) {
                    loader = new FXMLLoader(getClass().getResource("/views/admin/admin_dashboard.fxml"));
                    root = loader.load();
                    scene = new Scene(root, 1200, 800);
                    scene.getStylesheets().add(getClass().getResource("/styles/admin-dashboard.css").toExternalForm());
                    Stage currentStage = (Stage) loginButton.getScene().getWindow();
                    currentStage.setScene(scene);
                    currentStage.setTitle("Admin Dashboard");
                } else if (role.equals("customer")) {
                    loader = new FXMLLoader(getClass().getResource("/views/customer/customer_dashboard.fxml"));
                    root = loader.load();
                    scene = new Scene(root, 1200, 800);
                    scene.getStylesheets().add(getClass().getResource("/styles/customer_dashboard.css").toExternalForm());
                    Stage currentStage = (Stage) loginButton.getScene().getWindow();
                    currentStage.setScene(scene);
                    currentStage.setTitle("Customer Dashboard");
                } else {
                    // Handle unknown roles, default action
                    showAlert(Alert.AlertType.WARNING, "Access Denied", "Role not recognized. Contact support.");
                }
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while loading the page.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Invalid Login", "No user found with the provided username and password.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void SignUpLink(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/common/SignUp.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1200, 800);
            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            currentStage.setScene(scene);
            currentStage.setTitle("Sign Up Page");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
