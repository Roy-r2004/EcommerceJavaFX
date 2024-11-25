package org.example.ecommercejavafx.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.ecommercejavafx.models.Category;
import org.example.ecommercejavafx.models.Product;
import org.example.ecommercejavafx.services.ProductService;
import org.example.ecommercejavafx.utils.SessionManager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public class CustomerController {

    @FXML
    private ImageView userIcon;

    @FXML
    private ImageView cartIcon;

    @FXML
    private ImageView notificationIcon;

    @FXML
    private Button loginButton;

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private ComboBox<Category> categoryDropdown;

    @FXML
    private GridPane productGrid;

    @FXML
    private StackPane rootPane;
    @FXML
    private VBox mainContent;
    @FXML
    private VBox productDetailsModal;
    @FXML
    private VBox cartModal;
    @FXML
    private VBox cartItemList;
    @FXML
    private Label totalAmountLabel;
    @FXML
    private ImageView productImage;
    @FXML
    private Label productName;
    @FXML
    private Label productDescription;
    @FXML
    private Label productPrice;
    @FXML
    private TextArea reviewText;
    @FXML
    private HBox starRatingBox;
    @FXML
    private Button submitReviewButton;
    @FXML
    private Button backButton;
    @FXML
    private Button closeCartButton;

    private final ProductService productService = new ProductService();
    private final ObservableList<Category> categories = FXCollections.observableArrayList();
    private final ObservableList<Product> cartItems = FXCollections.observableArrayList();

    public void initialize() {
        // Set up icons
        userIcon.setImage(new Image("file:/C:/OOP%202%20Project/ECommerceJavaFX/src/main/resources/images/user.png"));
        cartIcon.setImage(new Image("file:/C:/OOP%202%20Project/ECommerceJavaFX/src/main/resources/images/shopping-bag.png"));
        notificationIcon.setImage(new Image("file:/C:/OOP%202%20Project/ECommerceJavaFX/src/main/resources/images/bell.png"));

        // Manage visibility based on login state
        toggleLoginState();

        initializeUserMenu();
        loadCategories();
        loadProducts(null, null);

        // Event listeners
        searchButton.setOnAction(event -> handleSearch());
        categoryDropdown.setOnAction(event -> handleSearch());
        backButton.setOnAction(event -> closeProductDetailsModal());
        submitReviewButton.setOnAction(event -> submitReview());
        cartIcon.setOnMouseClicked(event -> displayCart());
        closeCartButton.setOnAction(event -> closeCart());
    }

    private void toggleLoginState() {
        if (SessionManager.isLoggedIn()) {
            userIcon.setVisible(true);
            loginButton.setVisible(false);
        } else {
            userIcon.setVisible(false);
            loginButton.setVisible(true);
        }
    }

    private void initializeUserMenu() {
        ContextMenu userMenu = new ContextMenu();

        MenuItem myProfile = new MenuItem("My Profile");
        myProfile.setOnAction(event -> goToMyProfile());

        MenuItem logout = new MenuItem("Logout");
        logout.setOnAction(event -> handleLogout());

        userMenu.getItems().addAll(myProfile, logout);

        userIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (SessionManager.isLoggedIn()) {
                userMenu.show(userIcon, event.getScreenX(), event.getScreenY());
            }
        });
    }

    private void goToMyProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/customer/my_profile.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) userIcon.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("My Profile");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleLogout() {
        SessionManager.setLoggedIn(false);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/common/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) userIcon.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToLoginPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/common/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login Page");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCategories() {
        categories.clear();
        categories.addAll(productService.getAllCategories());
        categoryDropdown.setItems(categories);
        categoryDropdown.getItems().add(0, new Category(0, "All Categories"));
        categoryDropdown.getSelectionModel().selectFirst();
    }

    private void loadProducts(Category category, String keyword) {
        productGrid.getChildren().clear();
        List<Product> products = productService.getAllProducts();

        if (category != null && category.getId() != 0) {
            products.removeIf(product -> product.getCategory().getId() != category.getId());
        }

        if (keyword != null && !keyword.isEmpty()) {
            products.removeIf(product -> !product.getName().toLowerCase().contains(keyword.toLowerCase()));
        }

        int column = 0;
        int row = 0;

        for (Product product : products) {
            VBox productCard = createProductCard(product);
            productGrid.add(productCard, column, row);

            column++;
            if (column == 4) {
                column = 0;
                row++;
            }
        }
    }

    private VBox createProductCard(Product product) {
        VBox card = new VBox();
        card.setStyle("-fx-background-color: #ffffff; -fx-border-color: #ddd; -fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 10; -fx-spacing: 10; -fx-alignment: center;");
        card.setPrefSize(200, 250);

        Image image = (product.getImage() != null)
                ? new Image(new ByteArrayInputStream(product.getImage()))
                : new Image("file:/path/to/default_image.png");
        ImageView productImage = new ImageView(image);
        productImage.setFitHeight(120);
        productImage.setFitWidth(120);
        productImage.setPreserveRatio(true);
        productImage.setOnMouseClicked(event -> displayProductDetails(product));

        Label name = new Label(product.getName());
        name.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333; -fx-wrap-text: true;");
        name.setMaxWidth(180);
        name.setWrapText(true);

        Label price = new Label("$" + product.getPrice());
        price.setStyle("-fx-font-size: 14px; -fx-text-fill: #777;");

        Button addToCart = new Button("Add to Cart");
        addToCart.setStyle("-fx-background-color: linear-gradient(to right, #ff5722, #e64a19); -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5 10; -fx-border-radius: 5; -fx-background-radius: 5;");
        addToCart.setOnAction(event -> handleAddToCart(product));

        card.getChildren().addAll(productImage, name, price, addToCart);
        return card;
    }

    private void displayProductDetails(Product product) {
        mainContent.setVisible(false);
        productDetailsModal.setVisible(true);
        productDetailsModal.setManaged(true);

        productImage.setImage(new Image(new ByteArrayInputStream(product.getImage())));
        productName.setText(product.getName());
        productDescription.setText(product.getDescription());
        productPrice.setText("$" + product.getPrice());

        starRatingBox.getChildren().clear();
        for (int i = 0; i < 5; i++) {
            ImageView star = new ImageView(new Image("file:/path/to/star_icon.png"));
            star.setFitWidth(20);
            star.setFitHeight(20);
            final int rating = i + 1;
            star.setOnMouseClicked(event -> setRating(rating));
            starRatingBox.getChildren().add(star);
        }
    }

    private void closeProductDetailsModal() {
        productDetailsModal.setVisible(false);
        productDetailsModal.setManaged(false);
        mainContent.setVisible(true);
    }

    private void handleAddToCart(Product product) {
        if (!SessionManager.isLoggedIn()) {
            goToLoginPage();
            return;
        }
        cartItems.add(product);
        System.out.println("Added to cart: " + product.getName());
    }

    private void displayCart() {
        cartModal.setVisible(true);
        cartModal.setManaged(true);

        cartItemList.getChildren().clear();
        double totalAmount = 0;

        for (Product product : cartItems) {
            HBox cartItem = new HBox();
            cartItem.setSpacing(10);

            Label nameLabel = new Label(product.getName());
            nameLabel.setStyle("-fx-font-size: 14px;");

            Label priceLabel = new Label("$" + product.getPrice());
            priceLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #e64a19;");

            cartItem.getChildren().addAll(nameLabel, priceLabel);
            cartItemList.getChildren().add(cartItem);

            totalAmount += product.getPrice();
        }

        totalAmountLabel.setText("Total: $" + totalAmount);
    }

    private void closeCart() {
        cartModal.setVisible(false);
        cartModal.setManaged(false);
    }

    private void setRating(int rating) {
        System.out.println("Selected rating: " + rating);
    }

    private void submitReview() {
        String review = reviewText.getText();
        System.out.println("Submitted review: " + review);
        reviewText.clear();
    }

    private void handleSearch() {
        Category selectedCategory = categoryDropdown.getValue();
        String keyword = searchField.getText();
        loadProducts(selectedCategory, keyword);
    }
}
