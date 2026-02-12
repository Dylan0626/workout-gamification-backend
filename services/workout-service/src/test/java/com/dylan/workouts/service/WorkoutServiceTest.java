package com.dylan.workouts.service;

import com.dylan.workouts.dto.CreateWorkoutRequest;
import com.dylan.workouts.kafka.WorkoutEventProducer;
import com.dylan.workouts.repository.WorkoutRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WorkoutServiceTest {

    @Test
    void createWorkout_savesWorkout_andPublishesEvent() {
        WorkoutRepository repo = mock(WorkoutRepository.class);
        WorkoutEventProducer producer = mock(WorkoutEventProducer.class);

        WorkoutService service = new WorkoutService(repo, producer);

        CreateWorkoutRequest req = new CreateWorkoutRequest();
        req.setUserId("dylan");
        req.setWorkoutType("run");
        req.setDurationMinutes(30);
        req.setCaloriesBurned(250);

        service.createWorkout(req);

        verify(repo, times(1)).save(any());
        verify(producer, times(1)).publishWorkoutCompleted(any());

        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(producer).publishWorkoutCompleted((com.dylan.workouts.kafka.WorkoutCompletedEvent) any());
    }

    @Test
    void createWorkout_invalidType_throws() {
        WorkoutRepository repo = mock(WorkoutRepository.class);
        WorkoutEventProducer producer = mock(WorkoutEventProducer.class);

        WorkoutService service = new WorkoutService(repo, producer);

        CreateWorkoutRequest req = new CreateWorkoutRequest();
        req.setUserId("dylan");
        req.setWorkoutType("swimmm"); // invalid
        req.setDurationMinutes(30);
        req.setCaloriesBurned(250);

        assertThrows(IllegalArgumentException.class, () -> service.createWorkout(req));
        verifyNoInteractions(repo);
        verifyNoInteractions(producer);
    }
}
