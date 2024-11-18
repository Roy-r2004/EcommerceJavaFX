package org.example.ecommercejavafx.models;

public class Product {
    private int id;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private Category category; // Updated to use Category object
    private byte[] image; // New field for product image

    // Default Constructor
    public Product() {
    }

    // Constructor without Image
    public Product(int id, String name, String description, double price, int quantity, Category category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }

    // Constructor with all fields
    public Product(int id, String name, String description, double price, int quantity, Category category, byte[] image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.image = image;
    }

    // Constructor without ID (e.g., for new Products before they are saved to the database)
    public Product(String name, String description, double price, int quantity, Category category, byte[] image) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.image = image;
    }

    // Constructor without ID and without Image
    public Product(String name, String description, double price, int quantity, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public byte[] getImage() { return image; }
    public void setImage(byte[] image) { this.image = image; }

    // Additional method to get Category ID easily
    public int getCategoryId() {
        return category != null ? category.getId() : 0; // Return 0 if category is null
    }

    // Additional method to get Category Name easily
    public String getCategoryName() {
        return category != null ? category.getName() : "Unknown Category";
    }

    // Setter for Category Name
    public void setCategoryName(String categoryName) {
        if (this.category != null) {
            this.category.setName(categoryName);
        } else {
            this.category = new Category();
            this.category.setName(categoryName);
        }
    }
}
