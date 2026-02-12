package com.dylan.workouts.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class WorkoutEventProducer {

    private final KafkaTemplate<String, WorkoutCompletedEvent> kafkaTemplate;
    private final String topicName;

    public WorkoutEventProducer(
            KafkaTemplate<String, WorkoutCompletedEvent> kafkaTemplate,
            @Value("${app.kafka.topic.workout-completed}") String topicName
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public void publishWorkoutCompleted(WorkoutCompletedEvent event) {
        // key = workoutId to keep ordering per workout + helps partitioning story
        kafkaTemplate.send(topicName, event.getWorkoutId().toString(), event);
    }
}
