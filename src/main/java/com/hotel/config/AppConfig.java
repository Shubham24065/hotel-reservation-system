package com.hotel.config;

import com.hotel.events.AvailabilityNotifier;
import com.hotel.repository.impl.*;
import com.hotel.service.*;

public final class AppConfig {
    private static final ActivityLogService activityLogService = new ActivityLogService(new ActivityLogRepository());
    private static final GuestService guestService = new GuestService(new GuestRepository());
    private static final RoomService roomService = new RoomService(new RoomRepository());
    private static final LoyaltyService loyaltyService = new LoyaltyService(guestService, activityLogService);
    private static final ReservationService reservationService = new ReservationService(
            new ReservationRepository(), guestService, roomService, loyaltyService, activityLogService, new AvailabilityNotifier());
    private static final PaymentService paymentService = new PaymentService(new PaymentRepository(), reservationService, loyaltyService, activityLogService);
    private static final FeedbackService feedbackService = new FeedbackService(new FeedbackRepository(), reservationService, activityLogService);
    private static final ReportingService reportingService = new ReportingService(reservationService, activityLogService, feedbackService);
    private static final AuthService authService = new AuthService(new AdminUserRepository(), activityLogService);
    private static final WaitlistService waitlistService = new WaitlistService(new WaitlistRepository(), activityLogService);
    private static final DatabaseInitializer databaseInitializer = new DatabaseInitializer(authService, roomService);

    private AppConfig() {
    }

    public static ActivityLogService activityLogService() { return activityLogService; }
    public static GuestService guestService() { return guestService; }
    public static RoomService roomService() { return roomService; }
    public static LoyaltyService loyaltyService() { return loyaltyService; }
    public static ReservationService reservationService() { return reservationService; }
    public static PaymentService paymentService() { return paymentService; }
    public static FeedbackService feedbackService() { return feedbackService; }
    public static ReportingService reportingService() { return reportingService; }
    public static AuthService authService() { return authService; }
    public static WaitlistService waitlistService() { return waitlistService; }
    public static DatabaseInitializer databaseInitializer() { return databaseInitializer; }
}
