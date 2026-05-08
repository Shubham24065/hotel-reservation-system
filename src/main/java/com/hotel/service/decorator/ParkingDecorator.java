package com.hotel.service.decorator;

public class ParkingDecorator extends PriceDecorator {
    private final long nights;

    public ParkingDecorator(PriceComponent component, long nights) {
        super(component);
        this.nights = nights;
    }

    @Override
    public double getCost() {
        return component.getCost() + (15.0 * nights);
    }

    @Override
    public String getDescription() {
        return component.getDescription() + ", Parking";
    }
}
