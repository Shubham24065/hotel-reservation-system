package com.hotel.service;

import com.hotel.model.ActivityLog;
import com.hotel.repository.impl.ActivityLogRepository;
import com.hotel.util.AppLogger;

import java.util.List;

public class ActivityLogService {
    private final ActivityLogRepository activityLogRepository;

    public ActivityLogService(ActivityLogRepository activityLogRepository) {
        this.activityLogRepository = activityLogRepository;
    }

    public void log(String actor, String action, String entityType, String entityIdentifier, String message) {
        AppLogger.getLogger().info(actor + " | " + action + " | " + entityType + " | " + entityIdentifier + " | " + message);
        activityLogRepository.save(new ActivityLog(actor, action, entityType, entityIdentifier, message));
    }

    public List<ActivityLog> findAll() {
        return activityLogRepository.findAll();
    }
}
