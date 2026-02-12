package com.dylan.workouts.controller;

import com.dylan.workouts.dto.CreateWorkoutRequest;
import com.dylan.workouts.dto.WorkoutResponse;
import com.dylan.workouts.model.Workout;
import com.dylan.workouts.service.WorkoutService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workouts")
public class WorkoutController {

    // Service layer contains all business logic
    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    /*
     * POST /workouts
     * Creates a workout record and triggers a Kafka event
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkoutResponse createWorkout(@Valid @RequestBody CreateWorkoutRequest req) {
        Workout created = workoutService.createWorkout(req);
        return WorkoutResponse.from(created);
    }

    /*
     * GET /workouts/{userId}
     * Lists all workouts for the user in descending order
     */
    @GetMapping("/{userId}")
    public List<WorkoutResponse> listWorkouts(@PathVariable String userId) {
        return workoutService.listWorkouts(userId).stream()
                .map(WorkoutResponse::from)
                .toList();
    }
}
