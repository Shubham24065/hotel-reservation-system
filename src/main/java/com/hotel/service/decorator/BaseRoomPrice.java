package com.hotel.service.decorator;

public class BaseRoomPrice implements PriceComponent {
    private final String description;
    private final double cost;

    public BaseRoomPrice(String description, double cost) {
        this.description = description;
        this.cost = cost;
    }

    @Override
    public double getCost() {
        return cost;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
