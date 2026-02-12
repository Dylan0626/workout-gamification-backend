package com.dylan.workouts.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateWorkoutRequest {

    @NotBlank
    private String userId;

    @NotNull
    private String workoutType;

    @Min(1)
    private int durationMinutes;

    @Min(0)
    private int caloriesBurned;

    public String getUserId() { return userId; }
    public String getWorkoutType() { return workoutType; }
    public int getDurationMinutes() { return durationMinutes; }
    public int getCaloriesBurned() { return caloriesBurned; }

    public void setUserId(String userId) { this.userId = userId; }
    public void setWorkoutType(String workoutType) { this.workoutType = workoutType; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }
    public void setCaloriesBurned(int caloriesBurned) { this.caloriesBurned = caloriesBurned; }
}
