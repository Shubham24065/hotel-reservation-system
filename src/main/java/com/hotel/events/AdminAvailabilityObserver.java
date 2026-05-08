package com.hotel.events;

import com.hotel.model.enums.RoomType;
import com.hotel.util.AppLogger;

public class AdminAvailabilityObserver implements AvailabilityObserver {
    @Override
    public void onAvailabilityChanged(RoomType roomType, String message) {
        AppLogger.getLogger().info("Availability changed for " + roomType + ": " + message);
    }
}
