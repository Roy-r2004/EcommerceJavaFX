package org.example.ecommercejavafx.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.ecommercejavafx.models.Category;
import org.example.ecommercejavafx.models.Product;
import org.example.ecommercejavafx.services.CategoryService;
import org.example.ecommercejavafx.services.ProductService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class ProductController {
    @FXML
    private TextField productNameField;

    @FXML
    private TextField productPriceField;

    @FXML
    private TextField productDescriptionField;

    @FXML
    private TextField productQuantityField;

    @FXML
    private TextField productIdField;

    @FXML
    private ComboBox<Category> categoryComboBox; // ComboBox for selecting a Category

    @FXML
    private Button addButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button chooseImageButton;

    @FXML
    private ListView<String> productListView;

    private final ProductService productService = new ProductService();
    private final CategoryService categoryService = new CategoryService(); // Service to get categories
    private File selectedImageFile;

    @FXML
    public void initialize() {
        addButton.setOnAction(event -> addProduct());
        updateButton.setOnAction(event -> updateProduct());
        deleteButton.setOnAction(event -> deleteProduct());
        chooseImageButton.setOnAction(event -> chooseImage());

        loadCategories(); // Load categories into the ComboBox
        loadProducts();
    }

    private void chooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Product Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        Stage stage = (Stage) chooseImageButton.getScene().getWindow();
        selectedImageFile = fileChooser.showOpenDialog(stage);

        if (selectedImageFile != null) {
            System.out.println("Selected image: " + selectedImageFile.getAbsolutePath());
        }
    }

    private void addProduct() {
        String name = productNameField.getText();
        String description = productDescriptionField.getText();
        double price = Double.parseDouble(productPriceField.getText());
        int quantity = Integer.parseInt(productQuantityField.getText());
        Category selectedCategory = categoryComboBox.getValue();

        if (selectedCategory == null) {
            System.out.println("Error: Please select a category.");
            return;
        }

        byte[] image = null;
        if (selectedImageFile != null) {
            try (FileInputStream fis = new FileInputStream(selectedImageFile)) {
                image = fis.readAllBytes();
            } catch (IOException e) {
                System.out.println("Error reading image file: " + e.getMessage());
            }
        }

        Product product = new Product(0, name, description, price, quantity, selectedCategory, image);
        productService.addProduct(product);
        loadProducts();
    }

    private void updateProduct() {
        int id = Integer.parseInt(productIdField.getText());
        String name = productNameField.getText();
        String description = productDescriptionField.getText();
        double price = Double.parseDouble(productPriceField.getText());
        int quantity = Integer.parseInt(productQuantityField.getText());
        Category selectedCategory = categoryComboBox.getValue();

        if (selectedCategory == null) {
            System.out.println("Error: Please select a category.");
            return;
        }

        byte[] image = null;
        if (selectedImageFile != null) {
            try (FileInputStream fis = new FileInputStream(selectedImageFile)) {
                image = fis.readAllBytes();
            } catch (IOException e) {
                System.out.println("Error reading image file: " + e.getMessage());
            }
        }

        Product product = new Product(id, name, description, price, quantity, selectedCategory, image);
        productService.updateProduct(product);
        loadProducts();
    }

    private void deleteProduct() {
        int id = Integer.parseInt(productIdField.getText());
        productService.deleteProduct(id);
        loadProducts();
    }

    private void loadProducts() {
        List<Product> products = productService.getAllProducts();
        productListView.getItems().clear();

        for (Product product : products) {
            // Construct a string with relevant product details
            String productInfo = String.format(
                    "Name: %s | Price: $%.2f | Quantity: %d | Category: %s",
                    product.getName() != null ? product.getName() : "N/A",
                    product.getPrice(),
                    product.getQuantity(),
                    product.getCategory() != null ? product.getCategoryName() : "Unknown"
            );
            productListView.getItems().add(productInfo);
        }
    }


    // Load all available categories into the ComboBox
    private void loadCategories() {
        List<Category> categories = categoryService.getAllCategories();
        categoryComboBox.getItems().clear();
        categoryComboBox.getItems().addAll(categories);
    }
}
