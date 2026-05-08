package com.hotel.controller;

import com.hotel.model.BookingSession;
import com.hotel.util.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;

public class GuestCountController {
    @FXML private ComboBox<String> adultsComboBox;
    @FXML private ComboBox<String> childrenComboBox;
    @FXML private Label footerDateTimeLabel;

    @FXML
    private void handleBack(ActionEvent event) {
        SceneNavigator.switchScene((Node) event.getSource(), "/view/welcome.fxml", "Welcome");
    }

    @FXML
    private void handleNext(ActionEvent event) {
        if (adultsComboBox.getValue() == null) {
            SceneNavigator.showValidation("Please select the number of adults.");
            return;
        }

        BookingSession session = BookingSession.getInstance();
        session.setAdults(Integer.parseInt(adultsComboBox.getValue()));
        session.setChildren(childrenComboBox.getValue() == null ? 0 : Integer.parseInt(childrenComboBox.getValue()));
        SceneNavigator.switchScene((Node) event.getSource(), "/view/stay-dates.fxml", "Stay Dates");
    }

    @FXML
    public void initialize() {
        DateTimeHelper.startClock(footerDateTimeLabel);
    }
}
