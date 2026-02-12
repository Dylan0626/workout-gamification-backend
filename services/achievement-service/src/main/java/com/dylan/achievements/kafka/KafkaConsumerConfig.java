package com.dylan.achievements.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value("${app.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${app.kafka.consumer-group}")
    private String groupId;

    /*
     * This defines how Kafka messages get deserialized into Java objects.
     * 
     * Key = String
     * Value = WorkoutCOmpletedEvent (JSON Object)
     */
    @Bean
    public ConsumerFactory<String, WorkoutCompletedEvent> consumerFactory() {

        // Tells Spring Kafka hoe to turn JSON into a WorkoutCOmpletedEvent 
        JsonDeserializer<WorkoutCompletedEvent> deserializer = new JsonDeserializer<>(WorkoutCompletedEvent.class);

        // Ensures compatibility across microservices since we disabled Kafka type headers on producer side 
        deserializer.setRemoveTypeHeaders(true);
        deserializer.setUseTypeMapperForKey(false);

        // Needed bc Spring Kafka protects against 'unsafe deserialization'
        deserializer.addTrustedPackages("*");

        Map<String, Object> props = new HashMap<>();

        // Connect to Kafka cluster
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        // Consumer group ID (for partition ownership and offset tracking)
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        // Deserializer for key/value
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer.getClass());

        // If no stored offset, read from beginning
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    /*
     * Kafka listener container factory is needed by @KafkaListener
     * It tells Spring Kafka what consumerFactory to use 
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, WorkoutCompletedEvent> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, WorkoutCompletedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
