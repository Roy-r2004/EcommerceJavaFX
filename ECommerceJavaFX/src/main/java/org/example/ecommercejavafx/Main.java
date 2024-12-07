package org.example.ecommercejavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static String role; // Added to hold the role of the user

    @Override
    public void start(Stage primaryStage) throws IOException {
        try {
            Parent root;
            FXMLLoader loader;

            // Load the appropriate dashboard based on the user role
            if ("admin".equals(role)) {
                loader = new FXMLLoader(getClass().getResource("/views/admin/admin_dashboard.fxml"));
                root = loader.load();
                primaryStage.setTitle("Admin Dashboard");
            } else {
                loader = new FXMLLoader(getClass().getResource("/views/customer/customer_dashboard.fxml"));
                root = loader.load();
                primaryStage.setTitle("Customer Dashboard");
            }

            Scene scene = new Scene(root, 1200, 800);
            scene.getStylesheets().add(getClass().getResource(
                    "admin".equals(role) ? "/styles/admin-dashboard.css" : "/styles/customer_dashboard.css").toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.setMinWidth(1200);
            primaryStage.setMinHeight(800);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("FXML loading error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void setRole(String userRole) {
        role = userRole; // Setting the user role before starting the application
    }
}
