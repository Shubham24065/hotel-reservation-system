package com.hotel.service.decorator;

public class BreakfastDecorator extends PriceDecorator {
    private final long nights;

    public BreakfastDecorator(PriceComponent component, long nights) {
        super(component);
        this.nights = nights;
    }

    @Override
    public double getCost() {
        return component.getCost() + (30.0 * nights);
    }

    @Override
    public String getDescription() {
        return component.getDescription() + ", Breakfast";
    }
}
