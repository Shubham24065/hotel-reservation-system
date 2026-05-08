package com.hotel.security;

import com.hotel.model.AdminUser;

public final class AuthSession {
    private static AdminUser currentUser;

    private AuthSession() {
    }

    public static void login(AdminUser adminUser) {
        currentUser = adminUser;
    }

    public static void logout() {
        currentUser = null;
    }

    public static AdminUser getCurrentUser() {
        return currentUser;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }
}
