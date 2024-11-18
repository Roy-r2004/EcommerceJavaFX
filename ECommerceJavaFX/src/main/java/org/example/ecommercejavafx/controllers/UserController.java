package org.example.ecommercejavafx.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import org.example.ecommercejavafx.models.User;
import org.example.ecommercejavafx.services.UserService;

import java.util.List;

public class UserController {
    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField roleField;

    @FXML
    private Button addButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TextField userIdField;

    @FXML
    private ListView<String> userListView;

    private final UserService userService = new UserService();

    @FXML
    public void initialize() {
        addButton.setOnAction(event -> addUser());
        updateButton.setOnAction(event -> updateUser());
        deleteButton.setOnAction(event -> deleteUser());
        loadUsers();
    }

    private void addUser() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = roleField.getText();
        User user = new User(0, username, password, role);
        userService.addUser(user);
        loadUsers();
    }

    private void updateUser() {
        int id = Integer.parseInt(userIdField.getText());
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = roleField.getText();
        User user = new User(id, username, password, role);
        userService.updateUser(user);
        loadUsers();
    }

    private void deleteUser() {
        int id = Integer.parseInt(userIdField.getText());
        userService.deleteUser(id);
        loadUsers();
    }

    private void loadUsers() {
        List<User> users = userService.getAllUsersForDisplay();
        userListView.getItems().clear();
        for (User user : users) {
            userListView.getItems().add(user.getUsername() + " - " + user.getRole());
        }
    }
}