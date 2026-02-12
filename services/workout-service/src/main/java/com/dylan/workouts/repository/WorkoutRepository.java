package com.dylan.workouts.repository;

import com.dylan.workouts.model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WorkoutRepository extends JpaRepository<Workout, UUID> {
    List<Workout> findByUserIdOrderByTimestampDesc(String userId);
}