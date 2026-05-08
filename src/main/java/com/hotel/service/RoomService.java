package com.hotel.service;

import com.hotel.model.Room;
import com.hotel.model.enums.RoomType;
import com.hotel.repository.impl.RoomRepository;

import java.util.List;

public class RoomService {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    public Room save(Room room) {
        return roomRepository.save(room);
    }

    public long totalRooms() {
        return roomRepository.findAll().size();
    }

    public List<Room> findByType(RoomType roomType) {
        return roomRepository.findByType(roomType);
    }
}
