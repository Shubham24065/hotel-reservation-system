package com.hotel.service;

import com.hotel.model.Guest;
import com.hotel.util.IdGenerator;

public class LoyaltyService {
    private static final double EARNING_RATE = 0.1;
    private final GuestService guestService;
    private final ActivityLogService activityLogService;

    public LoyaltyService(GuestService guestService, ActivityLogService activityLogService) {
        this.guestService = guestService;
        this.activityLogService = activityLogService;
    }

    public void ensureLoyaltyEnrollment(Guest guest) {
        if (guest.isLoyaltyMember() && (guest.getLoyaltyNumber() == null || guest.getLoyaltyNumber().isBlank())) {
            guest.setLoyaltyNumber(IdGenerator.loyaltyNumber());
        }
        guestService.saveOrUpdate(guest);
    }

    public void earnPoints(Guest guest, double paidAmount) {
        if (!guest.isLoyaltyMember()) {
            return;
        }
        int earnedPoints = (int) Math.round(paidAmount * EARNING_RATE);
        guest.setLoyaltyPoints(guest.getLoyaltyPoints() + earnedPoints);
        guestService.saveOrUpdate(guest);
        activityLogService.log("SYSTEM", "LOYALTY_EARN", "Guest", guest.getPassportNumber(), "Earned " + earnedPoints + " loyalty points.");
    }

    public double redeemPoints(Guest guest, int pointsToRedeem) {
        if (!guest.isLoyaltyMember() || pointsToRedeem <= 0) {
            return 0;
        }
        int allowed = Math.min(guest.getLoyaltyPoints(), pointsToRedeem);
        guest.setLoyaltyPoints(guest.getLoyaltyPoints() - allowed);
        guestService.saveOrUpdate(guest);
        activityLogService.log("SYSTEM", "LOYALTY_REDEEM", "Guest", guest.getPassportNumber(), "Redeemed " + allowed + " loyalty points.");
        return allowed * 0.05;
    }
}
