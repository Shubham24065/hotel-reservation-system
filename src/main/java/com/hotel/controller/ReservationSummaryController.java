package com.hotel.controller;

import com.hotel.config.AppConfig;
import com.hotel.model.BookingSession;
import com.hotel.model.Guest;
import com.hotel.model.Reservation;
import com.hotel.model.enums.RoomType;
import com.hotel.util.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class ReservationSummaryController {
    @FXML private Label footerDateTimeLabel;
    @FXML private Label bookingDetailsLabel;
    @FXML private Label subtotalLabel;
    @FXML private Label addonsLabel;
    @FXML private Label taxLabel;
    @FXML private Label totalLabel;

    @FXML
    private void handleBack(ActionEvent event) {
        SceneNavigator.switchScene((Node) event.getSource(), "/view/addons.fxml", "Add-ons");
    }

    @FXML
    private void handleConfirmReservation(ActionEvent event) {
        try {
            BookingSession session = BookingSession.getInstance();

            validateSession(session);

            Guest guest = new Guest();
            guest.setFirstName(nullToEmpty(session.getFirstName()));
            guest.setLastName(nullToEmpty(session.getLastName()));
            guest.setPhone(nullToEmpty(session.getPhone()));
            guest.setEmail(nullToEmpty(session.getEmail()));
            guest.setPassportNumber(nullToEmpty(session.getPassportNumber()));
            guest.setCountry(nullToEmpty(session.getCountry()));
            guest.setAddress(nullToEmpty(session.getAddress()));
            guest.setCity(nullToEmpty(session.getCity()));
            guest.setEmergencyContact(nullToEmpty(session.getEmergencyContact()));
            guest.setEmergencyPhone(nullToEmpty(session.getEmergencyPhone()));
            guest.setLoyaltyMember(session.isLoyaltyEnrollment());

            RoomType roomType = mapRoomType(session.getSelectedRoomType());

            double roomCharge = AppConfig.reservationService().applyWeekendMultiplier(
                    session.getSelectedRoomPrice(),
                    session.getCheckInDate(),
                    session.getCheckOutDate()
            );

            Reservation reservation = AppConfig.reservationService().createReservation(
                    guest,
                    roomType,
                    session.getRoomCount(),
                    session.getAdults(),
                    session.getChildren(),
                    session.getCheckInDate(),
                    session.getCheckOutDate(),
                    session.getAddons().isEmpty() ? "" : String.join(", ", session.getAddons()),
                    nullToEmpty(session.getSpecialRequests()),
                    roomCharge,
                    session.getAddonsPrice(),
                    session.isLoyaltyEnrollment()
            );

            SceneNavigator.showInfo(
                    "Reservation Confirmed",
                    "Reservation completed successfully for " + guest.getFullName().trim()
                            + "\nReservation Code: " + reservation.getReservationCode()
                            + "\nBilling will be handled at the front desk."
            );

            BookingSession.getInstance().reset();
            SceneNavigator.switchScene((Node) event.getSource(), "/view/welcome.fxml", "Welcome");

        } catch (Exception exception) {
            exception.printStackTrace();
            SceneNavigator.showValidation("Could not confirm reservation: " + exception.getMessage());
        }
    }

    @FXML
    public void initialize() {
        System.out.println("ReservationSummaryController initialize() called");
        try {
            BookingSession session = BookingSession.getInstance();

            LocalDate checkIn = session.getCheckInDate();
            LocalDate checkOut = session.getCheckOutDate();

            if (checkIn == null || checkOut == null) {
                bookingDetailsLabel.setText("Stay dates are missing. Please go back and select valid dates.");
                subtotalLabel.setText("$0.00");
                addonsLabel.setText("$0.00");
                taxLabel.setText("$0.00");
                totalLabel.setText("$0.00");
                DateTimeHelper.startClock(footerDateTimeLabel);
                return;
            }

            if (session.getSelectedRoomType() == null || session.getSelectedRoomType().isBlank()) {
                bookingDetailsLabel.setText("Room selection is missing. Please go back and choose a room.");
                subtotalLabel.setText("$0.00");
                addonsLabel.setText("$0.00");
                taxLabel.setText("$0.00");
                totalLabel.setText("$0.00");
                DateTimeHelper.startClock(footerDateTimeLabel);
                return;
            }

            long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
            if (nights <= 0) {
                bookingDetailsLabel.setText("Invalid stay duration. Please go back and correct the dates.");
                subtotalLabel.setText("$0.00");
                addonsLabel.setText("$0.00");
                taxLabel.setText("$0.00");
                totalLabel.setText("$0.00");
                DateTimeHelper.startClock(footerDateTimeLabel);
                return;
            }

            double roomSubtotal = AppConfig.reservationService().applyWeekendMultiplier(
                    session.getSelectedRoomPrice(),
                    checkIn,
                    checkOut
            );
            double addons = session.getAddonsPrice();
            double tax = round((roomSubtotal + addons) * 0.13);
            double total = round(roomSubtotal + addons + tax);

            bookingDetailsLabel.setText(
                    "Adults: " + session.getAdults() + " | Children: " + session.getChildren()
                            + "\nStay: " + checkIn + " to " + checkOut + " (" + nights + " nights)"
                            + "\nRoom: " + session.getRoomCount() + " x " + session.getSelectedRoomType()
                            + "\nAdd-ons: " + (session.getAddons().isEmpty() ? "None" : String.join(", ", session.getAddons()))
                            + "\nStatus: Pending confirmation"
            );

            subtotalLabel.setText(format(roomSubtotal));
            addonsLabel.setText(format(addons));
            taxLabel.setText(format(tax));
            totalLabel.setText(format(total));

            DateTimeHelper.startClock(footerDateTimeLabel);

        } catch (Exception exception) {
            exception.printStackTrace();
            bookingDetailsLabel.setText("Error loading reservation summary: " + exception.getMessage());
            subtotalLabel.setText("$0.00");
            addonsLabel.setText("$0.00");
            taxLabel.setText("$0.00");
            totalLabel.setText("$0.00");
            DateTimeHelper.startClock(footerDateTimeLabel);
        }
    }

    private void validateSession(BookingSession session) {
        if (session.getCheckInDate() == null || session.getCheckOutDate() == null) {
            throw new IllegalArgumentException("Stay dates are missing.");
        }
        if (session.getSelectedRoomType() == null || session.getSelectedRoomType().isBlank()) {
            throw new IllegalArgumentException("Room selection is missing.");
        }
        if (session.getAdults() + session.getChildren() <= 0) {
            throw new IllegalArgumentException("Guest count is missing.");
        }
    }

    private RoomType mapRoomType(String roomType) {
        String clean = roomType.toUpperCase(Locale.ENGLISH).replace(" ", "_");
        return switch (clean) {
            case "SINGLE_ROOM" -> RoomType.SINGLE;
            case "DOUBLE_ROOM" -> RoomType.DOUBLE;
            case "DELUXE_ROOM" -> RoomType.DELUXE;
            case "PENTHOUSE" -> RoomType.PENTHOUSE;
            default -> throw new IllegalArgumentException("Unknown room type: " + roomType);
        };
    }

    private String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    private String format(double amount) {
        return String.format("$%.2f", amount);
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}