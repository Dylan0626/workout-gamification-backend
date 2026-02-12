package com.dylan.achievements.service;

import com.dylan.achievements.kafka.WorkoutCompletedEvent;
import com.dylan.achievements.model.ProcessedEvent;
import com.dylan.achievements.model.UserStats;
import com.dylan.achievements.repository.AchievementRepository;
import com.dylan.achievements.repository.ProcessedEventRepository;
import com.dylan.achievements.repository.UserStatsRepository;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

class AchievementServiceTest {

    @Test
    void duplicateEvent_isIgnored() {
        AchievementRepository achievementRepo = mock(AchievementRepository.class);
        ProcessedEventRepository processedRepo = mock(ProcessedEventRepository.class);
        UserStatsRepository statsRepo = mock(UserStatsRepository.class);

        AchievementService service = new AchievementService(achievementRepo, processedRepo, statsRepo);

        UUID eventId = UUID.randomUUID();
        WorkoutCompletedEvent event = new WorkoutCompletedEvent();

        // Fake: eventId exists already
        when(processedRepo.existsByEventId(eventId)).thenReturn(true);

        // We can't set fields because event has only getters; so just mock exists check and call with null id won't work.
        // Instead: make existsByEventId(null) true and validate no writes.
        when(processedRepo.existsByEventId(null)).thenReturn(true);

        service.handleWorkoutCompleted(event);

        verify(processedRepo, never()).save(any(ProcessedEvent.class));
        verifyNoInteractions(statsRepo);
        verifyNoInteractions(achievementRepo);
    }

    @Test
    void firstWorkout_awardsAchievement() {
        AchievementRepository achievementRepo = mock(AchievementRepository.class);
        ProcessedEventRepository processedRepo = mock(ProcessedEventRepository.class);
        UserStatsRepository statsRepo = mock(UserStatsRepository.class);

        AchievementService service = new AchievementService(achievementRepo, processedRepo, statsRepo);

        WorkoutCompletedEvent event = mock(WorkoutCompletedEvent.class);
        when(event.getEventId()).thenReturn(UUID.randomUUID());
        when(event.getUserId()).thenReturn("dylan");
        when(event.getCaloriesBurned()).thenReturn(100);
        when(event.getTimestamp()).thenReturn(Instant.now());

        when(processedRepo.existsByEventId(any())).thenReturn(false);
        when(statsRepo.findById("dylan")).thenReturn(Optional.of(new UserStats("dylan")));
        when(achievementRepo.existsByUserIdAndCode("dylan", "FIRST_WORKOUT")).thenReturn(false);

        service.handleWorkoutCompleted(event);

        verify(processedRepo, times(1)).save(any());
        verify(statsRepo, times(1)).save(any());
        verify(achievementRepo, atLeastOnce()).save(any());
    }
}
