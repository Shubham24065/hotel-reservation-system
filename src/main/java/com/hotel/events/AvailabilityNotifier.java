package com.hotel.events;

import com.hotel.model.enums.RoomType;

import java.util.ArrayList;
import java.util.List;

public class AvailabilityNotifier {
    private final List<AvailabilityObserver> observers = new ArrayList<>();

    public void subscribe(AvailabilityObserver observer) {
        observers.add(observer);
    }

    public void notifyAll(RoomType roomType, String message) {
        for (AvailabilityObserver observer : observers) {
            observer.onAvailabilityChanged(roomType, message);
        }
    }
}
