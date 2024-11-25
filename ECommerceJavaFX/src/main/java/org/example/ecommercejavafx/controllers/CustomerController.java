package org.example.ecommercejavafx.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CustomerController {
    @FXML
    private ImageView userIcon;

    @FXML
    private ImageView cartIcon;

    @FXML
    private ImageView notificationIcon;

    public void initialize() {
        // Set icons using absolute paths
        userIcon.setImage(new Image("file:/C:/Users/rami_/IdeaProjects/EcommerceJavaFX/ECommerceJavaFX/src/main/resources/images/user.png"));
        cartIcon.setImage(new Image("file:/C:/Users/rami_/IdeaProjects/EcommerceJavaFX/ECommerceJavaFX/src/main/resources/images/shopping-bag.png"));
        notificationIcon.setImage(new Image("file:/C:/Users/rami_/IdeaProjects/EcommerceJavaFX/ECommerceJavaFX/src/main/resources/images/bell.png"));
    }
}
