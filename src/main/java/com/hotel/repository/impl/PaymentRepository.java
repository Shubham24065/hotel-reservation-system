package com.hotel.repository.impl;

import com.hotel.config.JpaUtil;
import com.hotel.model.Payment;
import jakarta.persistence.EntityManager;

import java.util.List;

public class PaymentRepository extends BaseRepository<Payment> {
    public PaymentRepository() {
        super(Payment.class);
    }

    public List<Payment> findByReservationId(Long reservationId) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            return em.createQuery("select p from Payment p where p.reservation.id = :reservationId order by p.createdAt desc", Payment.class)
                    .setParameter("reservationId", reservationId)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
