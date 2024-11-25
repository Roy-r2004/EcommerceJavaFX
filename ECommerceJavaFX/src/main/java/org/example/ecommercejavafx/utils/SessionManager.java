package org.example.ecommercejavafx.utils;

public class SessionManager {
    private static boolean isLoggedIn = false;
    private static String userRole;

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
}
