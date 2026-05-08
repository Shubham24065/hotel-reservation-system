package com.hotel.events;

import com.hotel.model.enums.RoomType;

public interface AvailabilityObserver {
    void onAvailabilityChanged(RoomType roomType, String message);
}
