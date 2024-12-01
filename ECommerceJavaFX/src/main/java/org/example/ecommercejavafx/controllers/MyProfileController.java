package org.example.ecommercejavafx.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.ecommercejavafx.models.User;
import org.example.ecommercejavafx.services.myProfileService;
import org.example.ecommercejavafx.utils.DatabaseUtils;
import org.example.ecommercejavafx.utils.SessionManager;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MyProfileController {
    public Button backButton;
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

    private User loggedInUser;

    private String profilePicturePath;

    public void initialize() {
        backButton.setOnAction(event -> closeProfile());
        loggedInUser = SessionManager.getCurrentUser();

        if (loggedInUser != null) {
            // Load the user profile
            loadUserProfile(loggedInUser);
        } else {
            System.out.println("No user is currently logged in.");
        }

        profilePicture.setOnMouseClicked(event -> updateProfilePicture());

        // Add save button action
        saveButton.setOnAction(event -> saveProfileChanges());
    }

    private void closeProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/customer/customer_dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("My Profile");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        myProfileService profileService = new myProfileService();
        boolean isUpdated;
        if (checkProfileExists(loggedInUser.getId())) {
            // Update existing profile
            isUpdated = profileService.updateProfile(loggedInUser.getId(), name, email, phone, address, profilePicturePath);
        } else {
            // Insert a new profile
            isUpdated = myProfileService.createProfile(loggedInUser.getId(), name, email, phone, address, profilePicturePath);
        }

        if (isUpdated) {
            showAlert(AlertType.INFORMATION, "Success", "Profile changes saved successfully!");
        } else {
            showAlert(AlertType.ERROR, "Error", "An error occurred while saving the profile changes!");
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadUserProfile(User user) {

        if (!checkProfileExists(user.getId())) {
            // No profile found, show default picture
            profilePicture.setImage(new Image("file:/C:/Users/rami_/IdeaProjects/EcommerceJavaFX/ECommerceJavaFX/src/main/resources/images/default_image.png"));
            return; // No need to query the database, user is new
        }
        String query = "SELECT full_name, email, phone, address, profile_picture FROM user_profiles WHERE user_id = ?";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, user.getId());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Populate fields with user data
                nameField.setText(rs.getString("full_name"));
                emailField.setText(rs.getString("email"));
                phoneField.setText(rs.getString("phone"));
                addressField.setText(rs.getString("address"));

                // Load profile picture
                byte[] profilePicBytes = rs.getBytes("profile_picture");
                if (profilePicBytes != null && profilePicBytes.length > 0) {
                    System.out.println("Profile picture found in database for user: " + user.getId());
                    InputStream inputStream = new ByteArrayInputStream(profilePicBytes);
                    Image image = new Image(inputStream);
                    profilePicture.setImage(image);
                } else {
                    profilePicture.setImage(new Image("file:/C:/Users/rami_/IdeaProjects/EcommerceJavaFX/ECommerceJavaFX/src/main/resources/images/default_image.png"));
                }
            } else {
                showAlert(AlertType.INFORMATION, "Notice", "Profile not found. You can create one now.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Failed to load user profile!");
        }
    }


    private void updateProfilePicture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(profilePicture.getScene().getWindow());

        if (selectedFile != null) {
            profilePicturePath = selectedFile.getAbsolutePath(); // Set the class-level variable
            profilePicture.setImage(new Image(selectedFile.toURI().toString())); // Update the displayed image
        } else {
            showAlert(AlertType.ERROR, "Validation Error", "Please select a profile picture!");
        }
    }


    public boolean checkProfileExists(int userId) {
        String query = "SELECT COUNT(*) FROM user_profiles WHERE user_id = ?";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}