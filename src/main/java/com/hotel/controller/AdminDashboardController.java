package com.hotel.controller;

import com.hotel.config.AppConfig;
import com.hotel.model.Reservation;
import com.hotel.model.enums.PaymentMethod;
import com.hotel.model.enums.RoomType;
import com.hotel.security.AuthSession;
import com.hotel.util.SceneNavigator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class AdminDashboardController {
    @FXML private Label footerDateTimeLabel;
    @FXML private Label welcomeLabel;
    @FXML private TextField searchField;
    @FXML private TableView<Reservation> reservationTable;
    @FXML private TableColumn<Reservation, String> codeColumn;
    @FXML private TableColumn<Reservation, String> guestColumn;
    @FXML private TableColumn<Reservation, String> statusColumn;
    @FXML private TableColumn<Reservation, String> amountColumn;

    @FXML
    private void handleLogout(javafx.event.ActionEvent event) {
        AuthSession.logout();
        SceneNavigator.switchScene((javafx.scene.Node) event.getSource(), "/view/welcome.fxml", "Welcome");
    }

    @FXML private void openReservations() { loadReservations(AppConfig.reservationService().findAll()); }
    @FXML private void openGuests() { SceneNavigator.showInfo("Guests", "Guest records are managed from reservations and loyalty enrollment."); }
    @FXML private void openPayments() { handleProcessPayment(); }
    @FXML private void openReports() { handleViewReports(); }
    @FXML private void openFeedback() { SceneNavigator.showInfo("Feedback", "Feedback entries are exported through the Reports flow."); }
    @FXML private void openLoyalty() { SceneNavigator.showInfo("Loyalty", "Loyalty points are earned during payments and can be redeemed through service logic."); }
    @FXML private void openWaitlist() { addWaitlistEntry(); }

    @FXML private void handleSearchReservation() { handleFind(); }

    @FXML
    private void handleModifyBooking() {
        Reservation reservation = getSelectedReservation();
        if (reservation == null) return;
        String message = "Reservation " + reservation.getReservationCode() + " is currently " + reservation.getStatus() +
                "\nGuest: " + reservation.getGuest().getFullName() +
                "\nOutstanding: $" + reservation.getOutstandingBalance();
        SceneNavigator.showInfo("Reservation Details", message);
    }

    @FXML
    private void handleProcessPayment() {
        Reservation reservation = getSelectedReservation();
        if (reservation == null) return;
        TextInputDialog amountDialog = new TextInputDialog();
        amountDialog.setTitle("Process Payment");
        amountDialog.setHeaderText("Enter payment amount for " + reservation.getReservationCode());
        Optional<String> amountInput = amountDialog.showAndWait();
        if (amountInput.isEmpty()) return;
        try {
            double amount = Double.parseDouble(amountInput.get());
            ChoiceDialog<PaymentMethod> methodDialog = new ChoiceDialog<>(PaymentMethod.CARD, PaymentMethod.values());
            methodDialog.setTitle("Payment Method");
            methodDialog.setHeaderText("Choose a payment method");
            Optional<PaymentMethod> method = methodDialog.showAndWait();
            if (method.isEmpty()) return;
            AppConfig.paymentService().processPayment(reservation.getReservationCode(), method.get(), amount, AuthSession.getCurrentUser().getUsername());
            loadReservations(AppConfig.reservationService().findAll());
            SceneNavigator.showInfo("Payment Processed", "Payment recorded successfully.");
        } catch (Exception exception) {
            SceneNavigator.showValidation(exception.getMessage());
        }
    }

    @FXML
    private void handleCheckoutGuest() {
        Reservation reservation = getSelectedReservation();
        if (reservation == null) return;
        try {
            AppConfig.reservationService().checkout(reservation.getReservationCode(), AuthSession.getCurrentUser().getUsername());
            loadReservations(AppConfig.reservationService().findAll());
            SceneNavigator.showInfo("Check-out Complete", "Guest checked out successfully.");
        } catch (Exception exception) {
            SceneNavigator.showValidation(exception.getMessage());
        }
    }

    @FXML
    private void handleApplyDiscount() {
        Reservation reservation = getSelectedReservation();
        if (reservation == null) return;
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Apply Discount");
        dialog.setHeaderText("Enter discount percent for " + reservation.getReservationCode());
        Optional<String> input = dialog.showAndWait();
        if (input.isEmpty()) return;
        try {
            double percent = Double.parseDouble(input.get());
            AppConfig.reservationService().applyDiscount(
                    reservation.getReservationCode(),
                    percent,
                    AuthSession.getCurrentUser().getRole(),
                    AuthSession.getCurrentUser().getUsername());
            loadReservations(AppConfig.reservationService().findAll());
            SceneNavigator.showInfo("Discount Applied", "Discount saved successfully.");
        } catch (Exception exception) {
            SceneNavigator.showValidation(exception.getMessage());
        }
    }

    @FXML
    private void handleViewReports() {
        Path revenue = AppConfig.reportingService().exportRevenueReport();
        Path occupancy = AppConfig.reportingService().exportOccupancyReport();
        Path activity = AppConfig.reportingService().exportActivityLogs();
        Path feedback = AppConfig.reportingService().exportFeedbackSummary();
        SceneNavigator.showInfo("Reports Exported",
                "Revenue: " + revenue + "\nOccupancy: " + occupancy + "\nActivity Logs: " + activity + "\nFeedback: " + feedback);
    }

    @FXML
    private void handleFind() {
        loadReservations(AppConfig.reservationService().search(searchField == null ? "" : searchField.getText()));
    }

    private void addWaitlistEntry() {
        TextInputDialog guestNameDialog = new TextInputDialog();
        guestNameDialog.setTitle("Waitlist");
        guestNameDialog.setHeaderText("Enter guest full name for the waitlist");
        Optional<String> guestName = guestNameDialog.showAndWait();
        if (guestName.isEmpty()) return;
        try {
            AppConfig.waitlistService().addEntry(guestName.get(), "", "", RoomType.DOUBLE, LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));
            SceneNavigator.showInfo("Waitlist Added", "Guest has been added to the waitlist.");
        } catch (Exception exception) {
            SceneNavigator.showValidation(exception.getMessage());
        }
    }

    private Reservation getSelectedReservation() {
        Reservation reservation = reservationTable.getSelectionModel().getSelectedItem();
        if (reservation == null) {
            SceneNavigator.showValidation("Please select a reservation from the table first.");
        }
        return reservation;
    }

    private void loadReservations(List<Reservation> reservations) {
        reservationTable.setItems(FXCollections.observableArrayList(reservations));
    }

    @FXML
    public void initialize() {
        codeColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getReservationCode()));
        guestColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getGuest().getFullName()));
        statusColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(String.valueOf(data.getValue().getStatus())));
        amountColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(String.format("$%.2f", data.getValue().getOutstandingBalance())));
        if (AuthSession.getCurrentUser() != null) {
            welcomeLabel.setText("Logged in as " + AuthSession.getCurrentUser().getFullName() + " (" + AuthSession.getCurrentUser().getRole() + ")");
        }
        loadReservations(AppConfig.reservationService().findAll());
        DateTimeHelper.startClock(footerDateTimeLabel);
    }
}
