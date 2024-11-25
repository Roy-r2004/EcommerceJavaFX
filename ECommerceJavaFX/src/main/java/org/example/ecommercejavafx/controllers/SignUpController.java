package org.example.ecommercejavafx.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.ecommercejavafx.models.User;
import org.example.ecommercejavafx.services.UserService;

import java.io.IOException;
import java.util.Objects;

public class SignUpController {

    @FXML
    private Button SignUpButton;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField ConfirmPasswordField;

    @FXML
    public void initialize() {
        SignUpButton.setOnAction(e-> {
            try {
                handleSignUpButton();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private final UserService userService = new UserService();


    private boolean checkPassword(){
        String password1= passwordField.getText();
        String password2= ConfirmPasswordField.getText();
if(Objects.equals(password1, password2))
    return true;
else{
    return false;
}
    }

    @FXML
    private void handleSignUpButton() throws IOException {
        FXMLLoader loader = null;
        Parent root = null;
        Scene scene = null;
        String username = usernameField.getText();
        String password1= passwordField.getText();
        String password2= ConfirmPasswordField.getText();

        if (username.isEmpty() || password1.isEmpty() || password2.isEmpty() ) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "All fields must be filled to Sign Up.");
            return;
        }
        if(checkPassword()) {
            User user = new User(0, username, password1, "customer");
            userService.addUser(user);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Account Created Successfully!" + username);
            loader = new FXMLLoader(getClass().getResource("/views/customer/customer_dashboard.fxml"));
            root = loader.load();
            scene = new Scene(root, 1200, 800);
            Stage currentStage = (Stage) SignUpButton.getScene().getWindow();  // Get current stage
            currentStage.setScene(scene);
            currentStage.setTitle("Customer Dashboard");
            currentStage.show();
        }
        else {
            showAlert(Alert.AlertType.ERROR,"Password mismatch","Passwords do not match");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
