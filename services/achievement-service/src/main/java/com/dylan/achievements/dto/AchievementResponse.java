package com.dylan.achievements.dto;

import com.dylan.achievements.model.Achievement;

import java.time.Instant;
import java.util.UUID;

public class AchievementResponse {
    
    // DTO fields that get serialized to JSON
    private UUID achievementId;
    private String userId;
    private String code;
    private String title;
    private Instant awardedAt;

    /*
     * Converts an Achievement entity into a response object
     * This is so we prevent directly returning the JPA entity
     */
    public static AchievementResponse from(Achievement a) {
        AchievementResponse r = new AchievementResponse();
        r.achievementId = a.getAchievementId();
        r.userId = a.getUserId();
        r.code = a.getCode();
        r.title = a.getTitle();
        r.awardedAt = a.getAwardedAt();
        return r;
    }

    public UUID getAchievementId() { return achievementId; }
    public String getUserId() { return userId; }
    public String getCode() { return code; }
    public String getTitle() { return title; }
    public Instant getAwardedAt() { return awardedAt; }
}
