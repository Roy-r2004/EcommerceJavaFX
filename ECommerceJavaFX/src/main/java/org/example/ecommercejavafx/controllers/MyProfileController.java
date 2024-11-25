package org.example.ecommercejavafx.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MyProfileController {
    @FXML
    private ImageView profilePicture;

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField addressField;

    @FXML
    private Button saveButton;

    public void initialize() {
        // Load a placeholder profile picture
        profilePicture.setImage(new Image("file:/path-to-placeholder-profile-image.png"));

        // Pre-fill user data (simulate data fetch from the database)
        nameField.setText("John Doe");
        emailField.setText("john.doe@example.com");
        phoneField.setText("+1234567890");
        addressField.setText("123 Main Street, City, Country");

        // Add save button action
        saveButton.setOnAction(event -> saveProfileChanges());
    }

    private void saveProfileChanges() {
        // Simulate saving profile changes (you can add database integration here)
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();

        // Validation (basic example)
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            showAlert(AlertType.ERROR, "Validation Error", "All fields are required!");
            return;
        }

        // Simulate success message
        showAlert(AlertType.INFORMATION, "Success", "Profile changes saved successfully!");
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
