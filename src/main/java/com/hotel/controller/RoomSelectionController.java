package com.hotel.controller;

import com.hotel.model.BookingSession;
import com.hotel.model.RoomOption;
import com.hotel.util.SceneNavigator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.temporal.ChronoUnit;

public class RoomSelectionController {
    @FXML private TableView<RoomOption> roomTable;
    @FXML private TableColumn<RoomOption, String> roomTypeColumn;
    @FXML private TableColumn<RoomOption, Integer> capacityColumn;
    @FXML private TableColumn<RoomOption, Double> priceColumn;
    @FXML private Label footerDateTimeLabel;
    @FXML private Label guestSummaryLabel;
    @FXML private Label dateSummaryLabel;

    @FXML
    public void initialize() {
        roomTypeColumn.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerNight"));

        roomTable.setItems(FXCollections.observableArrayList(
                new RoomOption("Single Room", 2, 120.0),
                new RoomOption("Double Room", 4, 220.0),
                new RoomOption("Deluxe Room", 2, 260.0),
                new RoomOption("Penthouse", 2, 450.0)
        ));

        BookingSession session = BookingSession.getInstance();
        guestSummaryLabel.setText("Adults: " + session.getAdults() + " | Children: " + session.getChildren());
        long nights = ChronoUnit.DAYS.between(session.getCheckInDate(), session.getCheckOutDate());
        dateSummaryLabel.setText(session.getCheckInDate() + " to " + session.getCheckOutDate() + " (" + nights + " nights)");

        DateTimeHelper.startClock(footerDateTimeLabel);
    }

    @FXML
    private void handleBack() {
        SceneNavigator.switchScene(roomTable, "/view/guest-info.fxml", "Guest Information");
    }

    @FXML
    private void handleNext() {
        RoomOption selectedRoom = roomTable.getSelectionModel().getSelectedItem();
        if (selectedRoom == null) {
            SceneNavigator.showValidation("Please select a room before continuing.");
            return;
        }

        BookingSession session = BookingSession.getInstance();
        int totalGuests = session.getAdults() + session.getChildren();
        if (totalGuests > selectedRoom.getCapacity()) {
            SceneNavigator.showValidation("This room does not support the selected number of guests. Please choose another room type.");
            return;
        }

        session.setSelectedRoomType(selectedRoom.getRoomType());
        session.setSelectedRoomPrice(selectedRoom.getPricePerNight());
        session.setRoomCount(1);
        SceneNavigator.switchScene(roomTable, "/view/addons.fxml", "Add-ons");
    }

    @FXML
    private void handleReset() {
        roomTable.getSelectionModel().clearSelection();
    }
}
