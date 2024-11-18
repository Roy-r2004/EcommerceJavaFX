package org.example.ecommercejavafx.utils;

public class ValidationUtils {
    public static boolean isValidUsername(String username) {
        return username != null && username.length() >= 3;
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
}