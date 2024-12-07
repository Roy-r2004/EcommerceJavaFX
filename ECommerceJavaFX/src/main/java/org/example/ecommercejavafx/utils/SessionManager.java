package org.example.ecommercejavafx.utils;

import org.example.ecommercejavafx.models.User;

public class SessionManager {
    private static boolean isLoggedIn = false;
    private static String userRole;
    private static User currentUser;

    public static boolean isLoggedIn() {
        return isLoggedIn;
    }

    public static void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public static String getUserRole() {
        return userRole;
    }

    public static void setUserRole(String role) {
        userRole = role;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
        isLoggedIn = (user != null); // Automatically update isLoggedIn status
    }
}
