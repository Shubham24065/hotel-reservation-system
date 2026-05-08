package com.hotel.controller;

import com.hotel.config.AppConfig;
import com.hotel.util.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FeedbackController {
    @FXML private TextField reservationIdField;
    @FXML private TextField overallRatingField;
    @FXML private TextArea additionalCommentsArea;
    @FXML private Label footerDateTimeLabel;

    @FXML
    private void handleClearForm() {
        if (reservationIdField != null) reservationIdField.clear();
        if (overallRatingField != null) overallRatingField.clear();
        if (additionalCommentsArea != null) additionalCommentsArea.clear();
    }

    @FXML
    private void handleSubmitFeedback(ActionEvent event) {
        try {
            int rating = Integer.parseInt(overallRatingField.getText().trim());
            AppConfig.feedbackService().submitFeedback(reservationIdField.getText().trim(), rating, additionalCommentsArea.getText().trim());
            SceneNavigator.showInfo("Thank You", "Your feedback has been submitted.");
            SceneNavigator.switchScene((Node) event.getSource(), "/view/welcome.fxml", "Welcome");
        } catch (Exception exception) {
            SceneNavigator.showValidation(exception.getMessage());
        }
    }

    @FXML
    private void handleExit(ActionEvent event) {
        SceneNavigator.switchScene((Node) event.getSource(), "/view/welcome.fxml", "Welcome");
    }

    @FXML
    public void initialize() {
        DateTimeHelper.startClock(footerDateTimeLabel);
    }
}
