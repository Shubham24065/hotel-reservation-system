package com.hotel.service;

import com.hotel.model.Feedback;
import com.hotel.model.Reservation;
import com.hotel.model.enums.ReservationStatus;
import com.hotel.repository.impl.FeedbackRepository;

import java.util.List;

public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final ReservationService reservationService;
    private final ActivityLogService activityLogService;

    public FeedbackService(FeedbackRepository feedbackRepository,
                           ReservationService reservationService,
                           ActivityLogService activityLogService) {
        this.feedbackRepository = feedbackRepository;
        this.reservationService = reservationService;
        this.activityLogService = activityLogService;
    }

    public Feedback submitFeedback(String reservationCode, int rating, String comment) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        Reservation reservation = reservationService.findByCode(reservationCode);
        if (reservation.getStatus() != ReservationStatus.CHECKED_OUT || reservation.getOutstandingBalance() > 0) {
            throw new IllegalArgumentException("Feedback can only be submitted after checkout and full payment.");
        }

        Feedback feedback = new Feedback();
        feedback.setReservation(reservation);
        feedback.setGuest(reservation.getGuest());
        feedback.setRating(rating);
        feedback.setComment(comment);
        feedback.setSentimentTag(resolveSentiment(rating));
        Feedback saved = feedbackRepository.save(feedback);
        activityLogService.log(reservation.getGuest().getFullName(), "SUBMIT_FEEDBACK", "Reservation", reservationCode, "Feedback submitted.");
        return saved;
    }

    public List<Feedback> findAll() {
        return feedbackRepository.findAll();
    }

    private String resolveSentiment(int rating) {
        if (rating >= 4) return "Positive";
        if (rating == 3) return "Neutral";
        return "Negative";
    }
}
