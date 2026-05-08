package com.hotel.model.enums;

public enum AddOnType {
    BREAKFAST(30.0, true),
    WIFI(20.0, false),
    PARKING(15.0, true),
    SPA(40.0, false);

    private final double price;
    private final boolean perNight;

    AddOnType(double price, boolean perNight) {
        this.price = price;
        this.perNight = perNight;
    }

    public double getPrice() {
        return price;
    }

    public boolean isPerNight() {
        return perNight;
    }
}
