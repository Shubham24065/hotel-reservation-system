package com.hotel.controller;

import com.hotel.model.BookingSession;
import com.hotel.util.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

import java.time.temporal.ChronoUnit;

public class AddonsController {
    @FXML private CheckBox breakfastCheckBox;
    @FXML private CheckBox wifiCheckBox;
    @FXML private CheckBox parkingCheckBox;
    @FXML private CheckBox spaCheckBox;
    @FXML private Label footerDateTimeLabel;

    @FXML
    private void handleBack(ActionEvent event) {
        SceneNavigator.switchScene((Node) event.getSource(), "/view/room-selection.fxml", "Room Selection");
    }

    @FXML
    private void handleSkip(ActionEvent event) {
        BookingSession session = BookingSession.getInstance();
        session.getAddons().clear();
        session.setAddonsPrice(0);

        System.out.println("Opening reservation summary from Skip...");
        SceneNavigator.switchScene((Node) event.getSource(), "/view/reservation-summary.fxml", "Reservation Summary");
    }

    @FXML
    private void handleNext(ActionEvent event) {
        BookingSession session = BookingSession.getInstance();

        if (session.getCheckInDate() == null || session.getCheckOutDate() == null) {
            SceneNavigator.showValidation("Stay dates are missing. Please go back and complete the booking flow.");
            return;
        }

        session.getAddons().clear();

        long nights = ChronoUnit.DAYS.between(session.getCheckInDate(), session.getCheckOutDate());
        if (nights <= 0) {
            SceneNavigator.showValidation("Invalid stay duration. Please go back and correct the dates.");
            return;
        }

        double addonsPrice = 0;

        if (breakfastCheckBox.isSelected()) {
            session.getAddons().add("Breakfast");
            addonsPrice += 30.0 * nights;
        }
        if (wifiCheckBox.isSelected()) {
            session.getAddons().add("Wi-Fi");
            addonsPrice += 20.0;
        }
        if (parkingCheckBox.isSelected()) {
            session.getAddons().add("Parking");
            addonsPrice += 15.0 * nights;
        }
        if (spaCheckBox.isSelected()) {
            session.getAddons().add("Spa Access");
            addonsPrice += 40.0;
        }

        session.setAddonsPrice(addonsPrice);

        System.out.println("Opening reservation summary from Next...");
        System.out.println("Selected add-ons: " + session.getAddons());
        System.out.println("Add-ons price: " + session.getAddonsPrice());

        SceneNavigator.switchScene((Node) event.getSource(), "/view/reservation-summary.fxml", "Reservation Summary");
    }

    @FXML
    public void initialize() {
        DateTimeHelper.startClock(footerDateTimeLabel);
    }
}