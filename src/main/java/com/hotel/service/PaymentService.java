package com.hotel.service;

import com.hotel.model.Payment;
import com.hotel.model.Reservation;
import com.hotel.model.enums.PaymentMethod;
import com.hotel.repository.impl.PaymentRepository;

import java.util.List;

public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final ReservationService reservationService;
    private final LoyaltyService loyaltyService;
    private final ActivityLogService activityLogService;

    public PaymentService(PaymentRepository paymentRepository,
                          ReservationService reservationService,
                          LoyaltyService loyaltyService,
                          ActivityLogService activityLogService) {
        this.paymentRepository = paymentRepository;
        this.reservationService = reservationService;
        this.loyaltyService = loyaltyService;
        this.activityLogService = activityLogService;
    }

    public Payment processPayment(String reservationCode, PaymentMethod method, double amount, String actor) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than zero.");
        }
        Reservation reservation = reservationService.findByCode(reservationCode);
        if (amount > reservation.getOutstandingBalance()) {
            throw new IllegalArgumentException("Payment cannot exceed outstanding balance.");
        }

        Payment payment = new Payment();
        payment.setReservation(reservation);
        payment.setMethod(method);
        payment.setAmount(amount);
        payment.setNote("Processed by " + actor);
        Payment saved = paymentRepository.save(payment);

        double newOutstanding = reservation.getOutstandingBalance() - amount;
        reservationService.updateAfterPayment(reservation, newOutstanding, amount);
        loyaltyService.earnPoints(reservation.getGuest(), amount);
        activityLogService.log(actor, "PROCESS_PAYMENT", "Reservation", reservationCode, "Payment processed: " + amount + " via " + method);
        return saved;
    }

    public List<Payment> findPaymentsByReservation(Long reservationId) {
        return paymentRepository.findByReservationId(reservationId);
    }
}
