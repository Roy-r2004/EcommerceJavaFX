package org.example.ecommercejavafx.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.ecommercejavafx.models.*;
import org.example.ecommercejavafx.services.*;
import org.example.ecommercejavafx.models.Review;
import org.example.ecommercejavafx.utils.SessionManager;

import java.awt.event.ActionEvent;
import java.io.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class CustomerController {

    public ImageView logo;
    @FXML
    private ImageView userIcon;
    @FXML
    private ImageView cartIcon;
    @FXML
    private ImageView orderIcon;
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
    @FXML
    private TextField discountCodeField; //added for discount
    @FXML
    private Button applyDiscount;        //added for discount
    @FXML
    public MenuButton displayfavorites;
    @FXML
    public Label newTotal;
    @FXML
    public Label discountedAmount;
    @FXML
    public Label discountedAmountValue;
    @FXML
    private VBox creditCardForm;
    @FXML
    private Button checkoutButton;
    @FXML
    private Button confirmOrderButton;
    @FXML
    public ScrollPane checkOutScrollpane;
    @FXML
    public ScrollPane cartScrollPane;
    @FXML
    public Button closeCheckoutButton;
    @FXML
    public ScrollPane oldOrderScrollPane;
    @FXML
    public TableView<Order> ordersTable;
    @FXML
    private TableColumn<Order, Integer> orderIdColumn;
    @FXML
    private TableColumn<Order, Time> orderDateColumn;
    @FXML
    private TableColumn<Order, String> productsColumn;
    @FXML
    private TableColumn<Order, Double> totalPriceColumn;
    @FXML
    private TableColumn<Order, String> statusColumn;
    @FXML
    public Button closeOldOrdersButton;
    @FXML
    public ScrollPane pendingOrderScrollPane;
    @FXML
    public TableView<Order> pendingOrdersTable;
    @FXML
    public TableColumn<Order, Integer> pendingOrderIdColumn;
    @FXML
    public TableColumn<Order, Time> pendingorderDateColumn;
    @FXML
    public TableColumn<Order, String> pendingProductsColumn;
    @FXML
    public TableColumn<Order, Double> pendingTotalPriceColumn;
    @FXML
    public TableColumn<Order, String> pendingStatusColumn;
    @FXML
    public Button closePendingOrdersButton;
    @FXML
    public TableColumn<Order, Void> orderActionsColumn;
    @FXML
    public VBox modifyOrderVbox;
    @FXML
    public Label updatedPrice;
    @FXML
    public Button confirmUpdateButton;
    @FXML
    public Button cancelUpdateButton;
    @FXML
    public VBox itemListForUpdate;
    @FXML
    public Label orderIdForUpdate;

    @FXML
    private TableColumn<Order, Double> discountedPriceColumn;
    private final ProductService productService = new ProductService();
    private final ObservableList<Category> categories = FXCollections.observableArrayList();
    private final ObservableList<Product> cartItems = FXCollections.observableArrayList();
    private final ObservableList<Product> forUpdateitems = FXCollections.observableArrayList();

    // For review
    private Product product1 = new Product();
    private int rating = 0; // Store user rating
    private final ReviewService reviewsService = new ReviewService();

    //for customer's discount
    private final DiscountService discountService = new DiscountService();
    private Discount discount = new Discount();

    //for favorites in cart
    private List<Product> favorites = new ArrayList<>();

    //for adding customer's order
    private final OrderService orderService = new OrderService();


    public void initialize() {
        // Set up icons
        userIcon.setImage(new Image("file:/C:/Users/rami_/IdeaProjects/EcommerceJavaFX/ECommerceJavaFX/src/main/resources/images/user.png"));
        cartIcon.setImage(new Image("file:/C:/Users/rami_/IdeaProjects/EcommerceJavaFX/ECommerceJavaFX/src/main/resources/images/shopping-bag.png"));
        orderIcon.setImage(new Image("file:/C:/Users/rami_/IdeaProjects/EcommerceJavaFX/ECommerceJavaFX/src/main/resources/images/icons8-order-64.png"));
        logo.setImage(new Image("file:/C:\\Users\\rami_\\IdeaProjects\\EcommerceJavaFX\\ECommerceJavaFX\\src\\main\\resources\\images\\Logo-Ecommecrece-oop2.png"));
        // Manage visibility based on login state
        toggleLoginState();
        initializeUserMenu();
        loadCategories();
        loadProducts(null, null);
        initializeOrderMenu();
        loadOrders();

        // Configure review table columns
        userColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getUserId() + ""));
        ratingColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getRating()));
        reviewColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getReviewText()));

        // Event listeners
        searchButton.setOnAction(event -> handleSearch());
        categoryDropdown.setOnAction(event -> handleSearch());
        backButton.setOnAction(event -> closeProductDetailsModal());
        submitReviewButton.setOnAction(event -> submitReview());
        cartIcon.setOnMouseClicked(event -> {
            cartScrollPane.setVisible(true);
            cartScrollPane.setManaged(true);
            displayCart();
        });
        closeCartButton.setOnAction(event -> closeCart());
        applyDiscount.setOnAction(event -> applyDiscount());
        displayfavorites.setOnAction(event -> updateFavoritesMenu());
        checkoutButton.setOnAction(e -> {
            creditCardForm.setVisible(true);
            creditCardForm.setManaged(true);
            checkOutScrollpane.setVisible(true);
            checkOutScrollpane.setManaged(true);
        });
        confirmOrderButton.setOnAction(event -> {
            reportOffering();
            addOrder();
        });
        closeCheckoutButton.setOnAction(e -> {
            cartScrollPane.setVisible(false);
            cartScrollPane.setManaged(false);
            checkOutScrollpane.setVisible(false);
            checkOutScrollpane.setManaged(false);
        });
        closeOldOrdersButton.setOnAction(event -> handleCloseOldOrders());
        closePendingOrdersButton.setOnAction(e -> handlePendingCloseOldOrders());
        cancelUpdateButton.setOnAction(event -> {
            cancelUpdateOrder();
        });
        confirmUpdateButton.setOnAction(event -> confirmOrderUpdate());

        // Attach click event to each star for reviews
        for (int i = 1; i <= 5; i++) {
            Label star = (Label) starRatingBox.lookup("#star" + i);
            star.setOnMouseClicked(this::handleStarClick); // Bind click event to handleStarClick
        }

        //setting up the order table columns for customer
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("OrderDate"));
        productsColumn.setCellValueFactory(new PropertyValueFactory<>("Products"));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("TotalPrice"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("Status"));

        pendingOrderIdColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        pendingorderDateColumn.setCellValueFactory(new PropertyValueFactory<>("OrderDate"));
        pendingProductsColumn.setCellValueFactory(new PropertyValueFactory<>("Products"));
        pendingTotalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("TotalPrice"));
        pendingStatusColumn.setCellValueFactory(new PropertyValueFactory<>("Status"));

        // Adding Action Buttons to Order Table
        addOrderActionButtonsToTable();
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

        // Check if the product already exists in the cart
        boolean productExists = false;
        for (Product cartProduct : cartItems) {
            if (cartProduct.getId() == product.getId()) {
                // If the product is found, increment its quantity
                cartProduct.setQuantity(cartProduct.getQuantity() + 1);
                productExists = true;
                break;
            }
        }

        // If product doesn't exist, add it to the cart with quantity 1
        if (!productExists) {
            product.setQuantity(1); // Set initial quantity as 1
            cartItems.add(product);
        }

        System.out.println("Added to cart: " + product.getName());
        displayCart(); // Update cart display after adding the product
    }

    private void handleRemoveFromCart(Product product) {
        if (!SessionManager.isLoggedIn()) {
            goToLoginPage();
            return;
        }

        // Find the product in the cart and reduce its quantity or remove it if quantity is 1
        for (Product cartProduct : cartItems) {
            if (cartProduct.getId() == product.getId()) {
                if (cartProduct.getQuantity() > 1) {
                    // Decrease quantity if more than 1
                    cartProduct.setQuantity(cartProduct.getQuantity() - 1);
                } else {
                    // Remove the product if its quantity is 1
                    cartItems.remove(cartProduct);
                }
                break;
            }
        }

        System.out.println("Removed from cart: " + product.getName());
        displayCart(); // Update cart display after removing the product
    }

    private void displayCart() {
        cartModal.setVisible(true);
        cartModal.setManaged(true);

        cartItemList.getChildren().clear();
        double totalAmount = 0;

        // List to keep track of already processed products
        List<Product> processedProducts = new ArrayList<>();

        for (Product product : cartItems) {
            // Skip this product if it's already been processed
            if (processedProducts.contains(product)) {
                continue;
            }

            // Calculate the total quantity for this product
            int quantity = 0;
            for (Product cartProduct : cartItems) {
                if (cartProduct.equals(product)) {
                    quantity += cartProduct.getQuantity(); // Sum quantities for this product
                }
            }

            // Create UI components for the product
            HBox cartItem = new HBox();
            cartItem.setSpacing(10);
            cartItem.setAlignment(Pos.CENTER_LEFT);

            Label nameLabel = new Label(product.getName());
            nameLabel.setStyle("-fx-font-size: 14px;");

            Label priceLabel = new Label("$" + product.getPrice());
            priceLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #e64a19;");

            Label quantityLabel = new Label("x" + quantity);
            quantityLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f7d7d;");

            Button favorites = new Button("add to favorites");
            favorites.setStyle("-fx-background-color: linear-gradient(to right, #03a9f4, #0288d1); -fx-text-fill: white; -fx-font-size: 10px; -fx-padding: 2px 5px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
            favorites.setOnAction(event -> addToFavorites(product));

            Button remove = new Button("remove");
            remove.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 10px; -fx-padding: 2px 5px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
            remove.setOnAction(event -> handleRemoveFromCart(product));

            // Add elements to cartItem HBox
            cartItem.getChildren().addAll(nameLabel, priceLabel, quantityLabel, favorites, remove);

            // Add cartItem to cartItemList
            cartItemList.getChildren().add(cartItem);

            // Calculate total amount
            totalAmount += product.getPrice() * quantity;

            // Mark the product as processed to avoid adding it again
            processedProducts.add(product);
        }

        // Update total amount labels
        totalAmountLabel.setText("$" + String.format("%.2f", totalAmount));
        newTotal.setText("$" + String.format("%.2f", totalAmount));
        discountedAmountValue.setText("$0");
    }


    private void closeCart() {
        cartScrollPane.setVisible(false);
        cartScrollPane.setManaged(false);
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

    //customer's discount part
    private void applyDiscount() {
        String code = discountCodeField.getText();
        String text = totalAmountLabel.getText();
        text = text.replace("$", "").trim();
        double oldTotalAmount = Double.parseDouble(text);
        discount = discountService.getDiscountByCode(code);

        if (discount == null) {
            showAlert(Alert.AlertType.ERROR, "Discount Code", "Invalid Discount Code!");
            return;
        }

        LocalDateTime currentTime = LocalDateTime.now();
        double discountedAmount = 0;
        double newTotalAmount;

        if (currentTime.isAfter(discount.getStartDate()) && currentTime.isBefore(discount.getEndDate())) {
            // Find the product group with the highest total price
            double maxProductTotal = 0;

            for (Product product : cartItems) {
                double productTotal;
                int quantity = 0;

                // Calculate total for this product
                for (Product p : cartItems) {
                    if (p.equals(product)) {
                        quantity++;
                    }
                }
                productTotal = product.getPrice() * quantity;

                // Check if this is the highest total
                if (productTotal > maxProductTotal) {
                    maxProductTotal = productTotal;
                }
            }

            // Apply discount to the highest total price
            String discountType = discount.getDiscountType();
            if ("percentage".equals(discountType)) {
                discountedAmount = (maxProductTotal * discount.getDiscountValue()) / 100;
            } else if ("fixed".equals(discountType)) {
                discountedAmount = discount.getDiscountValue();
            }

            discountedAmount = Math.min(discountedAmount, maxProductTotal); // Ensure discount doesn't exceed item total
            newTotalAmount = oldTotalAmount - discountedAmount;

            // Update UI
            newTotal.setText("$" + String.format("%.2f", newTotalAmount));
            discountedAmountValue.setText("$" + String.format("%.2f", discountedAmount));

            System.out.println("Discount has been applied!");
            showAlert(Alert.AlertType.INFORMATION, "Discount Applied", "The price has been updated with your discount!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Discount Code", "Sorry, the discount code you entered has expired.");
        }
    }

    private void addToFavorites(Product product) {
        favorites.add(product);
        updateFavoritesMenu();
        System.out.println("item " + product.getName() + " has been added to favorites");
    }

    private void updateFavoritesMenu() {
        // Clear existing items in the MenuButton
        displayfavorites.getItems().clear();

        // Check if there are favorites
        if (favorites.isEmpty()) {
            MenuItem emptyItem = new MenuItem("No favorites added.");
            displayfavorites.getItems().add(emptyItem);
        } else {
            for (Product product : favorites) {
                HBox favoriteItem = new HBox();
                favoriteItem.setSpacing(10);

                Label nameLabel = new Label(product.getName());
                nameLabel.setStyle("-fx-font-size: 14px;");

                Label priceLabel = new Label("$" + product.getPrice());
                priceLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #e64a19;");

                Button remove = new Button("remove");
                remove.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 10px; -fx-padding: 2px 5px; -fx-border-radius: 5px; -fx-background-radius: 5px; ");
                remove.setOnAction(event -> handleRemoveFromFavorites(product));

                favoriteItem.getChildren().addAll(nameLabel, priceLabel, remove);

                CustomMenuItem menuItem = new CustomMenuItem(favoriteItem);
                displayfavorites.getItems().add(menuItem);
            }
        }

        // Update the MenuButton's text to show the count of favorites
        displayfavorites.setText("(" + favorites.size() + ")");
    }


    private void handleRemoveFromFavorites(Product product) {
        favorites.remove(product);
        updateFavoritesMenu();
        System.out.println("item " + product.getName() + " has been removed from favorites");
    }

    // for checkout in cart
    private void addOrder() {
        int userId = SessionManager.getCurrentUser().getId();
        String discountCode = discountCodeField.getText().trim();
        Discount discount = discountService.getDiscountByCode(discountCode);

        String string1 = discountedAmountValue.getText().replace("$", "").trim();
        double discountedTotal = Double.parseDouble(string1);

        Product highestTotalProduct = null;
        double highestTotalPrice = 0;


        for (Product product : cartItems) {
            int quantity = extractQuantityFromLabel(product);
            double productTotal = product.getPrice() * quantity;

            if (productTotal > highestTotalPrice) {
                highestTotalPrice = productTotal;
                highestTotalProduct = product;
            }
        }

        // Add each product as an order
        for (Product product : cartItems) {
            int quantity = extractQuantityFromLabel(product);
            double productTotal = product.getPrice() * quantity;


            Order order = new Order();
            order.setUserId(userId);
            order.setProductId(product.getId());
            order.setQuantity(quantity);
            order.setTotalPrice(productTotal);
            order.setStatus("Pending");


            if (product.equals(highestTotalProduct) && discount != null) {
                order.setDiscountId(discount.getId());
                order.setDiscountedPrice(productTotal - discountedTotal);
            } else {
                order.setDiscountId(null);
                order.setDiscountedPrice(productTotal);
            }

            orderService.addOrder(order);
        }
        // Clear the cart and close the cart modal
        cartItems.clear();
        closeCart();
        cartScrollPane.setVisible(false);
        cartScrollPane.setManaged(false);
        checkOutScrollpane.setVisible(false);
        checkOutScrollpane.setManaged(false);

        // Show success alert
        showAlert(Alert.AlertType.INFORMATION, "Success", "Order Placed Successfully");
    }


    private int extractQuantityFromLabel(Product product) {

        for (int i = 0; i < cartItemList.getChildren().size(); i++) {
            HBox cartItemHBox = (HBox) cartItemList.getChildren().get(i);


            Label nameLabel = (Label) cartItemHBox.getChildren().get(0);
            if (nameLabel.getText().equalsIgnoreCase(product.getName())) {
                Label quantityLabel = (Label) cartItemHBox.getChildren().get(2);
                String quantityText = quantityLabel.getText().trim();

                // removing the X
                if (quantityText.startsWith("x")) {
                    try {
                        return Integer.parseInt(quantityText.substring(1).trim());
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                }
            }
        }
        return 0;
    }

    // cart report
    private String generateReport() {
        StringBuilder reportContent = new StringBuilder();

        reportContent.append("Shopping Cart Report\n");
        reportContent.append("=====================\n");

        List<Product> processedProducts = new ArrayList<>();

        for (Product product : cartItems) {
            if (processedProducts.contains(product)) {
                continue;
            }

            // Calculate the total quantity
            int quantity = 0;
            for (Product cartProduct : cartItems) {
                if (cartProduct.equals(product)) {
                    quantity += cartProduct.getQuantity();
                }
            }

            // Append product details
            reportContent.append("Product: ").append(product.getName()).append("\n");
            reportContent.append("Price: $").append(product.getPrice()).append("\n");
            reportContent.append("Quantity: ").append(quantity).append("\n");
            reportContent.append("Subtotal: $").append(String.format("%.2f", product.getPrice() * quantity)).append("\n");
            reportContent.append("-----------------------\n");

            processedProducts.add(product);
        }
        String totalAmount = newTotal.getText();
        reportContent.append("Total Amount: ").append(totalAmount).append("\n");

        return reportContent.toString();
    }

    private void saveReportToFile(String reportContent) {

        String fileName = "ShoppingCartReport.txt";
        String reportsDirectoryPath = "src/reports";

        // Create the reports directory if it doesn't exist
        File reportsDirectory = new File(reportsDirectoryPath);
        if (!reportsDirectory.exists()) {
            reportsDirectory.mkdirs();
        }

        File reportFile = new File(reportsDirectory, fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(reportFile))) {
            writer.write(reportContent);
            System.out.println("Report saved to " + reportFile.getAbsolutePath());  // Print the full path
        } catch (IOException e) {
            System.out.println("An error occurred while saving the report: " + e.getMessage());
        }
    }


    private void reportOffering() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Generate Report");
        alert.setHeaderText("Do you want to generate a shopping cart report?");


        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {

                String reportContent = generateReport();

                saveReportToFile(reportContent);

                Alert confirmationAlert = new Alert(Alert.AlertType.INFORMATION);
                confirmationAlert.setTitle("Report Saved");
                confirmationAlert.setHeaderText("Your shopping cart report has been saved.");
                confirmationAlert.showAndWait();
            }
        });
    }

    private void initializeOrderMenu() {
        ContextMenu orderMenu = new ContextMenu();

        MenuItem orderHistory = new MenuItem("My order history");
        orderHistory.setOnAction(event -> goToMyOrderHistory());

        MenuItem trackOrder = new MenuItem("Track current orders");
        trackOrder.setOnAction(event -> {
            trackMyOrder();
            addOrderActionButtonsToTable();
        });

        orderMenu.getItems().addAll(orderHistory, trackOrder);

        orderIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (SessionManager.isLoggedIn()) {
                orderMenu.show(orderIcon, event.getScreenX(), event.getScreenY());
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Login needed", "Please Login to view your orders details!");
            }
        });
    }


    private void goToMyOrderHistory() {
        oldOrderScrollPane.setVisible(true);
        oldOrderScrollPane.setManaged(true);

        int userId = SessionManager.getCurrentUser().getId();
        List<Order> allOrders = orderService.getOrdersByUserId(userId);
        List<Order> consolidatedOrders = new ArrayList<>();

        for (Order order : allOrders) {
            if (!order.getStatus().equalsIgnoreCase("delivered")) {
                continue;
            }

            Order existingOrder = null;
            for (Order consolidatedOrder : consolidatedOrders) {
                if (consolidatedOrder.getOrderDate().equals(order.getOrderDate())) {
                    existingOrder = consolidatedOrder;
                    break;
                }
            }

            int productid = order.getProductId();
            Product product = productService.getProductById(productid);
            String pName = product.getName();

            if (existingOrder == null) {
                List<String> products = new ArrayList<>();
                products.add(pName + " x" + order.getQuantity());
                consolidatedOrders.add(new Order(order.getId(), userId, order.getOrderDate(), products, order.getTotalPrice(), order.getStatus()));
            } else {
                existingOrder.getProducts().add(pName + " x" + order.getQuantity());
                existingOrder.setTotalPrice(existingOrder.getTotalPrice() + order.getTotalPrice());
            }
        }

        // Display consolidated orders
        ordersTable.getItems().clear();
        ordersTable.getItems().addAll(consolidatedOrders);
        System.out.println("Displayed " + consolidatedOrders.size() + " consolidated orders.");
    }


    private void loadOrders() {
        List<Order> orders = orderService.getAllOrders();
        ordersTable.getItems().clear();
        ordersTable.getItems().addAll(orders);
        System.out.println("Loaded " + orders.size() + " orders.");
    }

    private void handleCloseOldOrders() {
        oldOrderScrollPane.setVisible(false);
        oldOrderScrollPane.setManaged(false);
    }


    private void handlePendingCloseOldOrders() {
        pendingOrderScrollPane.setVisible(false);
        pendingOrderScrollPane.setManaged(false);
    }

    private void trackMyOrder() {
        pendingOrderScrollPane.setVisible(true);
        pendingOrderScrollPane.setManaged(true);

        int userId = SessionManager.getCurrentUser().getId();
        List<Order> allOrders = orderService.getOrdersByUserId(userId);
        List<Order> consolidatedOrders = new ArrayList<>();

        for (Order order : allOrders) {
            if (!order.getStatus().equalsIgnoreCase("Pending")) {
                continue;
            }

            Order existingOrder = null;
            for (Order consolidatedOrder : consolidatedOrders) {
                if (consolidatedOrder.getOrderDate().equals(order.getOrderDate())) {
                    existingOrder = consolidatedOrder;
                    break;
                }
            }

            int productid = order.getProductId();
            Product product = productService.getProductById(productid);
            String pName = product.getName();

            if (existingOrder == null) {
                List<String> products = new ArrayList<>();
                products.add(pName + " x" + order.getQuantity());
                consolidatedOrders.add(new Order(order.getId(), userId, order.getOrderDate(), products, order.getTotalPrice(), order.getStatus()));
            } else {
                existingOrder.getProducts().add(pName + " x" + order.getQuantity());
                existingOrder.setTotalPrice(existingOrder.getTotalPrice() + order.getTotalPrice());
            }
        }

        // Display consolidated orders
        pendingOrdersTable.getItems().clear();
        pendingOrdersTable.getItems().addAll(consolidatedOrders);
        System.out.println("Displayed " + consolidatedOrders.size() + " consolidated orders.");

    }

    private void addOrderActionButtonsToTable() {
        Callback<TableColumn<Order, Void>, TableCell<Order, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Order, Void> call(final TableColumn<Order, Void> param) {
                final TableCell<Order, Void> cell = new TableCell<>() {

                    private final Button cancelButton = new Button("Cancel");
                    private final Button updateButton = new Button("Update");

                    {
                        updateButton.setOnAction(event -> {
                            Order selectedorder = getTableView().getItems().get(getIndex());
                            orderIdForUpdate.setText(String.valueOf(selectedorder.getId()));
                            System.out.println("item with id :" + selectedorder.getId() + " is selected");
                            getProductsForUpdate(selectedorder);
                            displayNewCart();
                        });

                        cancelButton.setOnAction(event -> {
                            Order order = getTableView().getItems().get(getIndex());
                            cancelOrderById(order.getId());
                        });

                        HBox actionButtons = new HBox(updateButton, cancelButton);
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

    private void cancelOrderById(int orderId) {
        try {
            Order currentorder = orderService.getOrderById(orderId);
            if (currentorder == null) {
                System.out.println("Error: Order not found.");
                return;
            }
            List<Order> allUserOrders = orderService.getOrdersByUserId(SessionManager.getCurrentUser().getId());
            for (Order order : allUserOrders) {
                if (order.getOrderDate().equals(currentorder.getOrderDate())) {
                    order.setStatus("Cancelled");
                    orderService.updateOrder(order);
                    System.out.println("Order cancelled successfully: ID " + orderId);
                }
            }
            trackMyOrder();
            addOrderActionButtonsToTable();
        } catch (NumberFormatException e) {
            System.out.println("Error: Order ID must be a number.");
        }
    }

    private void cancelUpdateOrder() {
        modifyOrderVbox.setManaged(false);
        modifyOrderVbox.setVisible(false);
    }


    private void getProductsForUpdate(Order order) {
        int userId = SessionManager.getCurrentUser().getId();
        List<Order> allOrders = orderService.getOrdersByUserId(userId); // Get all orders for the current user

        forUpdateitems.clear();
        for (Order myOrder : allOrders) {
            if (!myOrder.getStatus().equalsIgnoreCase("Pending")) {
                continue; // Skip non-pending orders
            }

            // Ensure we are processing the correct order
            if (myOrder.getOrderDate().equals(order.getOrderDate()) && myOrder.getUserId() == order.getUserId()) {
                for (int i = 0; i < myOrder.getQuantity(); i++) {
                    forUpdateitems.add(productService.getProductById(myOrder.getProductId()));
                }
            }
        }
    }


    private void AddToUpdateCart(Product product) {
        int orderId = getOrderIdByProduct(product.getId());
        Order theOrder = orderService.getOrderById(orderId);
        if (theOrder == null) {
            System.out.println("No matching order found for product with ID: " + product.getId());
            return;  // Or handle the error as appropriate
        }
        product.setQuantity(1); // Set initial quantity as 1
        forUpdateitems.add(product);
        theOrder.setQuantity(theOrder.getQuantity() + 1);
        orderService.updateOrder(theOrder);
        trackMyOrder();
        System.out.println("Added to cart: " + product.getName());
        getProductsForUpdate(theOrder);
        displayNewCart(); // Update cart display after adding the product
    }


    private void RemoveFromUpdateCart(Product product) {
        int orderId = getOrderIdByProduct(product.getId());
        Order theOrder = orderService.getOrderById(orderId);
        if (theOrder == null) {
            System.out.println("No matching order found for product with ID: " + product.getId());
            return;  // Or handle the error as appropriate
        }
        System.out.println("orderid: " + theOrder.getId());
        if (theOrder.getQuantity() == 1 && Integer.parseInt(orderIdForUpdate.getText()) == theOrder.getId()) {
            orderIdForUpdate.setText(String.valueOf(Integer.parseInt(orderIdForUpdate.getText()) + 1));
            orderService.deleteOrder(orderId);
        } else if (theOrder.getQuantity() == 1 && !orderIdForUpdate.getText().equals(theOrder.getId())) {
            orderService.deleteOrder(orderId);
        } else {
            product.setQuantity(1); // Set initial quantity as 1
            forUpdateitems.remove(product);
            theOrder.setQuantity(theOrder.getQuantity() - 1);
            orderService.updateOrder(theOrder);
        }
        System.out.println("removed from new cart: " + product.getName());
        trackMyOrder();
        getProductsForUpdate(theOrder);
        displayNewCart(); // Update cart display after adding the product
        addOrderActionButtonsToTable();
    }

    private void displayNewCart() {
        modifyOrderVbox.setVisible(true);
        modifyOrderVbox.setManaged(true);

        itemListForUpdate.getChildren().clear();
        double totalAmount = 0;

        // List to keep track of already processed products
        List<Integer> processedProducts = new ArrayList<>();

        for (Product product : forUpdateitems) {
            if (processedProducts.contains(product.getId())) {
                continue;
            }

            // Calculate the total quantity for this product
            int quantity = 0;
            for (Product cartProduct : forUpdateitems) {
                if (cartProduct.getId() == product.getId()) {
                    quantity++; // Sum quantities for this product
                }
            }
            // Create UI components for the product
            HBox cartItem = new HBox();
            cartItem.setSpacing(10);
            cartItem.setAlignment(Pos.CENTER);
            cartItem.setStyle("-fx-background-color: #dcdcdc;");
            Label nameLabel = new Label(product.getName());
            nameLabel.setStyle("-fx-font-size: 14px;");


            Label priceLabel = new Label("$" + product.getPrice());
            priceLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #e64a19;");

            Label quantityLabel = new Label("x" + quantity);
            quantityLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f7d7d;");

            Button add = new Button("add");
            add.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 10px; -fx-padding: 2px 5px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
            add.setOnAction(event -> AddToUpdateCart(product));

            Button remove = new Button("remove");
            remove.setStyle("-fx-background-color: #0515cd; -fx-text-fill: white; -fx-font-size: 10px; -fx-padding: 2px 5px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
            remove.setOnAction(event -> RemoveFromUpdateCart(product));

            // Add elements to cartItem HBox
            cartItem.getChildren().addAll(nameLabel, priceLabel, quantityLabel, add, remove);

            // Add cartItem to itemListForUpdate
            itemListForUpdate.getChildren().add(cartItem);

            // Calculate total amount
            totalAmount += product.getPrice() * quantity;

            // Mark the product as processed to avoid adding it again
            processedProducts.add(product.getId());
        }

        // Update total amount labels
        totalAmountLabel.setText("$" + String.format("%.2f", totalAmount));
        updatedPrice.setText(String.valueOf(totalAmount));
    }


    private void confirmOrderUpdate() {
        trackMyOrder();
        addOrderActionButtonsToTable();
        cancelUpdateOrder();
        showAlert(Alert.AlertType.INFORMATION, "Success", "Order updated successfully!");
    }

    private int getOrderIdByProduct(int productid) {
        int userId = SessionManager.getCurrentUser().getId();
        List<Order> allOrders = orderService.getOrdersByUserId(userId);
        Order theOrder = orderService.getOrderById(Integer.parseInt(orderIdForUpdate.getText()));
        // Loop through all orders to find the product with the given productId
        for (Order order : allOrders) {
            if (order.getStatus().equals("Pending") && order.getProductId() == productid && order.getOrderDate().equals(theOrder.getOrderDate())) {
                return order.getId();
            }
        }
        System.out.println("order not found for this product");
        return 0;
    }


}

