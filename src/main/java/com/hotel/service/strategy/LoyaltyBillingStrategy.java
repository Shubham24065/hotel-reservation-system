package com.hotel.service.strategy;

public class LoyaltyBillingStrategy implements BillingStrategy {
    private final double redemptionValue;

    public LoyaltyBillingStrategy(double redemptionValue) {
        this.redemptionValue = redemptionValue;
    }

    @Override
    public double apply(double amount) {
        return Math.max(0, amount - redemptionValue);
    }
}
