package org.example.ecommercejavafx.services;
import org.example.ecommercejavafx.models.Review;
import org.example.ecommercejavafx.utils.DatabaseUtils;


import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReviewService {

    // Method to add a review
    public void addReview(Review review) {
        String sql = "INSERT INTO reviews (productId, userId, rating, reviewText, reviewDate) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, review.getProductId());
            statement.setInt(2, review.getUserId());
            statement.setInt(3, review.getRating());
            statement.setString(4, review.getReviewText());
            statement.setDate(5, Date.valueOf(review.getReviewDate()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error adding review to the database", e);
        }
    }

    // Method to update a review
    public void updateReview(Review review) {
        String sql = "UPDATE reviews SET productId = ?, userId = ?, rating = ?, reviewText = ?, reviewDate = ? WHERE id = ?";
        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, review.getProductId());
            statement.setInt(2, review.getUserId());
            statement.setInt(3, review.getRating());
            statement.setString(4, review.getReviewText());
            statement.setDate(5, Date.valueOf(review.getReviewDate()));
            statement.setInt(6, review.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating review in the database", e);
        }
    }

    // Method to delete a review
    public void deleteReview(int reviewId) {
        String sql = "DELETE FROM reviews WHERE id = ?";
        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, reviewId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting review from the database", e);
        }
    }

    // Method to get all reviews
    public List<Review> getAllReviews() {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT * FROM reviews";
        try (Connection connection = DatabaseUtils.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Review review = new Review(
                        resultSet.getInt("id"),
                        resultSet.getInt("productId"),
                        resultSet.getInt("userId"),
                        resultSet.getInt("rating"),
                        resultSet.getString("reviewText"),
                        resultSet.getDate("reviewDate").toLocalDate()
                );
                reviews.add(review);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving reviews from the database", e);
        }
        return reviews;
    }
}
