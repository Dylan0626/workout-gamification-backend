package com.dylan.workouts.dto;

import com.dylan.workouts.model.Workout;
import java.time.Instant;
import java.util.UUID;

public class WorkoutResponse {
    private UUID workoutId;
    private String userId;
    private String workoutType;
    private int durationMinutes;
    private int caloriesBurned;
    private Instant timestamp;

    public static WorkoutResponse from(Workout w) {
        WorkoutResponse r = new WorkoutResponse();
        r.workoutId = w.getWorkoutId();
        r.userId = w.getUserId();
        r.workoutType = w.getWorkoutType().name();
        r.durationMinutes = w.getDurationMinutes();
        r.caloriesBurned = w.getCaloriesBurned();
        r.timestamp = w.getTimestamp();
        return r;
    }

    public UUID getWorkoutId() { return workoutId; }
    public String getUserId() { return userId; }
    public String getWorkoutType() { return workoutType; }
    public int getDurationMinutes() { return durationMinutes; }
    public int getCaloriesBurned() { return caloriesBurned; }
    public Instant getTimestamp() { return timestamp; }
}
