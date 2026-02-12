package com.dylan.achievements.kafka;

import com.dylan.achievements.service.AchievementService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class WorkoutCompletedListener {

    // Handles all business logic when an event is received
    private final AchievementService achievementService;

    public WorkoutCompletedListener(AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    /*
     * Listens for workout completion events published by workout-service
     * 
     * When an event is received, achievement service 
     *  - updates user stats
     *  - checks if new achievements should be awarded
     *  - saves them to DB
     */
    @KafkaListener(topics = "${app.kafka.topic.workout-completed}", containerFactory = "kafkaListenerContainerFactory")
    public void onMessage(WorkoutCompletedEvent event) {
        achievementService.handleWorkoutCompleted(event);
    }
}
