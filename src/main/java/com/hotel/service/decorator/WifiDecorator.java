package com.hotel.service.decorator;

public class WifiDecorator extends PriceDecorator {
    public WifiDecorator(PriceComponent component) {
        super(component);
    }

    @Override
    public double getCost() {
        return component.getCost() + 20.0;
    }

    @Override
    public String getDescription() {
        return component.getDescription() + ", Wi-Fi";
    }
}
