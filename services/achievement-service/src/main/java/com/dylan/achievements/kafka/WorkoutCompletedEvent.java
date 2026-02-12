package com.dylan.achievements.kafka;

import java.time.Instant;
import java.util.UUID;

public class WorkoutCompletedEvent {
    private UUID eventId;
    private UUID workoutId;
    private String userId;
    private String workoutType;
    private int durationMinutes;
    private int caloriesBurned;
    private Instant timestamp;

    public WorkoutCompletedEvent() {}

    public UUID getEventId() { return eventId; }
    public UUID getWorkoutId() { return workoutId; }
    public String getUserId() { return userId; }
    public String getWorkoutType() { return workoutType; }
    public int getDurationMinutes() { return durationMinutes; }
    public int getCaloriesBurned() { return caloriesBurned; }
    public Instant getTimestamp() { return timestamp; }
}
