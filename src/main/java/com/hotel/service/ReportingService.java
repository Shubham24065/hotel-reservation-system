package com.hotel.service;

import com.hotel.model.ActivityLog;
import com.hotel.model.Feedback;
import com.hotel.model.Reservation;
import com.hotel.util.CsvExporter;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportingService {
    private final ReservationService reservationService;
    private final ActivityLogService activityLogService;
    private final FeedbackService feedbackService;

    public ReportingService(ReservationService reservationService,
                            ActivityLogService activityLogService,
                            FeedbackService feedbackService) {
        this.reservationService = reservationService;
        this.activityLogService = activityLogService;
        this.feedbackService = feedbackService;
    }

    public Path exportRevenueReport() {
        List<String> rows = new ArrayList<>();
        rows.add("Reservation Code,Guest,Check In,Check Out,Subtotal,Discount,Tax,Total,Outstanding");
        for (Reservation reservation : reservationService.findAll()) {
            double subtotal = reservation.getRoomCharge() + reservation.getAddonsCharge();
            rows.add(String.join(",",
                    reservation.getReservationCode(),
                    safe(reservation.getGuest().getFullName()),
                    reservation.getCheckInDate().toString(),
                    reservation.getCheckOutDate().toString(),
                    String.valueOf(subtotal),
                    String.valueOf(reservation.getDiscountAmount()),
                    String.valueOf(reservation.getTaxAmount()),
                    String.valueOf(reservation.getTotalAmount()),
                    String.valueOf(reservation.getOutstandingBalance())));
        }
        return CsvExporter.export("revenue-report.csv", rows);
    }

    public Path exportOccupancyReport() {
        List<String> rows = new ArrayList<>();
        rows.add("Date,Rooms Available,Rooms Occupied,Occupancy Percentage");
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 7; i++) {
            LocalDate date = today.plusDays(i);
            long occupied = reservationService.activeReservationCount(date, date.plusDays(1));
            long totalRooms = 6; // seeded rooms
            long available = Math.max(0, totalRooms - occupied);
            double percentage = totalRooms == 0 ? 0 : (occupied * 100.0 / totalRooms);
            rows.add(date + "," + available + "," + occupied + "," + Math.round(percentage * 100.0) / 100.0);
        }
        return CsvExporter.export("occupancy-report.csv", rows);
    }

    public Path exportActivityLogs() {
        List<String> rows = new ArrayList<>();
        rows.add("Timestamp,Actor,Action,Entity Type,Entity Identifier,Message");
        for (ActivityLog log : activityLogService.findAll()) {
            rows.add(String.join(",",
                    safe(String.valueOf(log.getTimestamp())),
                    safe(log.getActor()),
                    safe(log.getAction()),
                    safe(log.getEntityType()),
                    safe(log.getEntityIdentifier()),
                    safe(log.getMessage())));
        }
        return CsvExporter.export("activity-logs.csv", rows);
    }

    public Path exportFeedbackSummary() {
        List<String> rows = new ArrayList<>();
        rows.add("Reservation Code,Guest,Rating,Comment,Sentiment");
        for (Feedback feedback : feedbackService.findAll()) {
            rows.add(String.join(",",
                    safe(feedback.getReservation().getReservationCode()),
                    safe(feedback.getGuest().getFullName()),
                    String.valueOf(feedback.getRating()),
                    safe(feedback.getComment()),
                    safe(feedback.getSentimentTag())));
        }
        return CsvExporter.export("feedback-summary.csv", rows);
    }

    private String safe(String value) {
        return value == null ? "" : value.replace(",", " ");
    }
}
