package org.example.ecommercejavafx.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.example.ecommercejavafx.models.*;
import org.example.ecommercejavafx.services.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private TextField productSearchField;



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
    private ComboBox<Discount> discountComboBox;

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
    private TableColumn<Shipping, Integer> orderIdShippingColumn;

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

    // Discount Management Fields
    @FXML
    private  TextField discountIdField;
    @FXML
    private TextField discountCodeField;
    @FXML
    private ComboBox<String> discountTypeComboBox;
    @FXML
    private TextField discountValueField;
    @FXML
    private DatePicker startDateField;
    @FXML
    private DatePicker endDateField;
    @FXML
    private TextField usageLimitField;
    @FXML
    private Button addDiscountButton;
    @FXML
    private Button updateDiscountButton;

    @FXML
    private Button deleteDiscountButton;
    @FXML
    private TableView<Discount> discountTableView;
    @FXML
    private TableColumn<Discount, Integer> discountIdColumn;
    @FXML
    private TableColumn<Discount, String> discountCodeColumn;
    @FXML
    private TableColumn<Discount, String> discountTypeColumn;
    @FXML
    private TableColumn<Discount, Double> discountValueColumn;
    @FXML
    private TableColumn<Discount, LocalDateTime> startDateColumn;
    @FXML
    private TableColumn<Discount, LocalDateTime> endDateColumn;
    @FXML
    private TableColumn<Discount, Integer> usageLimitColumn;
    @FXML
    private TableColumn<Discount, Void> discountActionsColumn;
    @FXML
    private ScrollPane discountManagementScrollPane;







    // Services
    private final UserService userService = new UserService();
    private final ProductService productService = new ProductService();
    private final CategoryService categoryService = new CategoryService();
    private final OrderService orderService = new OrderService();

    private final ShippingService shippingService = new ShippingService();
    private final DiscountService discountService = new DiscountService();



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
        // Add listener to the product search field
        productSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterProductList(newValue);
        });
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
        orderIdShippingColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        shippingProviderColumn.setCellValueFactory(new PropertyValueFactory<>("shippingProvider"));
        trackingNumberColumn.setCellValueFactory(new PropertyValueFactory<>("trackingNumber"));
        // Adjusting the format for Estimated Delivery Column
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        estimatedDeliveryColumn.setCellValueFactory(new PropertyValueFactory<>("estimatedDelivery"));
        estimatedDeliveryColumn.setCellFactory(column -> {
            return new TextFieldTableCell<>(new StringConverter<LocalDateTime>() {
                @Override
                public String toString(LocalDateTime date) {
                    return (date != null) ? date.format(formatter) : "";
                }

                @Override
                public LocalDateTime fromString(String string) {
                    return LocalDateTime.parse(string, formatter);
                }
            });
        });

        shippingStatusColumn.setCellValueFactory(new PropertyValueFactory<>("shippingStatus"));
        shippingStatusComboBox.getItems().addAll("In Transit", "Delivered", "Pending", "Cancelled");
        addShippingButton.setOnAction(event -> addShipping());
        deleteShippingButton.setOnAction(event -> deleteShipping());
        loadShipping();
        addShippingActionButtonsToTable();


        // Discount Management Initialization
        addDiscountButton.setOnAction(event -> addDiscount());
        deleteDiscountButton.setOnAction(event -> deleteDiscount());
        discountTypeComboBox.getItems().clear();
        discountTypeComboBox.getItems().addAll("percentage", "fixed");

        discountIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        discountCodeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        discountTypeColumn.setCellValueFactory(new PropertyValueFactory<>("discountType"));
        discountValueColumn.setCellValueFactory(new PropertyValueFactory<>("discountValue"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        usageLimitColumn.setCellValueFactory(new PropertyValueFactory<>("usageLimit"));

        // Define the formatter for the date
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        // Set custom cell factory for Start Date column
        startDateColumn.setCellFactory(new Callback<TableColumn<Discount, LocalDateTime>, TableCell<Discount, LocalDateTime>>() {
            @Override
            public TableCell<Discount, LocalDateTime> call(TableColumn<Discount, LocalDateTime> param) {
                return new TableCell<Discount, LocalDateTime>() {
                    @Override
                    protected void updateItem(LocalDateTime item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.format(dateFormatter));
                        }
                    }
                };
            }
        });

