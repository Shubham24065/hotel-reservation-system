package com.hotel.controller;

import com.hotel.model.BookingSession;
import com.hotel.util.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;

public class StayDatesController {
    @FXML private DatePicker checkInDatePicker;
    @FXML private DatePicker checkOutDatePicker;
    @FXML private Label footerDateTimeLabel;

    @FXML
    private void handleBack(ActionEvent event) {
        SceneNavigator.switchScene((Node) event.getSource(), "/view/guest-count.fxml", "Guest Count");
    }

    @FXML
    private void handleNext(ActionEvent event) {
        LocalDate checkIn = checkInDatePicker.getValue();
        LocalDate checkOut = checkOutDatePicker.getValue();

        if (checkIn == null || checkOut == null) {
            SceneNavigator.showValidation("Please select both check-in and check-out dates.");
            return;
        }
        if (checkIn.isBefore(LocalDate.now())) {
            SceneNavigator.showValidation("Check-in date cannot be in the past.");
            return;
        }
        if (!checkOut.isAfter(checkIn)) {
            SceneNavigator.showValidation("Check-out date must be after check-in date.");
            return;
        }

        BookingSession session = BookingSession.getInstance();
        session.setCheckInDate(checkIn);
        session.setCheckOutDate(checkOut);
        SceneNavigator.switchScene((Node) event.getSource(), "/view/guest-info.fxml", "Guest Information");
    }

    @FXML
    public void initialize() {
        DateTimeHelper.startClock(footerDateTimeLabel);
    }
}
