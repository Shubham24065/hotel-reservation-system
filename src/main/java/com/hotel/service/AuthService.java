package com.hotel.service;

import com.hotel.model.AdminUser;
import com.hotel.model.enums.Role;
import com.hotel.repository.impl.AdminUserRepository;
import com.hotel.security.PasswordUtil;

public class AuthService {
    private final AdminUserRepository adminUserRepository;
    private final ActivityLogService activityLogService;

    public AuthService(AdminUserRepository adminUserRepository, ActivityLogService activityLogService) {
        this.adminUserRepository = adminUserRepository;
        this.activityLogService = activityLogService;
    }

    public AdminUser authenticate(String username, String password) {
        AdminUser user = adminUserRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password."));
        if (!PasswordUtil.matches(password, user.getPasswordHash())) {
            activityLogService.log(username, "LOGIN_FAILED", "AdminUser", username, "Invalid password attempt.");
            throw new IllegalArgumentException("Invalid username or password.");
        }
        activityLogService.log(user.getUsername(), "LOGIN_SUCCESS", "AdminUser", user.getUsername(), "Admin logged in successfully.");
        return user;
    }

    public void seedDefaultAdmins() {
        if (adminUserRepository.findAll().isEmpty()) {
            adminUserRepository.save(new AdminUser("admin", PasswordUtil.hash("admin123"), Role.ADMIN, "System Admin"));
            adminUserRepository.save(new AdminUser("manager", PasswordUtil.hash("manager123"), Role.MANAGER, "Duty Manager"));
        }
    }
}
