package com.hotel.controller;

import com.hotel.config.AppConfig;
import com.hotel.model.AdminUser;
import com.hotel.security.AuthSession;
import com.hotel.util.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AdminLoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label footerDateTimeLabel;

    @FXML
    private void handleBack(ActionEvent event) {
        SceneNavigator.switchScene((Node) event.getSource(), "/view/welcome.fxml", "Welcome");
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        try {
            AdminUser adminUser = AppConfig.authService().authenticate(usernameField.getText().trim(), passwordField.getText());
            AuthSession.login(adminUser);
            SceneNavigator.switchScene((Node) event.getSource(), "/view/admin-dashboard.fxml", "Admin Dashboard");
        } catch (Exception exception) {
            SceneNavigator.showValidation(exception.getMessage());
        }
    }

    @FXML
    public void initialize() {
        DateTimeHelper.startClock(footerDateTimeLabel);
    }
}
