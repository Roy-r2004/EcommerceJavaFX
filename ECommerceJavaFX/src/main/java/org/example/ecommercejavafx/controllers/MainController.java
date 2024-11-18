package org.example.ecommercejavafx.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainController {
    @FXML
    private Button loginButton;

    @FXML
    public void initialize() {
        loginButton.setOnAction(event -> {
            System.out.println("Login button clicked!");
        });
    }
}