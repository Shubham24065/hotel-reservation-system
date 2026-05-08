package com.hotel.controller;

import com.hotel.model.BookingSession;
import com.hotel.util.SceneNavigator;
import com.hotel.util.ValidationUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

public class GuestInfoController {
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private TextField passportField;
    @FXML private TextField countryField;
    @FXML private TextField addressField;
    @FXML private TextField cityField;
    @FXML private TextField emergencyContactField;
    @FXML private TextField emergencyPhoneField;
    @FXML private TextArea specialRequestsArea;
    @FXML private CheckBox loyaltyCheckBox;
    @FXML private Label footerDateTimeLabel;

    @FXML
    private void handleBack(ActionEvent event) {
        SceneNavigator.switchScene((Node) event.getSource(), "/view/stay-dates.fxml", "Stay Dates");
    }

    @FXML
    private void handleNext(ActionEvent event) {
        if (isBlank(firstNameField.getText()) || isBlank(lastNameField.getText()) ||
                isBlank(phoneField.getText()) || isBlank(emailField.getText()) ||
                isBlank(passportField.getText())) {
            SceneNavigator.showValidation("First name, last name, phone, email, and passport number are required.");
            return;
        }
        if (!ValidationUtil.isValidName(firstNameField.getText()) || !ValidationUtil.isValidName(lastNameField.getText())) {
            SceneNavigator.showValidation("Please enter a valid first and last name.");
            return;
        }
        if (!ValidationUtil.isValidPhone(phoneField.getText())) {
            SceneNavigator.showValidation("Please enter a valid phone number.");
            return;
        }
        if (!ValidationUtil.isValidEmail(emailField.getText())) {
            SceneNavigator.showValidation("Please enter a valid email address.");
            return;
        }

        BookingSession session = BookingSession.getInstance();
        session.setFirstName(firstNameField.getText().trim());
        session.setLastName(lastNameField.getText().trim());
        session.setPhone(phoneField.getText().trim());
        session.setEmail(emailField.getText().trim());
        session.setPassportNumber(passportField.getText().trim());
        session.setCountry(countryField.getText().trim());
        session.setAddress(addressField.getText().trim());
        session.setCity(cityField.getText().trim());
        session.setEmergencyContact(emergencyContactField.getText().trim());
        session.setEmergencyPhone(emergencyPhoneField.getText().trim());
        session.setSpecialRequests(specialRequestsArea.getText().trim());
        session.setLoyaltyEnrollment(loyaltyCheckBox.isSelected());

        SceneNavigator.switchScene((Node) event.getSource(), "/view/room-selection.fxml", "Room Selection");
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    @FXML
    public void initialize() {
        DateTimeHelper.startClock(footerDateTimeLabel);
    }
}
