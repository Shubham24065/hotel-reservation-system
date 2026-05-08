package com.hotel.repository.impl;

import com.hotel.config.JpaUtil;
import com.hotel.model.Room;
import com.hotel.model.enums.RoomType;
import jakarta.persistence.EntityManager;

import java.util.List;

public class RoomRepository extends BaseRepository<Room> {
    public RoomRepository() {
        super(Room.class);
    }

    public List<Room> findByType(RoomType roomType) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            return em.createQuery("select r from Room r where r.roomType = :roomType", Room.class)
                    .setParameter("roomType", roomType)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
