package com.hotel.repository.impl;

import com.hotel.config.JpaUtil;
import com.hotel.model.Guest;
import jakarta.persistence.EntityManager;

import java.util.Optional;

public class GuestRepository extends BaseRepository<Guest> {
    public GuestRepository() {
        super(Guest.class);
    }

    public Optional<Guest> findByPassport(String passport) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            return em.createQuery("select g from Guest g where g.passportNumber = :passport", Guest.class)
                    .setParameter("passport", passport)
                    .getResultStream().findFirst();
        } finally {
            em.close();
        }
    }
}
