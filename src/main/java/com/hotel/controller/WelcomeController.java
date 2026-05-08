package com.hotel.controller;

import com.hotel.model.BookingSession;
import com.hotel.util.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class WelcomeController {
    @FXML private Label footerDateTimeLabel;

    @FXML
    private void handleStartBooking(ActionEvent event) {
        BookingSession.getInstance().reset();
        SceneNavigator.switchScene((javafx.scene.Node) event.getSource(), "/view/guest-count.fxml", "Guest Count");
    }

    @FXML
    private void handleFeedback(ActionEvent event) {
        SceneNavigator.switchScene((javafx.scene.Node) event.getSource(), "/view/feedback.fxml", "Feedback Form");
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        SceneNavigator.switchScene((javafx.scene.Node) event.getSource(), "/view/admin-login.fxml", "Admin Login");
    }

    @FXML
    private void handleRules(ActionEvent event) {
        SceneNavigator.showInfo("Rules and Regulations",
                "Check-in starts at 3 PM. Check-out is at 11 AM.\n" +
                        "Government-issued ID is required.\n" +
                        "No smoking inside rooms.\n" +
                        "Damages and unpaid balances must be settled at the front desk.");
    }

    @FXML
    public void initialize() {
        DateTimeHelper.startClock(footerDateTimeLabel);
    }
}
