package com.hotel.repository.impl;

import com.hotel.config.JpaUtil;
import com.hotel.model.Reservation;
import com.hotel.model.enums.ReservationStatus;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ReservationRepository extends BaseRepository<Reservation> {
    public ReservationRepository() {
        super(Reservation.class);
    }

    public Optional<Reservation> findByCode(String reservationCode) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            return em.createQuery("select r from Reservation r where r.reservationCode = :code", Reservation.class)
                    .setParameter("code", reservationCode)
                    .getResultStream().findFirst();
        } finally {
            em.close();
        }
    }

    public List<Reservation> search(String keyword) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            String q = keyword == null ? "" : keyword.toLowerCase();
            return em.createQuery("""
                    select r from Reservation r
                    where lower(r.reservationCode) like :q
                    or lower(r.guest.firstName) like :q
                    or lower(r.guest.lastName) like :q
                    or lower(r.guest.phone) like :q
                    order by r.createdAt desc
                    """, Reservation.class)
                    .setParameter("q", "%" + q + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public long countActiveReservationsByDateRange(LocalDate start, LocalDate end) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            return em.createQuery("""
                    select count(r) from Reservation r
                    where r.status <> :cancelled
                    and r.checkInDate < :endDate
                    and r.checkOutDate > :startDate
                    """, Long.class)
                    .setParameter("cancelled", ReservationStatus.CANCELLED)
                    .setParameter("startDate", start)
                    .setParameter("endDate", end)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }
}
