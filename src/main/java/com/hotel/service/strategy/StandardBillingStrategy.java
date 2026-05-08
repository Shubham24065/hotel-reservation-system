package com.hotel.service.strategy;

public class StandardBillingStrategy implements BillingStrategy {
    @Override
    public double apply(double amount) {
        return amount;
    }
}
