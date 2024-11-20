package org.example.ecommercejavafx.models;

import java.time.LocalDate;

public class Review {
    private int id;
    private int productId;
    private int userId;
    private int rating;
    private String reviewText;
    private LocalDate reviewDate;

    // Constructor with all parameters
    public Review(int id, int productId, int userId, int rating, String reviewText, LocalDate reviewDate) {
        this.id = id;
        this.productId = productId;
        this.userId = userId;
        this.rating = rating;
        this.reviewText = reviewText;
        this.reviewDate = reviewDate;
    }

    // Constructor without ID (useful for inserting a new review)
    public Review(int productId, int userId, int rating, String reviewText, LocalDate reviewDate) {
        this.productId = productId;
        this.userId = userId;
        this.rating = rating;
        this.reviewText = reviewText;
        this.reviewDate = reviewDate;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public LocalDate getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDate reviewDate) {
        this.reviewDate = reviewDate;
    }
}
