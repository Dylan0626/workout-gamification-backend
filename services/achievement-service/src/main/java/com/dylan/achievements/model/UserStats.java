package com.dylan.achievements.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "user_stats")
public class UserStats {

    // user_id is the primary key (one stats row per user)
    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;

    // total workouts completed by the user
    @Column(name = "workouts_count", nullable = false)
    private int workoutsCount;

    // total calories burned across all workouts
    @Column(name = "calories_total", nullable = false)
    private int caloriesTotal;

    // last workout data (for streak calculation)
    @Column(name = "last_workout_date")
    private LocalDate lastWorkoutDate;

    // streak of consecutive days
    @Column(name = "streak_days", nullable = false)
    private int streakDays;

    protected UserStats() {}

    public UserStats(String userId) {
        this.userId = userId;
        this.workoutsCount = 0;
        this.caloriesTotal = 0;
        this.lastWorkoutDate = null;
        this.streakDays = 0;
    }

    public String getUserId() { return userId; }
    public int getWorkoutsCount() { return workoutsCount; }
    public int getCaloriesTotal() { return caloriesTotal; }
    public LocalDate getLastWorkoutDate() { return lastWorkoutDate; }
    public int getStreakDays() { return streakDays; }

    public void incrementWorkouts() { this.workoutsCount += 1; }
    public void addCalories(int calories) { this.caloriesTotal += calories; }

    public void updateStreak(LocalDate workoutDate) {
        if (lastWorkoutDate == null) {
            streakDays = 1;
        } else if (lastWorkoutDate.plusDays(1).equals(workoutDate)) {
            streakDays += 1;
        } else if (lastWorkoutDate.equals(workoutDate)) {
            // same day workout doesn't increase streak
        } else {
            streakDays = 1;
        }
        lastWorkoutDate = workoutDate;
    }
}
