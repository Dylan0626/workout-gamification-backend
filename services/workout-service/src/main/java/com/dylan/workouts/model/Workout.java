package com.dylan.workouts.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "workouts")
public class Workout {

    @Id
    @Column(name = "workout_id", nullable = false)
    private UUID workoutId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "workout_type", nullable = false)
    private WorkoutType workoutType;

    @Column(name = "duration_minutes", nullable = false)
    private int durationMinutes;

    @Column(name = "calories_burned", nullable = false)
    private int caloriesBurned;

    @Column(name = "workout_ts", nullable = false)
    private Instant timestamp;

    protected Workout() {
        // for JPA
    }

    public Workout(UUID workoutId, String userId, WorkoutType workoutType, int durationMinutes, int caloriesBurned, Instant timestamp) {
        this.workoutId = workoutId;
        this.userId = userId;
        this.workoutType = workoutType;
        this.durationMinutes = durationMinutes;
        this.caloriesBurned = caloriesBurned;
        this.timestamp = timestamp;
    }

    public UUID getWorkoutId() { return workoutId; }
    public String getUserId() { return userId; }
    public WorkoutType getWorkoutType() { return workoutType; }
    public int getDurationMinutes() { return durationMinutes; }
    public int getCaloriesBurned() { return caloriesBurned; }
    public Instant getTimestamp() { return timestamp; }
}