// Set custom cell factory for End Date column
        endDateColumn.setCellFactory(new Callback<TableColumn<Discount, LocalDateTime>, TableCell<Discount, LocalDateTime>>() {
            @Override
            public TableCell<Discount, LocalDateTime> call(TableColumn<Discount, LocalDateTime> param) {
                return new TableCell<Discount, LocalDateTime>() {
                    @Override
                    protected void updateItem(LocalDateTime item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.format(dateFormatter));
                        }
                    }
                };
            }
        });
        addDiscountActionButtonsToTable();
        loadDiscounts();




    }

    // Discounts methods



    @FXML
    private void addDiscount() {
        try {
            String code = discountCodeField.getText();
            String type = discountTypeComboBox.getValue();
            double value = Double.parseDouble(discountValueField.getText());
            LocalDateTime startDate = startDateField.getValue().atStartOfDay();
            LocalDateTime endDate = endDateField.getValue().atStartOfDay();
            Integer usageLimit = usageLimitField.getText().isEmpty() ? null : Integer.parseInt(usageLimitField.getText());

            Discount discount = new Discount(0, code, type, value, startDate, endDate, usageLimit);
            discountService.addDiscount(discount);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Discount added successfully.");
            loadDiscounts();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Discount value and usage limit must be valid numbers.");
        }
    }




    @FXML
    private void deleteDiscount() {
        Discount selectedDiscount = discountTableView.getSelectionModel().getSelectedItem();
        if (selectedDiscount != null) {
            discountService.deleteDiscount(selectedDiscount.getId());
            showAlert(Alert.AlertType.INFORMATION, "Success", "Discount deleted successfully.");
            loadDiscounts();
            addShippingActionButtonsToTable();
        } else {
            showAlert(Alert.AlertType.ERROR, "No Selection", "Please select a discount to delete.");
        }
    }

    private void addDiscountActionButtonsToTable() {
        // Clear existing action column to avoid duplicate entries
        discountTableView.getColumns().remove(discountActionsColumn);

        // Set up the action column
        discountActionsColumn = new TableColumn<>("Actions");

        Callback<TableColumn<Discount, Void>, TableCell<Discount, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Discount, Void> call(final TableColumn<Discount, Void> param) {
                final TableCell<Discount, Void> cell = new TableCell<>() {
                    private final Button updateButton = new Button("Update");
                    private final Button deleteButton = new Button("Delete");

                    {
                        updateButton.setOnAction(event -> {
                            Discount discount = getTableView().getItems().get(getIndex());
                            handleUpdateDiscount(discount);
                        });

                        deleteButton.setOnAction(event -> {
                            Discount discount = getTableView().getItems().get(getIndex());
                            discountService.deleteDiscountById(discount.getId());
                            loadDiscounts(); // Refresh the discount table
                            addDiscountActionButtonsToTable(); // Re-add action buttons to the refreshed table
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
        discountActionsColumn.setCellFactory(cellFactory);
        discountTableView.getColumns().add(discountActionsColumn);
    }



    private void deleteDiscount(int discountId) {
        // Confirmation dialog to avoid accidental deletions
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Discount");
        alert.setHeaderText("Are you sure you want to delete this discount?");
        alert.setContentText("This action cannot be undone.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                discountService.deleteDiscountById(discountId);
                loadDiscounts();  // Refresh the discount table after deletion
            }
        });
    }


    private void handleUpdateDiscount(Discount discount) {
        discountCodeField.setText(discount.getCode());
        discountTypeComboBox.setValue(discount.getDiscountType());
        discountValueField.setText(String.valueOf(discount.getDiscountValue()));
        startDateField.setValue(discount.getStartDate().toLocalDate());
        endDateField.setValue(discount.getEndDate().toLocalDate());
        usageLimitField.setText(discount.getUsageLimit() != null ? String.valueOf(discount.getUsageLimit()) : "");

        addDiscountButton.setText("Update Discount");
        addDiscountButton.setOnAction(event -> {
            discount.setCode(discountCodeField.getText());
            discount.setDiscountType(discountTypeComboBox.getValue());
            discount.setDiscountValue(Double.parseDouble(discountValueField.getText()));
            discount.setStartDate(startDateField.getValue().atStartOfDay());
            discount.setEndDate(endDateField.getValue().atStartOfDay());
            discount.setUsageLimit(usageLimitField.getText().isEmpty() ? null : Integer.parseInt(usageLimitField.getText()));

            discountService.updateDiscount(discount);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Discount updated successfully.");
            loadDiscounts();
            addDiscountActionButtonsToTable();
            clearDiscountFields();
            addDiscountButton.setText("Add Discount");
            addDiscountButton.setOnAction(e -> addDiscount());
        });
    }

    private void clearDiscountFields() {

        discountCodeField.clear();
        discountTypeComboBox.setValue(null);
        discountValueField.clear();
        startDateField.setValue(null);
        endDateField.setValue(null);
        usageLimitField.clear();
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
        addProductActionButtonsToTable();
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
    private void filterProductList(String filter) {
        ObservableList<Product> filteredList = FXCollections.observableArrayList();

        for (Product product : productService.getAllProducts()) {
            if (product.getName().toLowerCase().contains(filter.toLowerCase()) ||
                    String.valueOf(product.getId()).contains(filter) ||
                    String.valueOf(product.getPrice()).contains(filter) ||
                    String.valueOf(product.getQuantity()).contains(filter)) {
                filteredList.add(product);
            }
        }

        productTableView.setItems(filteredList);

        // Re-apply the cell factory for the "Actions" column after filtering
        addProductActionButtonsToTable();
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
        addProductActionButtonsToTable();
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

    @FXML
    private void loadDiscounts() {
        // Fetch all discounts from the service
        List<Discount> discounts = discountService.getAllDiscounts();

        // Clear the discountComboBox and discountTableView items before adding new ones
        discountComboBox.getItems().clear();
        discountTableView.getItems().clear();

        // Populate ComboBox with the list of discounts
        discountComboBox.getItems().addAll(discounts);

        // Set a custom cell factory to display only the discount code in the ComboBox
        discountComboBox.setCellFactory(lv -> new ListCell<Discount>() {
            @Override
            protected void updateItem(Discount discount, boolean empty) {
                super.updateItem(discount, empty);
                if (empty || discount == null) {
                    setText(null);
                } else {
                    setText(discount.getCode());  // Display only the discount code
                }
            }
        });

        // Set a custom button cell to display the selected discount's code in the ComboBox
        discountComboBox.setButtonCell(new ListCell<Discount>() {
            @Override
            protected void updateItem(Discount discount, boolean empty) {
                super.updateItem(discount, empty);
                if (empty || discount == null) {
                    setText("Select Discount");
                } else {
                    setText(discount.getCode());  // Display only the discount code
                }
            }
        });

        // Add all discounts to the discountTableView
        discountTableView.getItems().addAll(discounts);

        System.out.println("Loaded " + discounts.size() + " discounts from database.");
    }


    // Order Management Methods
    @FXML
    private void addOrder() {
        try {
            int userId = Integer.parseInt(userIdOrderField.getText());
            int productId = Integer.parseInt(productIdOrderField.getText());
            int quantity = Integer.parseInt(orderQuantityField.getText());
            String status = orderStatusComboBox.getValue();
            Discount discount = discountComboBox.getValue();

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
            if (discount != null && discount.isActive()) {
                if ("percentage".equals(discount.getDiscountType())) {
                    totalPrice -= totalPrice * (discount.getDiscountValue() / 100);
                } else if ("fixed".equals(discount.getDiscountType())) {
                    totalPrice -= discount.getDiscountValue();
                }
            }

            totalPriceField.setText(String.format("%.2f", totalPrice));
            Timestamp orderDate = new Timestamp(System.currentTimeMillis());

            Order order = new Order(0, userId, productId, quantity, totalPrice, orderDate, status, discount != null ? discount.getId() : null);
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
            Discount discount = discountComboBox.getValue();

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
            if (discount != null && discount.isActive()) {
                if ("percentage".equals(discount.getDiscountType())) {
                    totalPrice -= totalPrice * (discount.getDiscountValue() / 100);
                } else if ("fixed".equals(discount.getDiscountType())) {
                    totalPrice -= discount.getDiscountValue();
                }
            }

            totalPriceField.setText(String.format("%.2f", totalPrice));

            // Updating order details
            order.setUserId(userId);
            order.setProductId(productId);
            order.setQuantity(quantity);
            order.setTotalPrice(totalPrice);
            order.setStatus(status);
            order.setDiscountId(discount != null ? discount.getId() : null); // Set discountId in order

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
        addOrderActionButtonsToTable();
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
        addOrderActionButtonsToTable();
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
        discountManagementScrollPane.setVisible(false);
    }

    @FXML
    private void goToProductManagement() {
        userManagementScrollPane.setVisible(false);
        productManagementScrollPane.setVisible(true);
        orderManagementScrollPane.setVisible(false);
        discountManagementScrollPane.setVisible(false);
    }

    @FXML
    private void goToOrderManagement() {
        userManagementScrollPane.setVisible(false);
        productManagementScrollPane.setVisible(false);
        orderManagementScrollPane.setVisible(true);
        discountManagementScrollPane.setVisible(false);
    }

    @FXML
    private void goToShippingManagement() {
        userManagementScrollPane.setVisible(false);
        productManagementScrollPane.setVisible(false);
        orderManagementScrollPane.setVisible(false);
        shippingManagementScrollPane.setVisible(true);
        discountManagementScrollPane.setVisible(false);
    }

    @FXML
    private void goToDiscountManagement() {
        // Set all other management sections to not be visible
        userManagementScrollPane.setVisible(false);
        productManagementScrollPane.setVisible(false);
        orderManagementScrollPane.setVisible(false);
        shippingManagementScrollPane.setVisible(false);
        discountManagementScrollPane.setVisible(true);
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
