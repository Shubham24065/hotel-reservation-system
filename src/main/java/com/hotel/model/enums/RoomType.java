package com.hotel.model.enums;

public enum RoomType {
    SINGLE(2, 120.0),
    DOUBLE(4, 220.0),
    DELUXE(2, 260.0),
    PENTHOUSE(2, 450.0);

    private final int capacity;
    private final double basePrice;

    RoomType(int capacity, double basePrice) {
        this.capacity = capacity;
        this.basePrice = basePrice;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getBasePrice() {
        return basePrice;
    }
}
