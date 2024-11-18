package org.example.ecommercejavafx.models;

import java.sql.Timestamp;

public class Order {
    private int id;
    private int userId;
    private int productId;
    private int quantity;
    private double totalPrice;
    private Timestamp orderDate;
    private String status;
    private Integer discountId;  // New field for discount ID
    private  Integer discountedPrice;

    // Constructor with discount ID
    public Order(int id, int userId, int productId, int quantity, double totalPrice, Timestamp orderDate, String status, Integer discountId) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.status = status;
        this.discountId = discountId;
    }

    // Constructor without discount ID
    public Order(int id, int userId, int productId, int quantity, double totalPrice, Timestamp orderDate, String status) {
        this(id, userId, productId, quantity, totalPrice, orderDate, status, null);
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Integer discountId) {
        this.discountId = discountId;
    }

    public  Integer getDiscountedPrice(){
        return discountedPrice;
    }

    public void setDiscountedPrice(Integer discountId) {
        this.discountId = discountId;
    }

    // Method to apply discount to the total price
    public void applyDiscount(Discount discount) {
        if (discount != null) {
            this.discountId = discount.getId();
            if ("PERCENTAGE".equalsIgnoreCase(discount.getDiscountType())) {
                this.totalPrice -= this.totalPrice * (discount.getDiscountValue() / 100);
            } else if ("FIXED".equalsIgnoreCase(discount.getDiscountType())) {
                this.totalPrice -= discount.getDiscountValue();
            }
            // Ensure the total price does not go below zero
            this.totalPrice = Math.max(0, this.totalPrice);
        }
    }

    @Override
    public String toString() {
        return String.format("Order ID: %d | User ID: %d | Product ID: %d | Quantity: %d | Total Price: $%.2f | Discount ID: %s | Status: %s",
                this.id, this.userId, this.productId, this.quantity, this.totalPrice,
                (this.discountId != null ? this.discountId.toString() : "None"), this.status);
    }
}
