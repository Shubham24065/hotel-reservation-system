package com.hotel.service;

import com.hotel.model.WaitlistEntry;
import com.hotel.model.enums.RoomType;
import com.hotel.repository.impl.WaitlistRepository;

import java.time.LocalDate;
import java.util.List;

public class WaitlistService {
    private final WaitlistRepository waitlistRepository;
    private final ActivityLogService activityLogService;

    public WaitlistService(WaitlistRepository waitlistRepository, ActivityLogService activityLogService) {
        this.waitlistRepository = waitlistRepository;
        this.activityLogService = activityLogService;
    }

    public WaitlistEntry addEntry(String guestName, String phone, String email, RoomType roomType, LocalDate startDate, LocalDate endDate) {
        WaitlistEntry entry = new WaitlistEntry();
        entry.setGuestName(guestName);
        entry.setPhone(phone);
        entry.setEmail(email);
        entry.setDesiredRoomType(roomType);
        entry.setStartDate(startDate);
        entry.setEndDate(endDate);
        entry.setStatus("WAITING");
        WaitlistEntry saved = waitlistRepository.save(entry);
        activityLogService.log("SYSTEM", "ADD_WAITLIST", "Waitlist", String.valueOf(saved.getId()), "Waitlist entry created.");
        return saved;
    }

    public List<WaitlistEntry> findAll() {
        return waitlistRepository.findAll();
    }
}
