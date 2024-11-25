package org.example.ecommercejavafx.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.example.ecommercejavafx.utils.DatabaseUtils;
import org.example.ecommercejavafx.utils.SessionManager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CustomerController {


    @FXML
    private ImageView userIcon;

    @FXML
    private ImageView cartIcon;

    @FXML
    private ImageView notificationIcon;

    @FXML
    private Button loginButton;

    private ContextMenu userMenu;




    public void initialize() {
        // Set icons
        userIcon.setImage(new Image("file:/C:/Users/rami_/IdeaProjects/EcommerceJavaFX/ECommerceJavaFX/src/main/resources/images/user.png"));
        cartIcon.setImage(new Image("file:/C:/Users/rami_/IdeaProjects/EcommerceJavaFX/ECommerceJavaFX/src/main/resources/images/shopping-bag.png"));
        notificationIcon.setImage(new Image("file:/C:/Users/rami_/IdeaProjects/EcommerceJavaFX/ECommerceJavaFX/src/main/resources/images/bell.png"));

        // Toggle visibility based on login state
        if (SessionManager.isLoggedIn()) {
            userIcon.setVisible(true);
            loginButton.setVisible(false);
        } else {
            userIcon.setVisible(false);
            loginButton.setVisible(true);
        }

        // Initialize the context menu for the user icon
        initializeUserMenu();



    }

    private void initializeUserMenu() {
        // Create the ContextMenu
        userMenu = new ContextMenu();

        // Add "My Profile" menu item
        MenuItem myProfile = new MenuItem("My Profile");
        myProfile.setOnAction(event -> goToMyProfile());

        // Add "Logout" menu item
        MenuItem logout = new MenuItem("Logout");
        logout.setOnAction(event -> handleLogout());

        // Add items to the ContextMenu
        userMenu.getItems().addAll(myProfile, logout);

        // Add event listener to show the ContextMenu on user icon click
        userIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (SessionManager.isLoggedIn()) {
                userMenu.show(userIcon, event.getScreenX(), event.getScreenY());
            }
        });
    }

    private void goToMyProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/customer/my_profile.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) userIcon.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("My Profile");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleLogout() {
        // Update session state
        SessionManager.setLoggedIn(false);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/common/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) userIcon.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToLoginPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/common/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login Page");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
