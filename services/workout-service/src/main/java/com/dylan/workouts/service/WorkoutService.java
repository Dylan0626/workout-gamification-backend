package com.dylan.workouts.service;

import com.dylan.workouts.dto.CreateWorkoutRequest;
import com.dylan.workouts.kafka.WorkoutCompletedEvent;
import com.dylan.workouts.kafka.WorkoutEventProducer;
import com.dylan.workouts.model.Workout;
import com.dylan.workouts.model.WorkoutType;
import com.dylan.workouts.repository.WorkoutRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;

    // producer publishes Kafka events
    private final WorkoutEventProducer producer;

    public WorkoutService(WorkoutRepository workoutRepository, WorkoutEventProducer producer) {
        this.workoutRepository = workoutRepository;
        this.producer = producer;
    }

    /*
     * Creates a workout record in Postgres
     * Then publishes a Kafka event so other services can react
     */
    @Transactional
    public Workout createWorkout(CreateWorkoutRequest req) {
        UUID workoutId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        WorkoutType type = parseWorkoutType(req.getWorkoutType());

        Instant now = Instant.now();
        Workout workout = new Workout(
                workoutId,
                req.getUserId(),
                type,
                req.getDurationMinutes(),
                req.getCaloriesBurned(),
                now
        );
        
        // persist workout to DB
        workoutRepository.save(workout);

        /*
         * Build Kafka event payload
         * This event will be consumed by achievement-service
         */
        WorkoutCompletedEvent event = new WorkoutCompletedEvent(
                eventId, // eventId
                workoutId,
                workout.getUserId(),
                workout.getWorkoutType().name(),
                workout.getDurationMinutes(),
                workout.getCaloriesBurned(),
                workout.getTimestamp()
        );

        // send event to Kafka
        producer.publishWorkoutCompleted(event);
        return workout;
    }

    /*
     * Lists all workouts for the given user
     */
    public List<Workout> listWorkouts(String userId) {
        return workoutRepository.findByUserIdOrderByTimestampDesc(userId);
    }

    /*
     * Converts string workoutType into enum 
     */
    private WorkoutType parseWorkoutType(String value) {
        try {
            return WorkoutType.valueOf(value.trim().toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Unsupported workoutType: " + value);
        }
    }
}
