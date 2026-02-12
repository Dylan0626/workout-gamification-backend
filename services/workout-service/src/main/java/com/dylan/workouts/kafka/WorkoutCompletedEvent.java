package com.dylan.workouts.kafka;

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

    public WorkoutCompletedEvent(UUID eventId, UUID workoutId, String userId, String workoutType, int durationMinutes, int caloriesBurned, Instant timestamp) {
        this.eventId = eventId;
        this.workoutId = workoutId;
        this.userId = userId;
        this.workoutType = workoutType;
        this.durationMinutes = durationMinutes;
        this.caloriesBurned = caloriesBurned;
        this.timestamp = timestamp;
    }

    public UUID getEventId() { return eventId; }
    public UUID getWorkoutId() { return workoutId; }
    public String getUserId() { return userId; }
    public String getWorkoutType() { return workoutType; }
    public int getDurationMinutes() { return durationMinutes; }
    public int getCaloriesBurned() { return caloriesBurned; }
    public Instant getTimestamp() { return timestamp; }
}
