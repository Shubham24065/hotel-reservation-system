package com.hotel.model;

public class RoomOption {
    private final String roomType;
    private final int capacity;
    private final double pricePerNight;

    public RoomOption(String roomType, int capacity, double pricePerNight) {
        this.roomType = roomType;
        this.capacity = capacity;
        this.pricePerNight = pricePerNight;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }
}