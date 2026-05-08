package com.hotel.service.decorator;

public class SpaDecorator extends PriceDecorator {
    public SpaDecorator(PriceComponent component) {
        super(component);
    }

    @Override
    public double getCost() {
        return component.getCost() + 40.0;
    }

    @Override
    public String getDescription() {
        return component.getDescription() + ", Spa";
    }
}
