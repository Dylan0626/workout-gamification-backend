package com.dylan.achievements.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dylan.achievements.kafka.WorkoutCompletedEvent;
import com.dylan.achievements.model.Achievement;
import com.dylan.achievements.model.ProcessedEvent;
import com.dylan.achievements.model.UserStats;
import com.dylan.achievements.repository.AchievementRepository;
import com.dylan.achievements.repository.ProcessedEventRepository;
import com.dylan.achievements.repository.UserStatsRepository;


@Service
public class AchievementService {

    // JPA repository for achievements table
    private final AchievementRepository achievementRepo;

    // JPA repository for processed_events table (idempotent tracking)
    private final ProcessedEventRepository processedRepo;

    // JPA repository for user_stats table
    private final UserStatsRepository userStatsRepo;


    public AchievementService(AchievementRepository achievementRepo, ProcessedEventRepository processedRepo, UserStatsRepository userStatsRepo) {
        this.achievementRepo = achievementRepo;
        this.processedRepo = processedRepo;
        this.userStatsRepo = userStatsRepo;
    }

    /*
     * Main event handler for workout completion events
     * 
     * The method is transational which means:
     *  - ensures DB changes are commited as a unit
     *  - if exception occurs, nothing is partially saved
     */
    @Transactional
    public void handleWorkoutCompleted(WorkoutCompletedEvent event) {

        // Idempotency check -- if eventId apready processed then do nothing
        if (processedRepo.existsByEventId(event.getEventId())) {
            return;
        }

        // Mark event as processed (to prevent dupe processing later)
        processedRepo.save(new ProcessedEvent(event.getEventId(), Instant.now()));

        String userId = event.getUserId();

        /*
         * Load user stats if they exist, if not, create new stats record
         * Keeps track of totals/streaks for achievement logic
         */
        UserStats stats = userStatsRepo.findById(userId).orElseGet(() -> new UserStats(userId));

        // Update stats
        stats.incrementWorkouts();
        stats.addCalories(event.getCaloriesBurned());

        // Convert event timestamp to a UTC LocalDate for streak tracking
        stats.updateStreak(event.getTimestamp().atZone(java.time.ZoneOffset.UTC).toLocalDate());

        // Persist updated streaks
        userStatsRepo.save(stats);

        // Collect achievements to award (if any) 
        List<Achievement> toAward = new ArrayList<>();

        // Achievement 1: First workout ever
        if (!achievementRepo.existsByUserIdAndCode(userId, "FIRST_WORKOUT")) {
            toAward.add(new Achievement(UUID.randomUUID(), userId, "FIRST_WORKOUT", "First Workout", Instant.now()));
        }

        // Achievement 2: 3 workouts completed
        if (stats.getWorkoutsCount() >= 3 && !achievementRepo.existsByUserIdAndCode(userId, "THREE_WORKOUTS")) {
            toAward.add(new Achievement(UUID.randomUUID(), userId, "THREE_WORKOUTS", "3 Workouts Completed", Instant.now()));
        }

        // Achievement 3: Burn 1000 calories total
        if (stats.getCaloriesTotal() >= 1000 && !achievementRepo.existsByUserIdAndCode(userId, "CALORIES_1000")) {
            toAward.add(new Achievement(UUID.randomUUID(), userId, "CALORIES_1000", "1000 Calories Burned Total", Instant.now()));
        }

        // Achievement 4: streak of 3 days
        if (stats.getStreakDays() >= 3 && !achievementRepo.existsByUserIdAndCode(userId, "STREAK_3")) {
            toAward.add(new Achievement(UUID.randomUUID(), userId, "STREAK_3", "Workout Streak: 3 Days", Instant.now()));
        }

        /*
         * Save each achievement
         * If another request/thread already inserted it, unique constraints triggers
         */
        for (Achievement a : toAward) {
            try {
                achievementRepo.save(a);
            } catch (DataIntegrityViolationException ignored) {}
                // Ignored because uniqueness prevents duplicates
        }
    }


    /* 
     * Returns all achievements for a user, newest first
     */
    public List<Achievement> listForUser(String userId) {
        return achievementRepo.findByUserIdOrderByAwardedAtDesc(userId);
    }
}
