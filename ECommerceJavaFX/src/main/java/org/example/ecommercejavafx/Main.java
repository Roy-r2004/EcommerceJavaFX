package org.example.ecommercejavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/admin/admin_dashboard.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1200, 800); // Set initial width to 1200 and height to 800
        scene.getStylesheets().add(getClass().getResource("/styles/admin-dashboard.css").toExternalForm());

        primaryStage.setTitle("Admin Dashboard");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1200); // Prevent shrinking below 1200px width
        primaryStage.setMinHeight(800); // Prevent shrinking below 800px height
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
