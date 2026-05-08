package com.hotel.service.strategy;

public class DiscountBillingStrategy implements BillingStrategy {
    private final double discountPercent;

    public DiscountBillingStrategy(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    @Override
    public double apply(double amount) {
        return amount - (amount * discountPercent / 100.0);
    }
}
