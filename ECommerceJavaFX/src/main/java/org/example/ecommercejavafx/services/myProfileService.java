package org.example.ecommercejavafx.services;

import org.example.ecommercejavafx.utils.DatabaseUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class myProfileService {

    public static boolean createProfile(int id, String name, String email, String phone, String address, String profilePicturePath) {
        String query = "INSERT INTO user_profiles (user_id, full_name, email, phone, address, profile_picture) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            stmt.setString(2, name);
            stmt.setString(3, email);
            stmt.setString(4, phone);
            stmt.setString(5, address);

            // Set profile picture as byte array
            if (profilePicturePath != null) {
                File imageFile = new File(profilePicturePath);
                try (FileInputStream fis = new FileInputStream(imageFile)) {
                    stmt.setBytes(6, fis.readAllBytes());
                }
            } else {
                stmt.setBytes(6, null);
            }

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateProfile(int userId, String name, String email, String phone, String address, String profilePicturePath) {
        String query;
        boolean hasProfilePic = profilePicturePath != null && !profilePicturePath.isEmpty(); // Check if the new picture is provided

        if (hasProfilePic) {
            query = "UPDATE user_profiles SET full_name = ?, email = ?, phone = ?, address = ?, profile_picture = ? WHERE user_id = ?";
        } else {
            query = "UPDATE user_profiles SET full_name = ?, email = ?, phone = ?, address = ? WHERE user_id = ?";
        }

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set query parameters
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.setString(4, address);

            if (hasProfilePic) {
                File imageFile = new File(profilePicturePath);
                try (FileInputStream fis = new FileInputStream(imageFile)) {
                    stmt.setBytes(5, fis.readAllBytes()); // Set the profile picture as a byte array
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                stmt.setInt(6, userId);
            } else {
                stmt.setInt(5, userId);
            }


            // Execute the update
            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }




}
