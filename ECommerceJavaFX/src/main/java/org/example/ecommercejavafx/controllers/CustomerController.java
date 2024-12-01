package org.example.ecommercejavafx.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
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
import org.example.ecommercejavafx.models.Order;
import org.example.ecommercejavafx.models.Product;
import org.example.ecommercejavafx.models.Review;
import org.example.ecommercejavafx.services.OrderService;
import org.example.ecommercejavafx.models.Review;
import org.example.ecommercejavafx.services.ProductService;
import org.example.ecommercejavafx.services.ReviewService;
import org.example.ecommercejavafx.utils.SessionManager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
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
    @FXML
    private TableView<Review> reviewTableView;
    @FXML
    private TableColumn<Review, String> userColumn;
    @FXML
    private TableColumn<Review, Integer> ratingColumn;
    @FXML
    private TableColumn<Review, String> reviewColumn;


    private final ProductService productService = new ProductService();
    private final ObservableList<Category> categories = FXCollections.observableArrayList();
    private final ObservableList<Product> cartItems = FXCollections.observableArrayList();

    // For review
    private Product product1 = new Product();
    private int rating = 0; // Store user rating
    private final ReviewService reviewsService = new ReviewService();

    public void initialize() {
        // Set up icons
        userIcon.setImage(new Image("file:/C:/Users/rami_/IdeaProjects/EcommerceJavaFX/ECommerceJavaFX/src/main/resources/images/user.png"));
        cartIcon.setImage(new Image("file:/C:/Users/rami_/IdeaProjects/EcommerceJavaFX/ECommerceJavaFX/src/main/resources/images/shopping-bag.png"));
        notificationIcon.setImage(new Image("file:/C:/Users/rami_/IdeaProjects/EcommerceJavaFX/ECommerceJavaFX/src/main/resources/images/bell.png"));

        // Manage visibility based on login state
        toggleLoginState();

        initializeUserMenu();
        loadCategories();
        loadProducts(null, null);

        // Configure review table columns
        userColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getUserId() + ""));
        ratingColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getRating()));
        reviewColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getReviewText()));

        // Event listeners
        searchButton.setOnAction(event -> handleSearch());
        categoryDropdown.setOnAction(event -> handleSearch());
        backButton.setOnAction(event -> closeProductDetailsModal());
        submitReviewButton.setOnAction(event -> submitReview());
        cartIcon.setOnMouseClicked(event -> displayCart());
        closeCartButton.setOnAction(event -> closeCart());

        // Attach click event to each star for reviews
        for (int i = 1; i <= 5; i++) {
            Label star = (Label) starRatingBox.lookup("#star" + i);
            star.setOnMouseClicked(this::handleStarClick); // Bind click event to handleStarClick
        }
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
                : new Image("file:/C:/Users/rami_/IdeaProjects/EcommerceJavaFX/ECommerceJavaFX/src/main/resources/images/default_image.png");
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

        product1 = productService.getProductById(product.getId());
        loadProductReviews(product1.getId());
    }

    private void loadProductReviews(int productId) {
        List<Review> reviews = reviewsService.getReviewsByProductId(productId);
        reviewTableView.setItems(FXCollections.observableArrayList(reviews));
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

    private void submitReview() {
        if (!SessionManager.isLoggedIn()) {
            goToLoginPage();
            return;
        }

        String review = reviewText.getText();
        if (rating == 0 || review.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "No input found", "Rating and review must not be empty!");
            return;
        }

        Review newReview = new Review(
                product1.getId(),
                SessionManager.getCurrentUser().getId(),
                rating,
                reviewText.getText(),
                LocalDate.now()
        );

        try {
            reviewsService.addReview(newReview);
            System.out.println("Review submitted successfully!");
            clearReviewForm();
            loadProductReviews(product1.getId());
        } catch (Exception e) {
            System.err.println("Failed to submit review: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Form submission", "Could not submit your review!");
        }
    }

    private void handleSearch() {
        Category selectedCategory = categoryDropdown.getValue();
        String keyword = searchField.getText();
        loadProducts(selectedCategory, keyword);
    }

    private void handleStarClick(MouseEvent event) {
        Label clickedStar = (Label) event.getSource();
        rating = Integer.parseInt(clickedStar.getId().replace("star", ""));

        for (int i = 1; i <= 5; i++) {
            Label star = (Label) starRatingBox.lookup("#star" + i);
            if (i <= rating) {
                star.setStyle("-fx-font-size: 24px; -fx-text-fill: #ffcc00;");
            } else {
                star.setStyle("-fx-font-size: 24px; -fx-text-fill: #E0E0E0;");
            }
        }
    }

    private void clearReviewForm() {
        reviewText.clear();
        rating = 0;
        for (int i = 1; i <= 5; i++) {
            Label star = (Label) starRatingBox.lookup("#star" + i);
            star.setStyle("-fx-font-size: 24px; -fx-text-fill: #E0E0E0;");
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
