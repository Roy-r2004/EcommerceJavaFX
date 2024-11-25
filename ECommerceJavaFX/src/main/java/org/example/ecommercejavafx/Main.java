package org.example.ecommercejavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/customer/customer_dashboard.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1200, 800);
            scene.getStylesheets().add(getClass().getResource("/styles/customer_dashboard.css").toExternalForm());
            primaryStage.setTitle("Customer Page");
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
}
