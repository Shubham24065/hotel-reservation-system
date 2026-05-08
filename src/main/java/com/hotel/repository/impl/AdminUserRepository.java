package com.hotel.repository.impl;

import com.hotel.config.JpaUtil;
import com.hotel.model.AdminUser;
import jakarta.persistence.EntityManager;

import java.util.Optional;

public class AdminUserRepository extends BaseRepository<AdminUser> {
    public AdminUserRepository() {
        super(AdminUser.class);
    }

    public Optional<AdminUser> findByUsername(String username) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            return em.createQuery("select a from AdminUser a where lower(a.username) = lower(:username)", AdminUser.class)
                    .setParameter("username", username)
                    .getResultStream()
                    .findFirst();
        } finally {
            em.close();
        }
    }
}
