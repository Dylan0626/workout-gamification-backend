package com.dylan.achievements.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "achievements")
public class Achievement {

    // Primary key
    @Id
    @Column(name = "achievement_id", nullable = false)
    private UUID achievementId;

    // Which user earned this achievement
    @Column(name = "user_id", nullable = false)
    private String userId;

    // Code identifier (like FIRST_WORKOUT, THREE_WORKOUTS)
    @Column(name = "code", nullable = false)
    private String code;

    // Display title shown to users
    @Column(name = "title", nullable = false)
    private String title;

    // Timestamp when awarded
    @Column(name = "awarded_at", nullable = false)
    private Instant awardedAt;

    // Required by JPA
    protected Achievement() {}

    public Achievement(UUID achievementId, String userId, String code, String title, Instant awardedAt) {
        this.achievementId = achievementId;
        this.userId = userId;
        this.code = code;
        this.title = title;
        this.awardedAt = awardedAt;
    }

    public UUID getAchievementId() { return achievementId; }
    public String getUserId() { return userId; }
    public String getCode() { return code; }
    public String getTitle() { return title; }
    public Instant getAwardedAt() { return awardedAt; }
}
