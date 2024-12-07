package org.example.ecommercejavafx.models;

import java.sql.Timestamp;
import java.util.List;

public class Order {
    // Fields for Order
    private int id;
    private int userId;
    private int productId;
    private int quantity;
    private double totalPrice;
    private double discountedPrice; // New field for discounted price
    private Timestamp orderDate;
    private String status;
    private Integer discountId; // Optional field for discount ID
    //added for customer orders
    private List<String> products;


    // Constructors
    public Order() {
    }

    // Constructor without discountedPrice
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

    // Constructor with discountedPrice
    public Order(int id, int userId, int productId, int quantity, double totalPrice, Timestamp orderDate, String status, Integer discountId, double discountedPrice) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.status = status;
        this.discountId = discountId;
        this.discountedPrice = discountedPrice;
    }

    public Order(int id, int userId, Timestamp orderDate, List<String> products, double totalPrice,String status) {
        this.id = id;
        this.userId = userId;
        this.orderDate = orderDate;
        this.products = products;
        this.totalPrice = totalPrice;
        this.status=status;
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

    public double getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(double discountedPrice) {
        this.discountedPrice = discountedPrice;
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

    // Add getter and setter
    public List<String> getProducts() {
        return products;
    }
    public void setProducts(List<String> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                ", discountedPrice=" + discountedPrice +
                ", orderDate=" + orderDate +
                ", status='" + status + '\'' +
                ", discountId=" + discountId +
                '}';
    }
}
