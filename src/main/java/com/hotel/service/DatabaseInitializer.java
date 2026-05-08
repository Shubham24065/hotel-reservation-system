package com.hotel.service;

import com.hotel.model.Room;
import com.hotel.model.enums.RoomType;

public class DatabaseInitializer {
    private final AuthService authService;
    private final RoomService roomService;

    public DatabaseInitializer(AuthService authService, RoomService roomService) {
        this.authService = authService;
        this.roomService = roomService;
    }

    public void initialize() {
        authService.seedDefaultAdmins();
        if (roomService.findAll().isEmpty()) {
            seedRoom("101", RoomType.SINGLE);
            seedRoom("102", RoomType.SINGLE);
            seedRoom("201", RoomType.DOUBLE);
            seedRoom("202", RoomType.DOUBLE);
            seedRoom("301", RoomType.DELUXE);
            seedRoom("401", RoomType.PENTHOUSE);
        }
    }

    private void seedRoom(String number, RoomType roomType) {
        Room room = new Room();
        room.setRoomNumber(number);
        room.setRoomType(roomType);
        room.setCapacity(roomType.getCapacity());
        room.setBasePrice(roomType.getBasePrice());
        room.setAvailable(true);
        roomService.save(room);
    }
}
