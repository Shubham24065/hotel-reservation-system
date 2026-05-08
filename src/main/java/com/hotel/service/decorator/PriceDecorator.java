package com.hotel.service.decorator;

public abstract class PriceDecorator implements PriceComponent {
    protected final PriceComponent component;

    protected PriceDecorator(PriceComponent component) {
        this.component = component;
    }
}
