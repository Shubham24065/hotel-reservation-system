package com.hotel.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "activity_logs")
public class ActivityLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp;
    private String actor;
    private String action;
    private String entityType;
    private String entityIdentifier;
    @Column(length = 500)
    private String message;

    @PrePersist
    public void prePersist() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }

    public ActivityLog() {
    }

    public ActivityLog(String actor, String action, String entityType, String entityIdentifier, String message) {
        this.actor = actor;
        this.action = action;
        this.entityType = entityType;
        this.entityIdentifier = entityIdentifier;
        this.message = message;
    }

    public Long getId() { return id; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getActor() { return actor; }
    public String getAction() { return action; }
    public String getEntityType() { return entityType; }
    public String getEntityIdentifier() { return entityIdentifier; }
    public String getMessage() { return message; }
}
