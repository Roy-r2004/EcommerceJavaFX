package org.example.ecommercejavafx.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.ecommercejavafx.models.*;
import org.example.ecommercejavafx.services.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AdminController {

    // User Management Fields
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField roleField;
    @FXML
    private Button addUserButton;
    @FXML
    private TableView<User> userTableView;
    @FXML
    private TableColumn<User, Integer> userIdColumn;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, String> roleColumn;
    @FXML
    private TableColumn<User, Void> actionColumn;
    @FXML
    private ScrollPane userManagementScrollPane;

    // Product Management Fields
    @FXML
    private TextField productIdField;
    @FXML
    private TextField productNameField;
    @FXML
    private TextField productPriceField;
    @FXML
    private TextField productDescriptionField;
    @FXML
    private TextField productQuantityField;
    @FXML
    private ComboBox<Category> productCategoryComboBox;
    @FXML
    private Button selectImageButton;
    @FXML
    private Label selectedImageLabel;
    @FXML
    private Button addProductButton;
    @FXML
    private Button addShippingButton;
    @FXML
    private Button deleteShippingButton;
    @FXML
    private Button updateShippingButton;


    @FXML
    private Button updateProductButton;
    @FXML
    private Button deleteProductButton;
    @FXML
    private TableView<Product> productTableView;
    @FXML
    private TableColumn<Product, Integer> productIdColumn;
    @FXML
    private TableColumn<Product, String> productNameColumn;
    @FXML
    private TableColumn<Product, Double> productPriceColumn;
    @FXML
    private TableColumn<Product, Integer> productQuantityColumn;
    @FXML
    private TableColumn<Product, Void> productActionsColumn;
    @FXML
    private ScrollPane productManagementScrollPane;

    // Order Management Fields
    @FXML
    private TextField orderIdField;
    @FXML
    private TextField userIdOrderField;
    @FXML
    private TextField productIdOrderField;
    @FXML
    private TextField orderQuantityField;
    @FXML
    private TextField totalPriceField;
    @FXML
    private ComboBox<String> orderStatusComboBox;
    @FXML
    private Button addOrderButton;
    @FXML
    private Button updateOrderButton;
    @FXML
    private Button deleteOrderButton;
    @FXML
    private Button cancelOrderButton;
    @FXML
    private TableView<Order> orderTableView;
    @FXML
    private TableColumn<Order, Integer> orderIdColumn;
    @FXML
    private TableColumn<Order, Integer> orderUserIdColumn;
    @FXML
    private TableColumn<Order, Integer> orderProductIdColumn;
    @FXML
    private TableColumn<Order, Integer> orderQuantityColumn;
    @FXML
    private TableColumn<Order, Double> orderTotalPriceColumn;
    @FXML
    private TableColumn<Order, String> orderStatusColumn;
    @FXML
    private TableColumn<Order, Void> orderActionsColumn;
    @FXML
    private ScrollPane orderManagementScrollPane;

    // Shipping Management Fields
    @FXML
    private TextField shippingIdField;
    @FXML
    private TextField orderIdShippingField;
    @FXML
    private TextField shippingProviderField;
    @FXML
    private TextField trackingNumberField;
    @FXML
    private DatePicker estimatedDeliveryField;
    @FXML
    private ComboBox<String> shippingStatusComboBox;
    @FXML
    private TableView<Shipping> shippingTableView;
    @FXML
    private TableColumn<Shipping, Integer> shippingIdColumn;
    @FXML
    private TableColumn<Shipping, Integer> shippingOrderIdColumn;
    @FXML
    private TableColumn<Shipping, String> shippingProviderColumn;
    @FXML
    private TableColumn<Shipping, String> trackingNumberColumn;
    @FXML
    private TableColumn<Shipping, LocalDateTime> estimatedDeliveryColumn;
    @FXML
    private TableColumn<Shipping, String> shippingStatusColumn;

    @FXML
    private  ScrollPane shippingManagementScrollPane;






    // Services
    private final UserService userService = new UserService();
    private final ProductService productService = new ProductService();
    private final CategoryService categoryService = new CategoryService();
    private final OrderService orderService = new OrderService();

    private final ShippingService shippingService = new ShippingService();


    private File selectedImageFile;

    @FXML
    public void initialize() {
        // User Management Initialization
        addUserButton.setOnAction(event -> addUser());
        loadUsers();
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        addActionButtonsToUserTable();

        // Product Management Initialization
        selectImageButton.setOnAction(event -> selectImage());
        addProductButton.setOnAction(event -> addProduct());
        deleteProductButton.setOnAction(event -> deleteProduct());
        loadProducts();
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        productQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        addProductActionButtonsToTable();
        loadCategories();

        // Order Management Initialization
        addOrderButton.setOnAction(event -> addOrder());
        deleteOrderButton.setOnAction(event -> deleteOrder());
        cancelOrderButton.setOnAction(event -> cancelOrder());
        loadOrders();
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        orderUserIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        orderProductIdColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
        orderQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        orderTotalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        orderStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        addOrderActionButtonsToTable();

        // Shipping Management Initialization

        shippingIdColumn.setCellValueFactory(new PropertyValueFactory<>("shippingId"));
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        shippingProviderColumn.setCellValueFactory(new PropertyValueFactory<>("shippingProvider"));
        trackingNumberColumn.setCellValueFactory(new PropertyValueFactory<>("trackingNumber"));
        estimatedDeliveryColumn.setCellValueFactory(new PropertyValueFactory<>("estimatedDelivery"));
        shippingStatusColumn.setCellValueFactory(new PropertyValueFactory<>("shippingStatus"));
        shippingStatusComboBox.getItems().addAll("In Transit", "Delivered", "Pending", "Cancelled");
        addShippingButton.setOnAction(event -> addShipping());
        deleteShippingButton.setOnAction(event -> deleteShipping());
        loadShipping();
        addShippingActionButtonsToTable();


    }
 // Shipping methods
 private void loadShipping() {
     List<Shipping> shippingList = shippingService.getAllShipping();
     shippingTableView.getItems().clear();
     shippingTableView.getItems().addAll(shippingList);
     System.out.println("Loaded " + shippingList.size() + " shipping records.");
 }

    @FXML
    private void addShipping() {
        try {
            int orderId = Integer.parseInt(orderIdShippingField.getText());
            String provider = shippingProviderField.getText();
            String trackingNumber = trackingNumberField.getText();
            LocalDateTime estimatedDelivery = estimatedDeliveryField.getValue().atStartOfDay();
            String status = shippingStatusComboBox.getValue();

            if (provider.isEmpty() || trackingNumber.isEmpty() || status.isEmpty() || estimatedDelivery == null) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "All fields must be filled in to add shipping.");
                return;
            }

            Shipping shipping = new Shipping(0, orderId, provider, trackingNumber, estimatedDelivery, status);
            shippingService.addShipping(shipping);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Shipping record added successfully.");
            loadShippingTable();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Order ID must be a number.");
        }
    }


    @FXML
    private void deleteShipping() {
        Shipping selectedShipping = shippingTableView.getSelectionModel().getSelectedItem();
        if (selectedShipping != null) {
            shippingService.deleteShipping(selectedShipping.getShippingId());
            showAlert(Alert.AlertType.INFORMATION, "Success", "Shipping record deleted successfully.");
            loadShippingTable();
        } else {
            showAlert(Alert.AlertType.ERROR, "No Selection", "Please select a shipping record to delete.");
        }
    }



    private void loadShippingTable() {
        List<Shipping> shippingList = shippingService.getAllShipping();
        ObservableList<Shipping> shippingObservableList = FXCollections.observableArrayList(shippingList);
        shippingTableView.setItems(shippingObservableList);
    }


    @FXML
    private void updateShipping() {
        try {
            // Get the input values
            int shippingId = Integer.parseInt(shippingIdField.getText().trim());
            String provider = shippingProviderField.getText().trim();
            String trackingNumber = trackingNumberField.getText().trim();
            LocalDate estimatedDeliveryDate = estimatedDeliveryField.getValue();
            String status = shippingStatusComboBox.getValue();

            // Check if the fields are filled properly
            if (provider.isEmpty() || trackingNumber.isEmpty() || estimatedDeliveryDate == null || status.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please fill in all fields.");
                return;
            }

            // Convert LocalDate to LocalDateTime for the Shipping model
            LocalDateTime estimatedDelivery = estimatedDeliveryDate.atStartOfDay();

            // Create a new Shipping object with updated details
            Shipping shipping = new Shipping(shippingId, Integer.parseInt(orderIdShippingField.getText()), provider, trackingNumber, estimatedDelivery, status);

            // Use ShippingService to update the shipping record in the database
            shippingService.updateShipping(shipping);

            // Refresh the table
            loadShippingTable();

            // Confirmation message
            showAlert(Alert.AlertType.INFORMATION, "Success", "Shipping record updated successfully");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter valid numerical values for Shipping ID and Order ID.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Update Failed", "An error occurred while updating shipping information.");
        }
    }

    // Utility function to show alert dialog
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void clearShippingFields() {
        shippingIdField.clear();
        orderIdShippingField.clear();
        shippingProviderField.clear();
        trackingNumberField.clear();
        estimatedDeliveryField.setValue(null);
        shippingStatusComboBox.setValue(null);
    }

    private void addShippingActionButtonsToTable() {
        TableColumn<Shipping, Void> actionColumn = new TableColumn<>("Actions");

        Callback<TableColumn<Shipping, Void>, TableCell<Shipping, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Shipping, Void> call(final TableColumn<Shipping, Void> param) {
                final TableCell<Shipping, Void> cell = new TableCell<>() {
                    private final Button updateButton = new Button("Update");

                    {
                        updateButton.setOnAction(event -> {
                            Shipping shipping = getTableView().getItems().get(getIndex());
                            handleUpdateShipping(shipping);
                        });

                        HBox actionButtons = new HBox(updateButton);
                        actionButtons.setSpacing(10);
                        setGraphic(actionButtons);
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(getGraphic());
                        }
                    }
                };
                return cell;
            }
        };
        actionColumn.setCellFactory(cellFactory);
        shippingTableView.getColumns().add(actionColumn);
    }

    private void handleUpdateShipping(Shipping shipping) {
        shippingIdField.setText(String.valueOf(shipping.getShippingId()));
        orderIdShippingField.setText(String.valueOf(shipping.getOrderId()));
        shippingProviderField.setText(shipping.getShippingProvider());
        trackingNumberField.setText(shipping.getTrackingNumber());
        estimatedDeliveryField.setValue(shipping.getEstimatedDelivery().toLocalDate());
        shippingStatusComboBox.setValue(shipping.getShippingStatus());
        System.out.println("Loaded shipping record for update.");
    }





    // User Management Methods
    private void addActionButtonsToUserTable() {
        Callback<TableColumn<User, Void>, TableCell<User, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<User, Void> call(final TableColumn<User, Void> param) {
                final TableCell<User, Void> cell = new TableCell<>() {

                    private final Button updateButton = new Button("Update");
                    private final Button deleteButton = new Button("Delete");

                    {
                        updateButton.setOnAction(event -> {
                            User user = getTableView().getItems().get(getIndex());
                            handleUpdateUser(user);
                        });

                        deleteButton.setOnAction(event -> {
                            User user = getTableView().getItems().get(getIndex());
                            handleDeleteUser(user);
                        });

                        HBox actionButtons = new HBox(updateButton, deleteButton);
                        actionButtons.setSpacing(10);
                        setGraphic(actionButtons);
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(getGraphic());
                        }
                    }
                };
                return cell;
            }
        };
        actionColumn.setCellFactory(cellFactory);
    }

    @FXML
    private void addUser() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = roleField.getText();

        if (username.isEmpty() || password.isEmpty() || role.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "All fields must be filled to add a user.");
            return;
        }

        User user = new User(0, username, password, role);
        userService.addUser(user);
        showAlert(Alert.AlertType.INFORMATION, "Success", "User added successfully: " + username);
        loadUsers();
        addActionButtonsToUserTable();
        clearUserFields();
    }


    private void handleUpdateUser(User user) {
        usernameField.setText(user.getUsername());
        roleField.setText(user.getRole());
        addUserButton.setText("Update User");
        addUserButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String role = roleField.getText();

            if (username.isEmpty() || password.isEmpty() || role.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "All fields must be filled to update a user.");
                return;
            }

            user.setUsername(username);
            user.setPassword(password);
            user.setRole(role);
            userService.updateUser(user);
            showAlert(Alert.AlertType.INFORMATION, "Success", "User updated successfully: " + username);
            loadUsers();
            addActionButtonsToUserTable();
            clearUserFields();
            addUserButton.setText("Add User");
            addUserButton.setOnAction(event2 -> addUser());
        });
    }


    private void handleDeleteUser(User user) {
        userService.deleteUser(user.getId());
        showAlert(Alert.AlertType.INFORMATION, "Success", "User deleted successfully: ID " + user.getId());
        loadUsers();
        addActionButtonsToUserTable();
    }


    private void loadUsers() {
        List<User> users = userService.getAllUsersForDisplay();
        userTableView.getItems().clear();
        userTableView.getItems().addAll(users);
        System.out.println("Loaded " + users.size() + " users.");
    }

    private void clearUserFields() {
        usernameField.clear();
        passwordField.clear();
        roleField.clear();
        addUserButton.setText("Add User");
        addUserButton.setOnAction(event -> addUser());
    }

    // Product Management Methods
    @FXML
    private void selectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Product Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        Stage stage = (Stage) selectImageButton.getScene().getWindow();
        selectedImageFile = fileChooser.showOpenDialog(stage);

        if (selectedImageFile != null) {
            selectedImageLabel.setText(selectedImageFile.getName());
        } else {
            selectedImageLabel.setText("No image selected");
        }
    }

    @FXML
    private void addProduct() {
        try {
            String name = productNameField.getText();
            String description = productDescriptionField.getText();
            double price = Double.parseDouble(productPriceField.getText());
            int quantity = Integer.parseInt(productQuantityField.getText());
            Category category = productCategoryComboBox.getValue();

            if (name.isEmpty() || description.isEmpty() || selectedImageFile == null || category == null) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "All fields must be provided to add a product.");
                return;
            }

            byte[] image = loadImageAsBytes(selectedImageFile);
            Product product = new Product(0, name, description, price, quantity, category, image);
            productService.addProduct(product);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Product added successfully: " + name);
            loadProducts();
            addProductActionButtonsToTable();
            clearProductFields();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Product price and quantity must be numbers.");
        }
    }


    @FXML
    private void updateProduct() {
        try {
            int id = Integer.parseInt(productIdField.getText());
            String name = productNameField.getText();
            String description = productDescriptionField.getText();
            double price = Double.parseDouble(productPriceField.getText());
            int quantity = Integer.parseInt(productQuantityField.getText());
            Category category = productCategoryComboBox.getValue();

            if (name.isEmpty() || description.isEmpty() || category == null) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "All fields must be provided to update a product.");
                return;
            }

            byte[] image = selectedImageFile != null ? loadImageAsBytes(selectedImageFile) : null;
            Product product = new Product(id, name, description, price, quantity, category, image);
            productService.updateProduct(product);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Product updated successfully: " + name);
            loadProducts();
            addProductActionButtonsToTable();
            clearProductFields();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Product ID, price, and quantity must be numbers.");
        }
    }

    @FXML
    private void deleteProduct() {
        try {
            int id = Integer.parseInt(productIdField.getText());
            productService.deleteProduct(id);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Product deleted successfully: ID " + id);
            loadProducts();
            addProductActionButtonsToTable();
            clearProductFields();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Product ID must be a number.");
        }
    }


    private void addProductActionButtonsToTable() {
        Callback<TableColumn<Product, Void>, TableCell<Product, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Product, Void> call(final TableColumn<Product, Void> param) {
                final TableCell<Product, Void> cell = new TableCell<>() {

                    private final Button updateButton = new Button("Update");
                    private final Button deleteButton = new Button("Delete");

                    {
                        updateButton.setOnAction(event -> {
                            Product product = getTableView().getItems().get(getIndex());
                            handleUpdateProduct(product);
                        });

                        deleteButton.setOnAction(event -> {
                            Product product = getTableView().getItems().get(getIndex());
                            handleDeleteProduct(product);
                        });

                        HBox actionButtons = new HBox(updateButton, deleteButton);
                        actionButtons.setSpacing(10);
                        setGraphic(actionButtons);
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(getGraphic());
                        }
                    }
                };
                return cell;
            }
        };
        productActionsColumn.setCellFactory(cellFactory);
    }

    private void handleUpdateProduct(Product product) {
        if (productIdField != null) {
            productIdField.setText(String.valueOf(product.getId()));
        }
        productNameField.setText(product.getName());
        productDescriptionField.setText(product.getDescription());
        productPriceField.setText(String.valueOf(product.getPrice()));
        productQuantityField.setText(String.valueOf(product.getQuantity()));
        productCategoryComboBox.setValue(product.getCategory());
        addProductButton.setText("Update Product");
        addProductButton.setOnAction(event -> updateProduct());
    }


    private void handleDeleteProduct(Product product) {
        productService.deleteProduct(product.getId());
        System.out.println("Product deleted successfully: ID " + product.getId());
        loadProducts();
    }

    private void loadProducts() {
        List<Product> products = productService.getAllProducts();
        productTableView.getItems().clear();
        productTableView.getItems().addAll(products);
        System.out.println("Loaded " + products.size() + " products.");
    }

    private void loadCategories() {
        List<Category> categories = categoryService.getAllCategories();
        productCategoryComboBox.getItems().clear();
        productCategoryComboBox.getItems().addAll(categories);
        System.out.println("Loaded " + categories.size() + " categories.");
    }

    private void clearProductFields() {
        productNameField.clear();
        productPriceField.clear();
        productDescriptionField.clear();
        productQuantityField.clear();
        productCategoryComboBox.setValue(null);
        selectedImageLabel.setText("No image selected");
        selectedImageFile = null;
        addProductButton.setText("Add Product");
        addProductButton.setOnAction(event -> addProduct());
    }

    // Order Management Methods
    @FXML
    private void addOrder() {
        try {
            int userId = Integer.parseInt(userIdOrderField.getText());
            int productId = Integer.parseInt(productIdOrderField.getText());
            int quantity = Integer.parseInt(orderQuantityField.getText());
            String status = orderStatusComboBox.getValue();

            if (status == null || status.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Order status must be provided.");
                return;
            }

            Product product = productService.getProductById(productId);
            if (product == null) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Product not found. Please provide a valid Product ID.");
                return;
            }

            double totalPrice = product.getPrice() * quantity;
            totalPriceField.setText(String.format("%.2f", totalPrice));

            Timestamp orderDate = new Timestamp(System.currentTimeMillis());

            Order order = new Order(0, userId, productId, quantity, totalPrice, orderDate, status);
            orderService.addOrder(order);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Order added successfully.");
            loadOrders();
            addOrderActionButtonsToTable();
            clearOrderFields();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "User ID, Product ID, and Quantity must be numbers.");
        }
    }


    @FXML
    private void updateOrder() {
        try {
            int id = Integer.parseInt(orderIdField.getText());
            int userId = Integer.parseInt(userIdOrderField.getText());
            int productId = Integer.parseInt(productIdOrderField.getText());
            int quantity = Integer.parseInt(orderQuantityField.getText());
            String status = orderStatusComboBox.getValue();

            if (status == null || status.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Order status must be provided.");
                return;
            }

            // Fetching order by ID
            Order order = orderService.getOrderById(id);
            if (order == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Order not found. Please provide a valid Order ID.");
                return;
            }

            // Fetching product by ID to calculate the total price
            Product product = productService.getProductById(productId);
            if (product == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Product not found. Please provide a valid Product ID.");
                return;
            }

            double totalPrice = product.getPrice() * quantity;
            totalPriceField.setText(String.format("%.2f", totalPrice));

            // Updating order details
            order.setUserId(userId);
            order.setProductId(productId);
            order.setQuantity(quantity);
            order.setTotalPrice(totalPrice);
            order.setStatus(status);

            // Calling the service to update the order
            orderService.updateOrder(order);

            // Alert for successful update
            showAlert(Alert.AlertType.INFORMATION, "Success", "Order updated successfully: ID " + id);

            // Refresh the orders list and clear fields
            loadOrders();
            addOrderActionButtonsToTable();
            clearOrderFields();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Order ID, User ID, Product ID, and Quantity must be numbers.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Update Error", "An error occurred while updating the order.");
            e.printStackTrace();  // Print stack trace for debugging purposes
        }
    }

    @FXML
    private void deleteOrder() {
        try {
            int id = Integer.parseInt(orderIdField.getText());
            orderService.deleteOrder(id);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Order deleted successfully: ID " + id);
            loadOrders();
            clearOrderFields();
            addOrderActionButtonsToTable();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Order ID must be a number.");
        }
    }


    @FXML
    private void cancelOrder() {
        try {
            int id = Integer.parseInt(orderIdField.getText());
            Order order = orderService.getOrderById(id);
            if (order == null) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Order not found.");
                return;
            }

            order.setStatus("Cancelled");
            orderService.updateOrder(order);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Order cancelled successfully: ID " + id);
            loadOrders();
            clearOrderFields();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Order ID must be a number.");
        }
    }


    private void addOrderActionButtonsToTable() {
        Callback<TableColumn<Order, Void>, TableCell<Order, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Order, Void> call(final TableColumn<Order, Void> param) {
                final TableCell<Order, Void> cell = new TableCell<>() {

                    private final Button updateButton = new Button("Update");
                    private final Button deleteButton = new Button("Delete");
                    private final Button cancelButton = new Button("Cancel");

                    {
                        updateButton.setOnAction(event -> {
                            Order order = getTableView().getItems().get(getIndex());
                            handleUpdateOrder(order);
                        });

                        deleteButton.setOnAction(event -> {
                            Order order = getTableView().getItems().get(getIndex());
                            handleDeleteOrder(order);
                        });

                        cancelButton.setOnAction(event -> {
                            Order order = getTableView().getItems().get(getIndex());
                            cancelOrderById(order.getId());
                        });

                        HBox actionButtons = new HBox(updateButton, deleteButton, cancelButton);
                        actionButtons.setSpacing(10);
                        setGraphic(actionButtons);
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(getGraphic());
                        }
                    }
                };
                return cell;
            }
        };
        orderActionsColumn.setCellFactory(cellFactory);
    }

    // Cancel order by ID method
    private void cancelOrderById(int orderId) {
        try {
            Order order = orderService.getOrderById(orderId);
            if (order == null) {
                System.out.println("Error: Order not found.");
                return;
            }

            order.setStatus("Cancelled");
            orderService.updateOrder(order);
            System.out.println("Order cancelled successfully: ID " + orderId);
            loadOrders();
            clearOrderFields();
        } catch (NumberFormatException e) {
            System.out.println("Error: Order ID must be a number.");
        }
    }

    private void handleUpdateOrder(Order order) {
        orderIdField.setText(String.valueOf(order.getId()));
        userIdOrderField.setText(String.valueOf(order.getUserId()));
        productIdOrderField.setText(String.valueOf(order.getProductId()));
        orderQuantityField.setText(String.valueOf(order.getQuantity()));
        orderStatusComboBox.setValue(order.getStatus());
        totalPriceField.setText(String.valueOf(order.getTotalPrice()));
        addOrderButton.setText("Update Order");
        addOrderButton.setOnAction(event -> updateOrder());
    }

    private void handleDeleteOrder(Order order) {
        orderService.deleteOrder(order.getId());
        System.out.println("Order deleted successfully: ID " + order.getId());
        loadOrders();
    }

    private void loadOrders() {
        List<Order> orders = orderService.getAllOrders();
        orderTableView.getItems().clear();
        orderTableView.getItems().addAll(orders);
        System.out.println("Loaded " + orders.size() + " orders.");
    }

    private void clearOrderFields() {
        orderIdField.clear();
        userIdOrderField.clear();
        productIdOrderField.clear();
        orderQuantityField.clear();
        orderStatusComboBox.setValue(null);
        totalPriceField.clear();
        addOrderButton.setText("Add Order");
        addOrderButton.setOnAction(event -> addOrder());
    }

    // Navigation Methods
    @FXML
    private void goToUserManagement() {
        userManagementScrollPane.setVisible(true);
        productManagementScrollPane.setVisible(false);
        orderManagementScrollPane.setVisible(false);
    }

    @FXML
    private void goToProductManagement() {
        userManagementScrollPane.setVisible(false);
        productManagementScrollPane.setVisible(true);
        orderManagementScrollPane.setVisible(false);
    }

    @FXML
    private void goToOrderManagement() {
        userManagementScrollPane.setVisible(false);
        productManagementScrollPane.setVisible(false);
        orderManagementScrollPane.setVisible(true);
    }

    @FXML
    private void goToShippingManagement() {
        userManagementScrollPane.setVisible(false);
        productManagementScrollPane.setVisible(false);
        orderManagementScrollPane.setVisible(false);
        shippingManagementScrollPane.setVisible(true);  // Show shipping section
    }



    // Utility method to load image as byte array
    private byte[] loadImageAsBytes(File imageFile) {
        try {
            return Files.readAllBytes(imageFile.toPath());
        } catch (IOException e) {
            System.out.println("Error loading image: " + e.getMessage());
            return null;
        }
    }
}
